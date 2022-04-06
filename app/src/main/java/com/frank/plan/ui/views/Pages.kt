package com.frank.plan.ui.views

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frank.plan.data.PlanModel
import com.frank.plan.ui.theme.PlanTheme


const val TAG = "HistoryList"

const val History = "HistoryList"
const val ADD = "Add"

@Composable
fun HistoryList(changeTheme: () -> Unit) {
    val planModel: PlanModel = viewModel()
    val context = LocalContext.current
    val monthlyBill = planModel.getBillByMonth(context).collectAsState(initial = null).value
    Log.d(TAG, "HistoryList: --->")
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            modifier = Modifier
                .clickable {
                    changeTheme()
                }
                .background(PlanTheme.colors.titleBg)
                .fillMaxWidth(),
            text = "轻松记账",
            fontSize = 40.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center,
            color = PlanTheme.colors.titleTextColor
        )
        MonthlyInfo()
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .background(PlanTheme.colors.mainBg)
        ) {
            if (!monthlyBill.isNullOrEmpty()) {
                items(monthlyBill) {
                    DayBill(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView(changeTheme: () -> Unit) {
    var show by remember {
        mutableStateOf(true)
    }
    val navController = rememberNavController()
    navController.addOnDestinationChangedListener { _, navDestination: NavDestination, _ ->
        if (TextUtils.equals(navDestination.route, History)) {
            show = true
        }
    }

    Scaffold(floatingActionButton = {
        if (show) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(ADD)
                    show = false
                },
                modifier = Modifier
                    .wrapContentSize(),
                backgroundColor = PlanTheme.colors.mainBg
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = PlanTheme.colors.textColor
                )
            }
        }
    }) {
        NavHostZone(navController = navController, changeTheme = changeTheme)
    }
}


@ExperimentalFoundationApi
@Composable
fun NavHostZone(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    changeTheme: () -> Unit
) {
    NavHost(navController = navController, startDestination = History, modifier = modifier) {
        composable(History) {
            HistoryList(changeTheme)
        }

        composable(ADD) {
            AddView(navController)
        }
    }
}
