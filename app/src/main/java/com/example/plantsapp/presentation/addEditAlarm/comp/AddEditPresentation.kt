package com.example.plantsapp.presentation.addEditAlarm.comp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.plantsapp.R
import com.example.plantsapp.presentation.addEditAlarm.AddEditEvent
import com.example.plantsapp.presentation.addEditAlarm.AddEditUiEvent
import com.example.plantsapp.presentation.addEditAlarm.AddEditViewModel
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditPresentation(
    navHostController: NavHostController,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val typeChoosePictureState = rememberUseCaseState()

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) viewModel.onEvent(AddEditEvent.ChoosePhoto(it))
        }
    )

    LaunchedEffect(key1 = true) {
        viewModel.sharedFlow.collectLatest { event ->
            when (event) {
                AddEditUiEvent.Save -> {
                    Toast.makeText(context, context.getString(R.string.SetUpNewAlarm), Toast.LENGTH_LONG).show()
                    navHostController.popBackStack()
                }
                is AddEditUiEvent.Toast -> {
                    Toast.makeText(context, context.getString(event.value), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    CoreDialog(
        state = typeChoosePictureState,
        selection = CoreSelection(
            positiveButton = SelectionButton(
                text = stringResource(id = R.string.Gallery),
                icon = IconSource(
                    imageVector = Icons.Filled.PhotoLibrary
                )
            ),
            onPositiveClick = {
                singlePhotoPicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            negativeButton = SelectionButton(
                text = stringResource(id = R.string.PickPhoto),
                icon = IconSource(
                    imageVector = Icons.Filled.PhotoCamera
                )
            ),
            onNegativeClick = {
                viewModel.onEvent(AddEditEvent.ShowCamera)
            }
        ),
        body = {
            Text(text = context.getString(R.string.HowDoYouWantPickImage))
        }
    )

    Scaffold(
        floatingActionButton = {
            if (state.showCamera.not()) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(AddEditEvent.Save)
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(10.dp, 10.dp),
                    modifier = Modifier
                        .size(60.dp)
                ) {
                    Image(
                        imageVector = Icons.Filled.Save,
                        contentDescription = "Add/Edit plant alarm",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
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
                    .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.MakeNewAlarm),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }

                    AnimatedContent(targetState = state.alarmModel.photo != null) {isPhoto ->
                        if (isPhoto) {
                            AsyncImage(
                                model = state.alarmModel.photo,
                                contentDescription = null,
                                imageLoader = ImageLoader(context),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(32.dp))
                                    .heightIn(max = 300.dp)
                                    .clickable {
                                        typeChoosePictureState.show()
                                    }
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Outlined.Yard,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clickable {
                                        typeChoosePictureState.show()
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.heightIn(16.dp))

                    TextField(
                        value = state.alarmModel.plantName,
                        onValueChange = {
                            viewModel.onEvent(AddEditEvent.EnteredPlantName(it))
                        },
                        singleLine = true,
                        label = {
                            Text(text = stringResource(id = R.string.PlantName))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusRequester.requestFocus()
                            }
                        ),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )

                    Spacer(modifier = Modifier.heightIn(16.dp))

                    TextField(
                        value = state.alarmModel.plantDescription,
                        onValueChange = {
                            viewModel.onEvent(AddEditEvent.EnteredPlantDescription(it))
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.PlantDescription)
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusRequester.requestFocus()
                            }
                        ),
                        modifier = Modifier
                            .heightIn(min = 150.dp)
                            .focusRequester(focusRequester)
                    )

                    Spacer(modifier = Modifier.heightIn(32.dp))

                    TextField(
                        value = "${state.alarmModel.repeating}",
                        onValueChange = {
                            val figure: Int? = it.toIntOrNull()
                            if (figure != null) viewModel.onEvent(AddEditEvent.ChooseAlarmTime(figure))
                        },
                        prefix = {
                            Text(text = stringResource(id = R.string.Every))
                        },
                        suffix = {
                            Text(text = stringResource(id = R.string.Day))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                        textStyle = TextStyle.Default.copy(
                            textAlign = TextAlign.Right
                        ),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                    )
                }
            }

            if(state.showCamera) {
                CameraView(
                    outputDirectory = state.outputDirectory,
                    executor = state.cameraExecutor,
                    onImageCapture = {
                        viewModel.onEvent(AddEditEvent.ChoosePhoto(it))
                        viewModel.onEvent(AddEditEvent.HideCamera)
                    },
                    onError = {
                        Log.e("Check", "View error:", it)
                    }
                )
            }
        }
    }
}