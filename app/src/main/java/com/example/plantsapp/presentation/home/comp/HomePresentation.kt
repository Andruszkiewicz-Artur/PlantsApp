package com.example.plantsapp.presentation.home.comp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.plantsapp.R
import com.example.plantsapp.core.unit.navigation.plants.PlantsScreen
import com.example.plantsapp.presentation.home.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePresentation(
    navHostController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(PlantsScreen.AddEdit.route)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(10.dp, 10.dp),
                modifier = Modifier
                    .size(60.dp)
            ) {
                Image(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add/Edit plant alarm",
                    modifier = Modifier
                        .size(40.dp)
                )
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            ) {

                item {
                    Text(
                        text = stringResource(id = R.string.PlantsAlarms),
                        style = MaterialTheme.typography.displayLarge
                    )

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

            }
        }
    }
}