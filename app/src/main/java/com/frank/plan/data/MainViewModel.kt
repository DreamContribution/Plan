package com.frank.plan.data

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

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
        Log.d(TAG, "addBill")
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
        Log.d(TAG, "getBillByMonth: start--->")
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

    companion object {
        const val TAG = "PlanModel"
    }
}