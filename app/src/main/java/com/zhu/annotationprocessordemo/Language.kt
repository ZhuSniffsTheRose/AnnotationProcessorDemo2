package com.zhu.annotationprocessordemo

import com.zhu.adaptergenerator_annotations.AdapterModel
import com.zhu.adaptergenerator_annotations.ViewHolderBinding

/**
 * @author Zhu
 * @date 2021/5/16 9:46 PM
 * @desc
 */
@AdapterModel(R.layout.item_language)
class Language(@ViewHolderBinding(R.id.name) val name: String){
    fun getNameFinal() = name
}