package com.example.dailyscrum.ui.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailyscrum.R
import com.example.dailyscrum.data.Attendee
import com.example.dailyscrum.data.ScrumItem

@Composable
fun ScrumDetailScreen(
    id: Int, navController: NavController,
    dailyScrumViewModel: DailyScrumViewModel
) {
        var meeting : ScrumItem? = null
        dailyScrumViewModel.scrumItemList.forEach{
        item -> if (item.id == id) meeting = item
    }
    Surface (color = Color(226, 218, 227), modifier = Modifier.fillMaxSize()){
        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${meeting?.title}",
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier.padding(start = 10.dp, top = 5.dp))
                Text(text = "Edit", color = Color.Blue,
                    fontWeight = FontWeight(800),
                    modifier = Modifier.
                    padding(end = 20.dp, top = 5.dp)
                        .clickable { navController.navigate("add_scrum/${id.toString()}/${true.toString()}") })
            }
            MeetingInfo(meeting?.duration, meeting?.theme){
                navController.navigate("running_scrum/${id.toString()}")
            }
            meeting?.attendeeList?.let { AttendeesSectionDetail(attendeeList = it) }
        }
    }
}

@Composable
fun MeetingInfo(duration: Int?, theme: String?, startMeeting : () -> Unit) {
    Column( modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Text(
            text = "MEETING INFO",
            modifier = Modifier.padding(start = 10.dp, bottom = 5.dp, top = 10.dp),
            color = Color.Black
        )
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.start_meeting_icon),
                        contentDescription = "start meeting",
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Start Meeting", modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                            .clickable { startMeeting() },
                        color = Color.Blue
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_icon),
                        contentDescription = null,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.duration_icon),
                        contentDescription = "Length",
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Length", modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    )
                    Text(text = "$duration Minutes", modifier = Modifier.padding(10.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.theme_icon),
                        contentDescription = "start meeting",
                        modifier = Modifier.padding(10.dp)
                    )
                    Text(
                        text = "Theme", modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                    )
                    if (theme != null) {
                        Text(text = theme, modifier = Modifier.padding(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AttendeesDetail(name: String) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, color = Color.LightGray)
    ) {
        Text(
            text = name, modifier = Modifier
                .weight(2f)
                .padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
        )
    }
}

@Composable
fun AttendeesSectionDetail(attendeeList: List<Attendee>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Attendees",
            modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 5.dp)
        )
        Surface(
            color = Color.White,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            LazyColumn(
                modifier = Modifier
            ) {
                items(
                    items = attendeeList,
                    key = { attendee -> attendee.id }
                ) { attendee ->
                    AttendeesDetail(attendee.name)
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun MeetingInfoPreview() {
//    DailyScrumTheme {
//        Surface {
//            MeetingInfo(meeting?.duration, meeting?.theme)
//        }
//    }
//}