package com.frank.plan.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


const val TAG = "HistoryList"

@Composable
fun HistoryList() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "轻松记账", fontSize = 40.sp, fontWeight = FontWeight.W900)
        MonthlyInfo()
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(10) {
                DayBill()
            }
        }
        BottomBar(modifier = Modifier.wrapContentHeight())
    }
}
