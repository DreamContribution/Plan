package com.frank.plan.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frank.plan.R


@Preview(name = "item-day", showBackground = true)
@Composable
fun DayInfo() {
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
        Divider(color = Color.Red, thickness = 0.5.dp)
    }
}

@Preview(name = "Item-Bill", showBackground = true)
@Composable
fun ItemBill() {
    Column {
        Row(
            modifier = Modifier
                .padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 10.dp)
                .fillMaxWidth(),
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
        Divider(color = Color.Red, thickness = 0.5.dp, modifier = Modifier.padding(start = 54.dp))
    }
}

@Preview(name = "ItemType", showBackground = true)
@Composable
fun ItemType() {
    Column(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier.size(40.dp),
            contentDescription = "test type"
        )
        Text(text = "餐饮")
    }
}


@ExperimentalFoundationApi
@Composable
fun GridTest() {
    LazyVerticalGrid(cells = GridCells.Fixed(4)) {
        items(2000) {
            ItemType()
        }
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
fun RecordTotal() {
    Column {
        Text(text = "支出", fontSize = 15.sp, fontWeight = FontWeight.W100)
        Text(text = "100.00", fontSize = 22.sp, fontWeight = FontWeight.W200)
    }
}


@Preview(name = "TotalRecord", showBackground = true)
@Composable
fun TotalRecord() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MonthSelectButton()
        Spacer(modifier = Modifier.width(30.dp))
        RecordTotal()
    }
}
