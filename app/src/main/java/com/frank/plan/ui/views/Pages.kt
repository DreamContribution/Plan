package com.frank.plan.ui.views

import android.text.TextUtils
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frank.plan.ui.theme.ComposePlanTheme


const val TAG = "HistoryList"

const val History = "HistoryList"
const val ADD = "Add"

@Preview(showBackground = true)
@Composable
fun HistoryList() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .background(ComposePlanTheme.colors.primaryVariant)
                .fillMaxWidth(),
            text = "轻松记账",
            fontSize = 40.sp,
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center
        )
        MonthlyInfo()
        LazyColumn {
            var count = 1
            items(5) {
                DayBill(count)
                count++
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainView() {
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
                backgroundColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }) {
        NavHostZone(navController = navController)
    }
}


@ExperimentalFoundationApi
@Composable
fun NavHostZone(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = History, modifier = modifier) {
        composable(History) {
            HistoryList()
        }

        composable(ADD) {
            AddView()
        }
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
