package com.example.plantsapp.presentation.addEditAlarm.comp

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.plantsapp.R
import com.example.plantsapp.presentation.addEditAlarm.AddEditEvent
import com.maxkeppeker.sheets.core.CoreDialog
import com.maxkeppeker.sheets.core.models.CoreSelection
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPresenter(
    photo: Uri?,
    context: Context,
    imagePicker: (Uri) -> Unit
) {
    val typeChoosePictureState = rememberUseCaseState()

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            if (it != null) imagePicker(it)
        }
    )

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

            }
        ),
        body = {
            Text(text = context.getString(R.string.HowDoYouWantPickImage))
        }
    )

    AnimatedContent(targetState = photo != null) {isPhoto ->
        if (isPhoto) {
            AsyncImage(
                model = photo,
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

}