package com.frank.plan.ui.views

import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.frank.plan.R
import com.frank.plan.data.*
import com.frank.plan.ui.theme.PlanTheme
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * 单日账单汇总
 */
@Composable
fun DayTotalInfo(dayBill: DayBill) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = dayBill.day.toString(),
                color = PlanTheme.colors.textColor,
                fontSize = 10.sp
            )
            Text(
                text = stringResource(id = R.string.label_out_put, dayBill.totalMoney.toString()),
                color = PlanTheme.colors.textColor,
                fontSize = 10.sp
            )
        }
        Divider(color = PlanTheme.colors.separate, thickness = 0.5.dp)
    }
}


/**
 * 单日账单详情
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemBill(showDivider: Boolean, itemData: Bill) {
    val itemUiInfo by remember {
        mutableStateOf(getItemTabUIDataById(itemData.type))
    }

    val viewModel: PlanModel = viewModel()
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .combinedClickable(onLongClick = {
                showDeleteDialog = true
            }, onClick = {})
            .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,

        ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = itemUiInfo.icon!!,
            contentDescription = "type icon",
            tint = PlanTheme.colors.iconTint
        )
        Spacer(modifier = Modifier.width(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = itemUiInfo.name, color = PlanTheme.colors.textColor)
            Text(text = itemData.value.toString(), color = PlanTheme.colors.textColor)
        }
    }
    // 是否显示分割线
    if (showDivider) {
        Divider(
            color = PlanTheme.colors.separate,
            thickness = 0.5.dp,
            modifier = Modifier.padding(start = 44.dp)
        )
    }
    // 长按显示删除对话框
    if (showDeleteDialog) {
        DeleteDialog(onConfirm = {
            viewModel.deleteItemBill(context, itemData)
            showDeleteDialog = false
        }) {
            showDeleteDialog = false
        }
    }
}

/**
 * 单日账单view
 */
@Composable
fun DayBill(dayBill: DayBill) {
    DayTotalInfo(dayBill)
    val bills = dayBill.bills
    for ((index, item) in dayBill.bills.withIndex()) {
        ItemBill(index != bills.size - 1, item)
    }
}

/**
 * 月份选择器
 */
@Composable
fun MonthSelectButton(onClick: () -> Unit) {
    val planModel: PlanModel = viewModel()
    Column(modifier = Modifier.clickable {
        onClick()
    }) {
        Text(
            text = planModel.selectedYear,
            fontSize = 15.sp,
            fontWeight = FontWeight.W200,
            color = PlanTheme.colors.titleTextColor
        )
        Row {
            Text(
                text = planModel.selectedMonth,
                fontSize = 22.sp,
                color = PlanTheme.colors.titleTextColor
            )
            Text(
                text = "月",
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Bottom),
                color = PlanTheme.colors.titleTextColor
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "content",
                modifier = Modifier
                    .padding(0.dp)
                    .align(Alignment.Bottom),
                tint = PlanTheme.colors.titleTextColor
            )
        }
    }
}

/**
 * 收入、支出统计
 */
@Composable
fun MonthlyRecordTotal(label: String, modifier: Modifier, data: Flow<FullInputPerMonth>? = null) {
    val num = data?.collectAsState(initial = null)?.value?.input?.toString() ?: "0.0"
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.W200,
            color = PlanTheme.colors.titleTextColor
        )
        Text(
            text = num,
            fontSize = 22.sp,
            fontWeight = FontWeight.W200,
            color = PlanTheme.colors.titleTextColor
        )
    }
}

/**
 * 月统计信息
 */
@Composable
fun MonthlyInfo() {
    Log.d(TAG, "MonthlyInfo: view")
    var showDialog by remember {
        mutableStateOf(false)
    }

    val planModel: PlanModel = viewModel()
    val context = LocalContext.current
    val allInputPerMonth = planModel.getFullInputByMonth(context)

    Row(
        modifier = Modifier
            .background(color = PlanTheme.colors.titleBg)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthSelectButton { showDialog = true }
        Spacer(modifier = Modifier.width(30.dp))
        MonthlyRecordTotal(label = "收入", modifier = Modifier.weight(1f))
        MonthlyRecordTotal(label = "支出", modifier = Modifier.weight(1f), data = allInputPerMonth)
        if (showDialog) {
            DateSelectorDialog {
                showDialog = false
            }
        }
    }
}


/**
 * 月份选择对话框
 */
@Composable
fun DateSelectorDialog(onDismiss: () -> Unit) {
    val viewModel: PlanModel = viewModel()
    Dialog(onDismissRequest = onDismiss) {
        AndroidView(
            factory = {
                CalendarView(it).apply {
                    val initDate = Calendar.getInstance().apply {
                        set(Calendar.YEAR, viewModel.selectedYear.toInt())
                        set(Calendar.MONTH, viewModel.selectedMonth.toInt() - 1)
                    }
                    date = initDate.timeInMillis
                }
            },
            Modifier
                .wrapContentSize()
                .background(color = PlanTheme.colors.titleBg),
            update = { view ->
                view.setOnDateChangeListener { _, year, mon, _ ->
                    viewModel.selectedMonth = String.format("%02d", mon + 1)
                    viewModel.selectedYear = year.toString()
                    onDismiss()
                }
            })
    }
}


/**
 * 删除记录对话框
 */
@Composable
fun DeleteDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .background(color = PlanTheme.colors.textColor)
                .padding(40.dp)
        ) {
            Text(text = "Need Delete?")
            Spacer(modifier = Modifier.padding(vertical = 10.dp))
            Row {
                Button(onClick = onConfirm) {
                    Text(text = "Confirm")
                }
                Spacer(modifier = Modifier.padding(horizontal = 10.dp))
                Button(onClick = onCancel) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}
