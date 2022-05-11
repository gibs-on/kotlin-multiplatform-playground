package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.insets.ui.TopAppBar
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawer
import com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components.LandingScreenDrawerItemType
import com.mutualmobile.harvestKmp.android.ui.screens.reportsScreen.ReportsScreen
import com.mutualmobile.harvestKmp.android.ui.screens.timeScreen.TimeScreen
import com.mutualmobile.harvestKmp.android.ui.theme.DrawerBgColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LandingScreen() {
    val scaffoldDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackBarHostState = remember { SnackbarHostState() }
    val scaffoldState = ScaffoldState(
        drawerState = scaffoldDrawerState,
        snackbarHostState = snackBarHostState
    )
    val coroutineScope = rememberCoroutineScope()
    var currentDrawerScreen by remember { mutableStateOf(LandingScreenDrawerItemType.Time) }
    var currentWeekOffset by remember { mutableStateOf(0) }
    var currentDayOffset by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Time")
                        Text(
                            text = "WeekOffset$currentWeekOffset\nDaysOffset$currentDayOffset",
                            style = MaterialTheme.typography.body2.copy(
                                color = MaterialTheme.colors.surface.copy(alpha = 0.5f)
                            )
                        )
                    }
                },
                contentPadding = WindowInsets.Companion.statusBars.asPaddingValues(),
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colors.surface
                        )
                    }
                }
            )
        },
        drawerShape = landingScreenDrawerShape(),
        drawerContent = {
            LandingScreenDrawer(
                currentDrawerScreen = currentDrawerScreen,
                closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                onScreenChanged = { newScreen: LandingScreenDrawerItemType ->
                    currentDrawerScreen = newScreen
                }
            )
        },
        drawerBackgroundColor = DrawerBgColor,
        scaffoldState = scaffoldState,
    ) { bodyPadding ->
        AnimatedContent(
            targetState = currentDrawerScreen,
            transitionSpec = { fadeIn() + fadeIn() with fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(bodyPadding)
        ) { drawerScreenState ->
            when (drawerScreenState) {
                LandingScreenDrawerItemType.Time -> TimeScreen(
                    onWeekScrolled = { weekOffset ->
                        currentWeekOffset = weekOffset
                    },
                    onDayScrolled = { dayOffset ->
                        currentDayOffset = dayOffset
                    },
                )
                LandingScreenDrawerItemType.Reports -> ReportsScreen()
            }
        }
    }
}

private fun landingScreenDrawerShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(Rect(0f, 0f, size.width.times(0.75f), size.height))
    }
}