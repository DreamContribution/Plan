package com.frank.plan.ui.views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.frank.plan.data.*
import com.frank.plan.ui.theme.PlanTheme

/**
 * 账单类型
 */
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
                    tint = if (viewModel.targetAddType == index) PlanTheme.colors.iconTintSelected else PlanTheme.colors.iconTint
                )
            } else if (itemUiUIData.iconResource != null) {
                Icon(
                    painter = itemUiUIData.iconResource!!,
                    modifier = iconModifier,
                    contentDescription = contentDesc,
                    tint = if (viewModel.targetAddType == index) PlanTheme.colors.iconTintSelected else PlanTheme.colors.iconTint
                )
            }
        }

        Text(text = itemUiUIData.name, fontSize = 14.sp)
    }
}

/**
 * 账单类型列表
 */
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

/**
 * 添加账单标题
 */
@ExperimentalFoundationApi
@Composable
fun AddTitleView(navController: NavHostController) {
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
fun InfoInput(navController: NavHostController) {
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

/**
 * 账单信息输入界面
 */
@Composable
fun InfoInputItem(index: Int, data: String, type: Int, onClick: (String, Int) -> Unit) {
    val speLineColor = PlanTheme.colors.iconTintSelected
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
                    color = speLineColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = if (isFirstRow) 2.dp.toPx() else {
                        1.dp.toPx()
                    }
                )
                if (!isLastColumn) {
                    drawLine(
                        color = speLineColor,
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