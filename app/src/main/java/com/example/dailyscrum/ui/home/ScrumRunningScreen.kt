package com.example.dailyscrum.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dailyscrum.R
import com.example.dailyscrum.data.ScrumItem
import com.example.dailyscrum.ui.theme.DailyScrumTheme
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.seconds

@Composable
fun ScrumRunningScreen(
    id: Int,
    navController: NavHostController,
    dailyScrumViewModel: DailyScrumViewModel
) {

    var meeting: ScrumItem? = null
    dailyScrumViewModel.scrumItemList.forEach { item -> if (item.id == id) meeting = item }

    val totalDuration = meeting?.duration
    val totalAttendee = meeting?.attendeeList?.size

    var currentSpeakerIndex by remember { mutableStateOf(0) }
    var meetingEnd by remember { mutableStateOf(false) }
    val individualTime = (totalDuration!! * 60) / totalAttendee!!

    val p = (1.0 / individualTime).toFloat()
    var progress by remember { mutableStateOf(p) }

    var remainingSeconds by remember { mutableStateOf(individualTime) }
    var elapsedSeconds by remember { mutableStateOf(0) }

    var currentSpeakerName by remember { mutableStateOf(meeting!!.attendeeList[currentSpeakerIndex].name) }
    LaunchedEffect(Unit) {
        while (progress <= 1.01 && !meetingEnd) {
            remainingSeconds--
            elapsedSeconds++
            delay(1.seconds)
            progress += p
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.padding(40.dp))
        CircularProgressBar(
            remainingSeconds,
            elapsedSeconds,
            progress,
            currentSpeakerName,
            meetingEnd
        )
        Spacer(modifier = Modifier.padding(80.dp))
        StepsProgressBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
            numberOfSteps = totalAttendee,
            currentStep = currentSpeakerIndex + 1
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            if (meetingEnd) {
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.caption
                )

                Icon(painter = painterResource(id = R.drawable.home_icon), contentDescription = null
                , modifier = Modifier.padding(end = 160.dp, top = 30.dp).clickable { navController.navigate("home") })

            }
            else {
                Text(
                    text = "Speaker ${currentSpeakerIndex + 1} of $totalAttendee",
                    style = MaterialTheme.typography.caption
                )
                Icon(painter = painterResource(id = R.drawable.next_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 10.dp, end = 10.dp)
                        .size(30.dp)
                        .clickable {
                            currentSpeakerIndex++
                            if (currentSpeakerIndex < totalAttendee) {
                                remainingSeconds = individualTime
                                elapsedSeconds = 0
                                progress = 0f
                                currentSpeakerName =
                                    meeting?.attendeeList?.get(currentSpeakerIndex)?.name.toString()
                            } else {
                                meetingEnd = true
                                progress = 0f
                            }
                        })
            }
        }

    }

}

@Composable
fun LinearProgressBar(progress: Float) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(15.dp)
                .clip(RoundedCornerShape(28.dp))
        ) {
            LinearProgressIndicator(
                backgroundColor = Color.Black,
                progress = progress,
                color = Color.Cyan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )
        }
    }
}

@Composable
fun CircularProgressBar(
    remainingSeconds: Int,
    elapsedSeconds: Int,
    progress: Float,
    speakerName: String,
    meetingEnd: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.padding(bottom = 10.dp), contentAlignment = Alignment.Center) {
            val stroke = with(LocalDensity.current) {
                Stroke(width = 20.dp.toPx(), cap = StrokeCap.Butt)
            }

            Canvas(modifier = Modifier.size(300.dp)) {
                val diameterOffset = stroke.width / 2
                drawCircle(
                    radius = size.minDimension / 2.0f - diameterOffset,
                    color = White, style = stroke
                )
            }
            CircularProgressIndicator(
                progress = if (!meetingEnd)progress else 0F,
                modifier = Modifier.size(300.dp),
                strokeWidth = 20.dp,
                color = Color.Black,
            )
            if (!meetingEnd)
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = speakerName, style = MaterialTheme.typography.h6)
                    Text(text = "is speaking", style = MaterialTheme.typography.body2)
                    Icon(
                        painter = painterResource(id = R.drawable.mic_icon),
                        contentDescription = null, modifier = Modifier
                            .padding(top = 10.dp)
                            .size(30.dp)
                    )
                }
            else
                Text(
                    text = "Thanks Everyone \n have a nice day",
                    style = MaterialTheme.typography.body1
                )
        }

        if (!meetingEnd)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Seconds Elapsed",
                    modifier = Modifier.padding(start = 10.dp),
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = "Seconds remaining",
                    modifier = Modifier.padding(end = 10.dp),
                    style = MaterialTheme.typography.caption
                )
            }

        if (!meetingEnd)
            Row(
                horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = elapsedSeconds.toString(),
                    modifier = Modifier.padding(end = 50.dp),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = remainingSeconds.toString(),
                    modifier = Modifier.padding(start = 50.dp),
                    style = MaterialTheme.typography.body2
                )
            }


    }

}

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 0..numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                isCompete = step < currentStep,
                isCurrent = step == currentStep
            )
        }
    }
}

@Composable
fun Step(modifier: Modifier = Modifier, isCompete: Boolean, isCurrent: Boolean) {
    val color = if (isCompete || isCurrent) Color.Black else Color.White
    val innerCircleColor = if (isCompete) Color.Black else Color.White

    Box(modifier = modifier) {

        //Line
        Divider(
            modifier = Modifier.align(Alignment.CenterStart),
            color = color,
            thickness = 8.dp
        )

        //Circle
        Canvas(modifier = Modifier
            .size(15.dp)
            .align(Alignment.CenterEnd)
            .border(
                shape = CircleShape,
                width = 2.dp,
                color = color
            ),
            onDraw = {
                drawCircle(color = innerCircleColor)
            }
        )
    }
}

//@Preview
//@Composable
//fun LinearProgressBarPreview() {
//    DailyScrumTheme {
//        Surface {
//            CircularProgressBar(60)
//        }
//    }
//}

//
@Preview
@Composable
fun LinearProgressBarPreview() {
    DailyScrumTheme {
        Surface {
            CircularProgressBar(40, 40, 0.75f, "Samir", false)
        }
    }
}

//@Preview
//@Composable
//fun StepsProgressBarPreview() {
//    val currentStep = remember { mutableStateOf(1) }
//    StepsProgressBar(modifier = Modifier.fillMaxWidth(), numberOfSteps = 2, currentStep = currentStep.value)
//}