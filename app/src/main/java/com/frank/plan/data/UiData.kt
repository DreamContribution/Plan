package com.frank.plan.data

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

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

class PlanModel : ViewModel() {
    // 添加页面中的类型标记
    var targetAddType: Int by mutableStateOf(-1)

    // 当前选择的年份
    var selectedYear: String by mutableStateOf(Calendar.getInstance().get(Calendar.YEAR).toString())

    // 当前选择的月份
    var selectedMonth: String by mutableStateOf(
        String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1)
    )

    // 添加页面中的数字
    var addString: String by mutableStateOf("0.00")

    fun addBill(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            BillDataBase.getDatabase(context).billDao()
                .addBill(Bill(type = targetAddType + 1, value = addString.toDouble()))
        }
    }

    fun getFullInputByMonth(context: Context): Flow<FullInputPerMonth> {
        Log.d(TAG, "getFullInputByMonth: $selectedYear-$selectedMonth")
        return BillDataBase.getDatabase(context = context).billDao()
            .getFullInputByMonth(
                "$selectedYear-$selectedMonth-01",
                "$selectedYear-$selectedMonth-31"
            )
    }

    /**
     * 通过当前选择的年-月，获取数据
     */
    fun getBillByMonth(
        context: Context
    ): Flow<List<DayBill>> {
        return BillDataBase.getDatabase(context).billDao()
            .getBillsByMonth("$selectedYear-$selectedMonth-01", "$selectedYear-$selectedMonth-31")
            .map {
                val listOfDayBill = mutableListOf<DayBill>()
                var lastDate = ""
                it.forEach { itemBill ->
                    if (TextUtils.equals(itemBill.time.toString(), lastDate)) {
                        listOfDayBill.last().also { itemDayBill ->
                            itemDayBill.totalMoney += itemBill.value
                            itemDayBill.bills.add(itemBill)
                        }
                    } else {
                        val newBill =
                            DayBill(day = itemBill.time, totalMoney = itemBill.value).also { new ->
                                new.bills.add(itemBill)
                            }
                        listOfDayBill.add(newBill)
                        lastDate = itemBill.time.toString()
                    }
                }
                listOfDayBill
            }
    }

    fun deleteItemBill(context: Context, itemBill: Bill) {
        viewModelScope.launch(Dispatchers.IO) {
            BillDataBase.getDatabase(context = context).billDao().deleteBill(itemBill)
        }
    }
}

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