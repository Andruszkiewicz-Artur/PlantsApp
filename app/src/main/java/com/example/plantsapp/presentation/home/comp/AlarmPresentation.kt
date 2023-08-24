package com.example.plantsapp.presentation.home.comp

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import com.example.plantsapp.R
import com.example.plantsapp.domain.model.PlantAlarmModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmPresentation(
    alarmModel: PlantAlarmModel,
    onClickAlarm: () -> Unit,
    onClickSwitch: () -> Unit,
    onClickDelete: () -> Unit,
    isDivider: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pattern = "dd MMM yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    val lastWateringDate = alarmModel.basicDate.format(formatter)
    val nextWateringDate = alarmModel.nextDate.format(formatter)

    Card(
        onClick = {
            onClickAlarm()
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    AnimatedVisibility(visible = alarmModel.photo != null) {
                        AsyncImage(
                            model = alarmModel.photo,
                            contentDescription = null,
                            imageLoader = ImageLoader(context),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .height(90.dp)
                        )
                    }

                    if (alarmModel.photo != null) Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = alarmModel.plantName,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            onClickDelete()
                        }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Row {

                        Text(
                            text = stringResource(id = R.string.Repeating) + ": ",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${alarmModel.repeating} "
                        )

                        Text(
                            text = stringResource(id = if(alarmModel.repeating > 1) R.string.Days else R.string.Day)
                        )

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {

                        Text(
                            text = stringResource(id = R.string.LastWatering) + ": ",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = lastWateringDate
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row {

                        Text(
                            text = stringResource(id = R.string.NextWatering) + ": ",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = nextWateringDate
                        )
                    }
                }

                Switch(
                    checked = alarmModel.isActive,
                    onCheckedChange = {
                        onClickSwitch()
                    }
                )
            }
        }
    }

}