package com.frank.plan.data

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

const val TAG = "UiData"

data class ItemTabUIData(
    val name: String,
    var icon: ImageVector? = null,
    var iconResource: Painter? = null
)

data class InputData(
    val content: String,
    val type: Int,
    @DrawableRes val icon: Int? = null,
    val imageVector: ImageVector? = null
)

fun generatedInputData(): List<InputData> {
    val inputContents: ArrayList<InputData> = arrayListOf()
    inputContents.add(InputData("8", 0))
    inputContents.add(InputData("9", 0))
    inputContents.add(InputData("7", 0))
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

class PlanModel : ViewModel() {
    // 添加页面中的类型标记
    var targetAddType: Int by mutableStateOf(-1)

    // 添加页面中的数字
    var addString: String by mutableStateOf("0.00")

    fun addBill(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            BillDataBase.getDatabase(context).billDao()
                .addBill(Bill(type = targetAddType, value = addString.toDouble()))
        }
    }

    fun getAll(context: Context): Flow<List<Bill>> {
        return BillDataBase.getDatabase(context).billDao().getAllBill()
    }
}

fun inputCallBack(content: String, type: Int, viewModel: PlanModel, context: Context) {

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
                    Log.d(TAG, "inputCallBack: -->$content")
                }
                "今天" -> {
                    viewModel.viewModelScope.launch {
                        viewModel.getAll(context).collect() {
                            Log.d(TAG, "inputCallBack: --->${it.size}")
                        }
                    }
                    Log.d(TAG, "inputCallBack: -->$content")
                }
                else -> {
                    Log.d(TAG, "inputCallBack: -->do nothing")
                }
            }
            Log.d("InputCallBack", "content:$content,type:$type")
        }
    }
}
