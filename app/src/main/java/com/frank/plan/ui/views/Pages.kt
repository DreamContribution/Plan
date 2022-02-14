package com.frank.plan.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
            var count = 1
            items(5) {
                DayBill(count)
                count++
            }
        }
        BottomBar(modifier = Modifier.wrapContentHeight())
    }
}

@Preview(showBackground = true)
@Composable
fun TestOfSurface() {
    Icon(
        imageVector = Icons.Default.Add,
        contentDescription = "Add ",
        tint = Color.Blue,
        modifier = Modifier.background(
            Color.Red
        )
    )
}
