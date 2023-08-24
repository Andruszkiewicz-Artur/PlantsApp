package com.example.plantsapp.presentation.home.comp

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.plantsapp.R
import com.example.plantsapp.core.unit.navigation.plants.PlantsScreen
import com.example.plantsapp.presentation.home.HomeEvent
import com.example.plantsapp.presentation.home.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePresentation(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState().value
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(PlantsScreen.AddEdit.route)
                }
            ) {
                Image(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add/Edit plant alarm",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.PlantsAlarms)
                    )
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { value ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(value)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {

                item {
                    if(state.plantsAlarm.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.NoneAlarmsYet),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                            )
                        }
                    }
                }

                items(state.plantsAlarm) {alarm ->
                    AlarmPresentation(
                        alarmModel = alarm,
                        onClickAlarm = {
                            if (alarm.id != null) navHostController.navigate(PlantsScreen.AddEdit.sendPlantAlarmId(alarm.id))
                        },
                        onClickSwitch = {
                            viewModel.onEvent(HomeEvent.ChangeActiveAlarm(alarm))
                        },
                        onClickDelete = {
                            viewModel.onEvent(HomeEvent.DeleteAlarm(alarm))
                            scope.launch {
                                val result = snackBarHostState.showSnackbar(
                                    message = context.getString(R.string.RecoverAlarm),
                                    actionLabel = context.getString(R.string.Retry),
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short
                                )

                                when (result) {
                                    SnackbarResult.Dismissed -> {  }
                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.onEvent(HomeEvent.AddAlarm(alarm))
                                    }
                                }
                            }
                        },
                        isDivider = state.plantsAlarm.last() != alarm,
                        modifier = Modifier
                            .animateItemPlacement(
                                animationSpec = tween(
                                    1000
                                )
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}