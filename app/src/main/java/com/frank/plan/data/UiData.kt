package com.frank.plan.data

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController

const val TAG = "UiData"


/**
 * 生成添加账单中页面的显示内容
 */
fun generatedInputData(): List<InputData> {
    val inputContents: ArrayList<InputData> = arrayListOf()
    inputContents.add(InputData("7", 0))
    inputContents.add(InputData("8", 0))
    inputContents.add(InputData("9", 0))
    inputContents.add(InputData("今天", 1))
    inputContents.add(InputData("4", 0))
    inputContents.add(InputData("5", 0))
    inputContents.add(InputData("6", 0))
    inputContents.add(InputData("+", 1))
    inputContents.add(InputData("1", 0))
    inputContents.add(InputData("2", 0))
    inputContents.add(InputData("3", 0))
    inputContents.add(InputData("-", 1))
    inputContents.add(InputData(".", 0))
    inputContents.add(InputData("0", 0))
    inputContents.add(InputData("删除", 1))
    inputContents.add(InputData("完成", 1))
    return inputContents
}

/**
 * 添加对话框中按钮回调
 */
fun inputCallBack(
    content: String,
    type: Int,
    viewModel: PlanModel,
    context: Context,
    nav: NavHostController
) {

    when (type) {
        0 -> {
            viewModel.addString =
                if (TextUtils.equals(
                        viewModel.addString,
                        "0.00"
                    )
                ) content else viewModel.addString + content
        }
        1 -> {
            when (content) {
                "删除" -> {
                    viewModel.addString = viewModel.addString.let {
                        if (it.isNotEmpty()) it.substring(0, it.length - 1) else "0.00"
                    }
                }
                "完成" -> {
                    viewModel.addBill(context)
                    nav.popBackStack()
                }
                "今天" -> {

                }
                else -> {
                    Log.d(TAG, "inputCallBack: -->do nothing")
                }
            }
            Log.d("InputCallBack", "content:$content,type:$type")
        }
    }
}

fun getItemTabUIDataById(id: Int): ItemTabUIData {
    return when (id) {
        1 -> ItemTabUIData(name = "餐饮", icon = Icons.Default.Create)
        2 -> ItemTabUIData(name = "购物", icon = Icons.Default.AccountBox)
        3 -> ItemTabUIData(name = "交通", icon = Icons.Default.Face)
        4 -> ItemTabUIData(name = "日用", icon = Icons.Default.AccountCircle)
        else -> ItemTabUIData(name = "其他", icon = Icons.Default.Star)
    }
}