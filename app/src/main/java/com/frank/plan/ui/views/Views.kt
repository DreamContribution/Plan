package com.frank.plan.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.plan.R
import com.frank.plan.data.ItemTabUIData
import com.frank.plan.ui.theme.ComposePlanTheme

@Preview(showBackground = true)
@Composable
fun DayTotalInfo() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "02月08日 星期二", color = Color.Gray, fontSize = 10.sp)
            Text(
                text = stringResource(id = R.string.label_out_put, 20.23.toString()),
                color = Color.Gray,
                fontSize = 10.sp
            )
        }
        Divider(color = Color.Gray, thickness = 0.5.dp)
    }
}


@Composable
fun ItemBill(showDivider: Boolean) {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "type icon"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

                ) {
                Text(text = "餐饮")
                Text(text = "-12.3")
            }
        }
        if (showDivider) {
            Divider(
                color = Color.Gray,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = 54.dp)
            )
        }
    }
}


@Composable
fun DayBill(count: Int) {
    DayTotalInfo()
//    val count = Random.nextInt(1, 10)
    for (i in 1..count) {
        ItemBill(i != count)
    }
}


@Composable
fun ItemType(
    modifier: Modifier = Modifier, itemUiUIData: ItemTabUIData = ItemTabUIData(
        "餐饮",
        iconResource = painterResource(id = R.drawable.ic_launcher_foreground)
    )
) {
    var uiState by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .clickable {
                uiState = !uiState
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val iconModifier = Modifier.size(40.dp)
        val contentDesc = "test type"
        if (itemUiUIData.icon != null) {
            Icon(
                imageVector = itemUiUIData.icon!!,
                modifier = iconModifier,
                contentDescription = contentDesc,
                tint = if (uiState) Color.Red else Color.Black
            )
        } else if (itemUiUIData.iconResource != null) {
            Icon(
                painter = itemUiUIData.iconResource!!,
                modifier = iconModifier,
                contentDescription = contentDesc,
                tint = if (uiState) Color.Red else Color.Black
            )
        }
        Text(text = itemUiUIData.name, fontSize = 14.sp)
    }
}


@ExperimentalFoundationApi
@Composable
fun GridTest() {
    LazyVerticalGrid(
        cells = GridCells.Fixed(4),
    ) {
        items(2000) {
            ItemType()
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun AddView() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "记一笔", fontSize = 30.sp, fontWeight = FontWeight.W500)
        GridTest()
    }
}


@Composable
fun MonthSelectButton() {
    Row {
        Column {
            Text(text = "2022年", fontSize = 15.sp, fontWeight = FontWeight.W100)
            Row {
                Text(text = "02", fontSize = 22.sp)
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
}


@Composable
fun MonthlyRecordTotal(label: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.W100)
        Text(text = "100.00", fontSize = 22.sp, fontWeight = FontWeight.W200)
    }
}


@Composable
fun MonthlyInfo() {
    Row(
        modifier = Modifier
            .background(color = ComposePlanTheme.colors.primaryVariant)
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthSelectButton()
        Spacer(modifier = Modifier.width(30.dp))
        MonthlyRecordTotal(label = "收入", modifier = Modifier.weight(1f))
        MonthlyRecordTotal(label = "支出", modifier = Modifier.weight(1f))
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
fun BottomBar(modifier: Modifier, onTabClick: (index: Int) -> Unit) {
    val tabAdd = ItemTabUIData("添加", icon = Icons.Default.Add)
    val tabList = ItemTabUIData("列表", icon = Icons.Default.List)
    Row(modifier = Modifier.wrapContentHeight()) {
        ItemType(
            modifier = Modifier
                .weight(1f)
                .clickable { onTabClick(0) },
            itemUiUIData = tabList
        )
        ItemType(
            modifier = Modifier
                .weight(1f)
                .clickable { onTabClick(1) },
            itemUiUIData = tabAdd
        )
    }
}

