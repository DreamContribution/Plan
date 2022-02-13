package com.frank.plan

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.room.Room
import com.frank.plan.data.Bill
import com.frank.plan.data.BillDataBase
import com.frank.plan.ui.views.HistoryList

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoryList()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

fun dataBaseTest(context: Application) {
    val db =
        Room.databaseBuilder(context, BillDataBase::class.java, "bill")
            .build()

    val billDao = db.billDao()

    val itemBill = Bill(value = 20.0, type = 1)
    Log.d(MainActivity.TAG, "onCreate: itemBill:$itemBill")
    billDao.addBill(Bill(value = 20.0, type = 1))

    val allBill = billDao.getAllBill()[0]
    Log.d(MainActivity.TAG, "onCreate: allBill:$allBill")
}