package com.frank.plan.ui.views

import android.widget.CalendarView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.frank.plan.R
import com.frank.plan.data.*
import com.frank.plan.ui.theme.PlanTheme
import java.util.*

@Composable
fun DayTotalInfo(dayBill: DayBill) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = dayBill.day.toString(), color = Color.Gray, fontSize = 10.sp)
            Text(
                text = stringResource(id = R.string.label_out_put, dayBill.totalMoney.toString()),
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
        Divider(color = Color.Gray, thickness = 0.5.dp)
    }
}


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
    Column(modifier = Modifier.combinedClickable(onLongClick = {
        showDeleteDialog = true
    }, onClick = {})) {
        Row(
            modifier = Modifier
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = itemUiInfo.icon!!,
                contentDescription = "type icon"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(text = itemUiInfo.name)
                Text(text = itemData.value.toString())
            }
        }
        if (showDivider) {
            Divider(
                color = Color.Gray,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 44.dp)
            )
        }

        if (showDeleteDialog) {
            DeleteDialog(onConfirm = {
                viewModel.deleteItemBill(context, itemData)
                showDeleteDialog = false
            }) {
                showDeleteDialog = false
            }
        }
    }
}


@Composable
fun DayBill(dayBill: DayBill) {
    DayTotalInfo(dayBill)
    val bills = dayBill.bills
    for ((index, item) in dayBill.bills.withIndex()) {
        ItemBill(index != bills.size - 1, item)
    }
}


@Composable
fun ItemType(itemUiUIData: ItemTabUIData, viewModel: PlanModel, index: Int) {
    Column(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .border(width = 1.dp, color = Color.Transparent, shape = RoundedCornerShape(2.dp))
            .clickable {
                viewModel.targetAddType = index
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconModifier = Modifier.size(30.dp)
        val contentDesc = "test type"
        Surface(
            modifier = Modifier
                .padding(vertical = 4.dp)
        ) {
            if (itemUiUIData.icon != null) {
                Icon(
                    imageVector = itemUiUIData.icon!!,
                    modifier = iconModifier,
                    contentDescription = contentDesc,
                    tint = if (viewModel.targetAddType == index) Color.Red else Color.Black
                )
            } else if (itemUiUIData.iconResource != null) {
                Icon(
                    painter = itemUiUIData.iconResource!!,
                    modifier = iconModifier,
                    contentDescription = contentDesc,
                    tint = if (viewModel.targetAddType == index) Color.Red else Color.Black
                )
            }
        }

        Text(text = itemUiUIData.name, fontSize = 14.sp)
    }
}


@ExperimentalFoundationApi
@Composable
fun RecordTypeGridList(modifier: Modifier) {
    val planModel: PlanModel = viewModel()
    LazyVerticalGrid(
        cells = GridCells.Fixed(4),
        modifier = modifier
    ) {
        items(6) {
            val itemUiUIData by mutableStateOf(getItemTabUIDataById(it + 1))
            ItemType(itemUiUIData = itemUiUIData, viewModel = planModel, index = it)
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AddView(navController: NavHostController) {
    val viewModel: PlanModel = viewModel()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "记一笔", fontSize = 30.sp, fontWeight = FontWeight.W500)
        RecordTypeGridList(modifier = Modifier.weight(1f))
        if (viewModel.targetAddType != -1) {
            InfoInput(navController = navController)
        }
    }
}

// 输入数据界面
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfoInput(modifier: Modifier = Modifier, navController: NavHostController) {
    val generatedInputData = generatedInputData()
    val planModel: PlanModel = viewModel()
    val context = LocalContext.current.applicationContext
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Text(
            text = planModel.addString,
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 10.dp)
        )
        LazyVerticalGrid(
            cells = GridCells.Fixed(4)
        ) {
            itemsIndexed(generatedInputData, spans = null) { index, it ->
                InfoInputItem(index, it.content, it.type) { content, type ->
                    inputCallBack(content, type, planModel, context, navController)
                }
            }
        }
    }
}


@Composable
fun InfoInputItem(index: Int, data: String, type: Int, onClick: (String, Int) -> Unit) {
    Text(
        text = data,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable {
                onClick(data, type)
            }
            .drawBehind {
                val isFirstRow = index <= 3
                val isLastRow = index >= 12
                val isLastColumn = index % 4 == 3
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = if (isFirstRow) 2.dp.toPx() else {
                        1.dp.toPx()
                    }
                )
                if (!isLastColumn) {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(size.width, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
                if (isLastRow) {
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(size.width, size.height),
                        end = Offset(0f, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                }

            }
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@Composable
fun MonthSelectButton(onClick: () -> Unit) {
    val planModel: PlanModel = viewModel()
    Column(modifier = Modifier.clickable {
        onClick()
    }) {
        Text(text = planModel.selectedYear, fontSize = 15.sp, fontWeight = FontWeight.W100)
        Row {
            Text(text = planModel.selectedMonth, fontSize = 22.sp)
            Text(text = "月", fontSize = 18.sp, modifier = Modifier.align(Alignment.Bottom))
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "content",
                modifier = Modifier
                    .padding(0.dp)
                    .align(Alignment.Bottom)
            )
        }
    }
}


@Composable
fun MonthlyRecordTotal(label: String, modifier: Modifier) {
    val planModel: PlanModel = viewModel()
    val context = LocalContext.current
    val monthlyBill = planModel.getFullInputByMonth(context)
        .collectAsState(initial = FullInputPerMonth(0.0)).value
    val num = if (label == "收入") {
        0.0.toString()
    } else {
        monthlyBill.input.toString()
    }
    Column(modifier = modifier) {
        Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.W100)
        Text(text = num, fontSize = 22.sp, fontWeight = FontWeight.W200)
    }
}


@Composable
fun MonthlyInfo() {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .background(color = PlanTheme.colors.bottomBar)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthSelectButton { showDialog = true }
        Spacer(modifier = Modifier.width(30.dp))
        MonthlyRecordTotal(label = "收入", modifier = Modifier.weight(1f))
        MonthlyRecordTotal(label = "支出", modifier = Modifier.weight(1f))
        if (showDialog) {
            DateSelectorDialog {
                showDialog = false
            }
        }
    }
}

@Preview(name = "InputRemark", showBackground = true)
@Composable
fun InputMoney() {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("0")
        Spacer(modifier = Modifier.width(10.dp))
    }
}

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
                .background(color = PlanTheme.colors.bottomBar),
            update = { view ->
                view.setOnDateChangeListener { _, year, mon, _ ->
                    viewModel.selectedMonth = String.format("%02d", mon + 1)
                    viewModel.selectedYear = year.toString()
                    onDismiss()
                }
            })
    }
}

@Composable
fun DeleteDialog(onConfirm: () -> Unit, onCancel: () -> Unit) {
    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .background(color = Color.White)
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

