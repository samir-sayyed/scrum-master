package com.example.dailyscrum.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dailyscrum.R
import com.example.dailyscrum.data.ScrumItem


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dailyScrumViewModel: DailyScrumViewModel
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_scrum/0/${false}") },
            ) {
                Icon(Icons.Filled.Add, "")
            }
        },
        content = { padding ->
            Surface(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(), color = Color(226, 218, 227)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "DAILY SCRUMS",
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(start = 80.dp, top = 10.dp, bottom = 10.dp)
                            .fillMaxWidth()
                    )
                    if (!dailyScrumViewModel.scrumItemList.isNullOrEmpty())
                        ScrumsDetailsList(list = dailyScrumViewModel.scrumItemList) {
                            navController.navigate("scrum_detail/${it.toString()}")
                        }
                    else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 100.dp)) {
                            Icon(painter = painterResource(id = R.drawable.empty_icon), null)
                            Text(text = "No Meetings")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ScrumDetailCard(
    modifier: Modifier = Modifier,
    id: Int,
    title: String, attendees: Int, duration: Int,
    onClick: (id: Int) -> Unit,
    theme: String
) {
    Surface(
        color =when(theme){
            "SeaFoam" -> Color(147, 233, 190)
            "Indigo" -> Color(	75, 0, 130)
            "Lavender" -> Color(230, 230, 250)
            "Magenta" -> Color(	255, 0, 255)
            "navy" -> Color(0, 0, 128)
            "Poppy" -> Color(227, 83, 53)
            "Purple" -> Color(221,160,221)
            "Sky" -> Color(147, 233, 190)
            else -> {
                Color(187, 222, 111)
            }
        },
        modifier = modifier
            .padding(10.dp)
            .clickable { onClick(id) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column {
            Row(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    style = MaterialTheme.typography.h6
                )
                Image(
                    painter = painterResource(id = R.drawable.arrow_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp)
                )

            }
            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                Row(Modifier.weight(1f)) {
                    Image(
                        painter = painterResource(id = R.drawable.attendees_icon),
                        modifier = Modifier.padding(start = 10.dp),
                        contentDescription = null
                    )
                    Text(text = "$attendees", modifier = Modifier.padding(start = 10.dp))
                }
                Row {
                    Text(text = "$duration", modifier = Modifier.padding(end = 10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.duration_icon),
                        contentDescription = null, modifier = Modifier.padding(end = 30.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScrumsDetailsList(list: List<ScrumItem>, onClick: (id: Int) -> Unit) {
    LazyColumn {
        items(list) { item ->
            Log.d("TAG", "ScrumsDetailsList: $item")
            ScrumDetailCard(
                title = item.title,
                attendees = item.attendeeList.size,
                duration = item.duration,
                onClick = { onClick(item.id) },
                id = item.id,
                theme = item.theme
            )
        }
    }
}

//@Preview
//@Composable
//fun scrumListPreview(){
//    DailyScrumTheme {
//        Surface {
//            ScrumsDetailsList(getWellnessTasks() as ArrayList<ScrumItem>)
//        }
//    }
//}


//@Preview
//@Composable
//fun ScrumDetailCard() {
//    DailyScrumTheme {
//        Surface {
//            ScrumDetailCard(
//                title = "Web Design",
//                attendees = 11,
//                duration = 20,
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//    }
//}

//@Preview
//@Composable
//fun ScrumDetailCard() {
//    DailyScrumTheme {
//        Surface {
//            HomeScreen()
//        }
//    }
//}
