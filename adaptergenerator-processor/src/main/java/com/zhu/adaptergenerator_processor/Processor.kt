package com.zhu.adaptergenerator_processor

import com.squareup.kotlinpoet.FileSpec
import com.zhu.adaptergenerator_annotations.AdapterModel
import com.zhu.adaptergenerator_annotations.ViewHolderBinding
import com.zhu.adaptergenerator_processor.codegen.AdapterCodeBuilder
import com.zhu.adaptergenerator_processor.data.BindViewData
import com.zhu.adaptergenerator_processor.data.DataHolder
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() =
            mutableSetOf(AdapterModel::class.java.canonicalName)

    override fun process(annotations: MutableSet<out TypeElement>?,
                         roundEnv: RoundEnvironment): Boolean {

        val kaptGeneratedDir =
                processingEnv.options[KAPT_GENERATED]
                        ?: return false


        roundEnv.getElementsAnnotatedWith(AdapterModel::class.java)
                .forEach {
                    val dataHolder = createDataHolder(it)
                    val fileName = "${dataHolder.modelName}Adapter"
                    FileSpec.builder(dataHolder.packageName, fileName)
                            .addType(AdapterCodeBuilder(fileName, dataHolder).build())
                            .build()
                            .writeTo(File(kaptGeneratedDir))
                }
        return true
    }

    private fun createDataHolder(elem: Element): DataHolder {
        val packageName = processingEnv.elementUtils.getPackageOf(elem).toString()
        val modelName = elem.simpleName.toString()
        val annotation = elem.getAnnotation(AdapterModel::class.java) // 拿到注解对象
        val layoutId = annotation.layoutId  // 获取注解属性
        val viewHolderBindingData = elem.enclosedElements.mapNotNull { // 获取所有属性
            val viewHolderBinding = it.getAnnotation(ViewHolderBinding::class.java)
            if (viewHolderBinding == null) {
                null
            } else {
                val elementName = it.simpleName.toString()
                val fieldName = elementName.substring(0, elementName.indexOf('$'))
//                println("-----> Processor createDataHolder:"  + elementName)
//                println("-----> Processor createDataHolder:"  + fieldName)
                BindViewData(fieldName, viewHolderBinding.viewId)
            }
        }
        return DataHolder(packageName, modelName, layoutId, viewHolderBindingData)
    }

    companion object {
        const val KAPT_GENERATED = "kapt.kotlin.generated"
    }
}