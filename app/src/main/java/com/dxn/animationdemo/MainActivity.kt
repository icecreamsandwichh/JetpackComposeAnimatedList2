package com.dxn.animationdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dxn.animationdemo.ui.theme.AnimationDemoTheme

class MainActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationDemoTheme {
                val navController = rememberNavController()
                val mainState = remember { MutableTransitionState(true) }
                val firstState = remember { MutableTransitionState(false) }
                val secondState = remember { MutableTransitionState(false) }

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NavHost(
                        navController = navController,
                        startDestination = "Main"
                    ) {
                        composable("Main") {
                            val visibleState = remember { MutableTransitionState(false) }
                            visibleState.targetState = true
                            AnimatedVisibility(
                                visibleState = visibleState,
                                enter = expandHorizontally(
                                    expandFrom = Alignment.End,
                                    animationSpec = tween(
                                        durationMillis = 500
                                    )
                                ) + expandVertically(
                                    expandFrom = Alignment.CenterVertically,
                                    animationSpec = tween(
                                        durationMillis = 500
                                    )
                                ),
                                exit = slideOutHorizontally(
                                    targetOffsetX = { 1000 },
                                    animationSpec = tween(
                                        durationMillis = 500
                                    )
                                ),
                            ) {
                                Main {
                                    navController.navigate("dest1")
                                }
                            }

                        }
                        composable("dest1") {
                            FirstScreen(navController = navController)
                        }
                        composable("dest2") {
                            SecondScreen(navController)
                        }
                    }
                }
            }
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun ExpandCard(onClick: () -> Unit) {
    val animationDuration = 500
    val size = remember { mutableStateOf(100.dp) }
    val elev = remember { mutableStateOf(1.dp) }
    val padding = remember { mutableStateOf(16.dp) }
    val isExpanded = remember { mutableStateOf(false) }
    val visibleState = remember { MutableTransitionState(false) }

    val animateHeight = animateDpAsState(
        targetValue = size.value,
        animationSpec = tween(
            durationMillis = animationDuration
        )
    )
    val animateElevation = animateDpAsState(
        targetValue = elev.value,
        animationSpec = tween(
            durationMillis = animationDuration
        )
    )
    val animatePadding = animateDpAsState(
        targetValue = padding.value,
        animationSpec = tween(
            durationMillis = animationDuration
        )
    )

    Card(
        elevation = animateElevation.value,
        modifier = Modifier
            .fillMaxWidth()
            .height(animateHeight.value)
            .padding(animatePadding.value)
            .clickable {
                Log.d("DEBUG", "ExpandCard: $isExpanded")
                if (isExpanded.value) {
                    size.value = 100.dp
                    elev.value = 1.dp
                    padding.value = 16.dp
                    isExpanded.value = false
                    visibleState.targetState = false
                } else {
                    size.value = 200.dp
                    elev.value = 5.dp
                    padding.value = 8.dp
                    isExpanded.value = true
                    visibleState.targetState = true
                }
                Log.d("DEBUG", "ExpandCard: $isExpanded")

            }) {
        Column(
            modifier = Modifier
                .background(Color(0xFFB8E9FF))
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "This is a visible text")
            AnimatedVisibility(
                visibleState = visibleState,
                enter = slideInVertically(
                    initialOffsetY = { 1000 },
                    animationSpec = tween(
                        durationMillis = animationDuration
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { 1000 },
                    animationSpec = tween(
                        durationMillis = animationDuration
                    )
                ),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { onClick() }) {
                        Text(text = "Invisible Button")
                    }
                    Text(text = "This is an invisible text")
                }
            }
        }

    }
}

@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
fun Main(onClick: () -> Unit) {
    LazyColumn() {
        items(5) {
            ExpandCard(onClick)
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun FirstScreen(navController: NavController) {
    val firstState = MutableTransitionState(true)
    EnterAnimation(content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Magenta)
        ) {
            Button(onClick = {
                firstState.targetState = false
                navController.navigate("Main")
            }) {

            }
        }
    }, state = firstState)
}

@Composable
fun SecondScreen(navController: NavController) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Button(onClick = { navController.navigate("dest1") }) {
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun EnterAnimation(content: @Composable () -> Unit, state: MutableTransitionState<Boolean>) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = true
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(
            initialOffsetX = { 1000 },
            animationSpec = tween(
                durationMillis = 500
            )
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { 1000 },
            animationSpec = tween(
                durationMillis = 500
            )
        ),
    ) {
        content()
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun Prev() {
    ExpandCard() {

    }
}

