package com.zhu.adaptergenerator_processor.data

/**
 * @author Zhu
 * @date 2021/5/16 9:36 PM
 * @desc
 */
data class DataHolder(val packageName: String,
                      val modelName: String,
                      val layoutId: Int,
                      val viewHolderBindingData: List<BindViewData>)
