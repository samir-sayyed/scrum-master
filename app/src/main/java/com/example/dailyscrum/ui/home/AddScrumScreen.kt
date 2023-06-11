package com.example.dailyscrum.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import com.example.dailyscrum.data.Attendee
import com.example.dailyscrum.data.ScrumItem


@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun AddScrum(
    id: Int, edit: Boolean, navController: NavController,
    dailyScrumViewModel: DailyScrumViewModel
) {
    val _attendeeList = mutableStateListOf<Attendee>()
    var sliderPosition by remember { mutableStateOf(0f) }
    var scrumTitle by remember { mutableStateOf("") }
    var attendeeList by remember { mutableStateOf(_attendeeList) }
    var mSelectedText by remember { mutableStateOf("SeaFoam") }
    val mContext = LocalContext.current
    var meeting: ScrumItem? = null
    if (edit) {
        dailyScrumViewModel.scrumItemList.forEach { item ->
            if (item.id == id) meeting = item
        }
    }

    if (edit) {
        attendeeList = meeting?.attendeeList as SnapshotStateList<Attendee>
        mSelectedText = meeting?.theme!!
        scrumTitle = meeting?.title!!
        sliderPosition = meeting?.duration!!.toFloat()
    }

    Scaffold(
        topBar = {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ADD MEETING INFO", style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(226, 218, 227))
                        .padding(start = 95.dp, top = 20.dp)
                )
            }
        },
        content = { paddingValues ->
            Surface(color = Color(226, 218, 227), modifier = Modifier.padding(paddingValues)) {
                Column(modifier = Modifier.padding(top = 20.dp)) {
                    Text(
                        text = "Meeting Info",
                        modifier = Modifier.padding(start = 20.dp, bottom = 5.dp)
                    )
                    ScrumInfoCard(sliderPosition = sliderPosition,
                        scrumTitle = scrumTitle,
                        onValueChange = { scrumTitle = it },
                        onChangeValue2 = { sliderPosition = it },
                        mSelectedText,
                        onThemeSelected = { mSelectedText = it })
                    AttendeesSection(attendeeList, onAdd = { num, name ->
                        attendeeList.add(Attendee(num, name))
                    })
                }
            }
        },
        bottomBar = {
            BottomBar(
                onClose = { navController.navigate("home") },
                onSave = {
                    if (scrumTitle.isNotEmpty() && sliderPosition.toInt() > 0 && attendeeList.size > 0) {
                        if (!edit) {
                            dailyScrumViewModel.addScrumItem(
                                ScrumItem(
                                    dailyScrumViewModel.scrumItemList.size, scrumTitle,
                                    sliderPosition.toInt(),
                                    attendeeList,
                                    mSelectedText
                                )
                            )
                        } else
                            dailyScrumViewModel.editScrumItem(
                                id,ScrumItem(
                                    id, scrumTitle,
                                    sliderPosition.toInt(),
                                    attendeeList,
                                    mSelectedText
                                )
                            )

                            navController.navigate("home")
                    } else
                        mToast(mContext, "please complete all meeting info")
                }
            )
        }
    )
}

@Composable
fun BottomBar(
    onSave: () -> Unit,
    onClose: () -> Unit
) {
    Row(modifier = Modifier.background(Color(226, 218, 227))) {
        Button(
            onClick = { onClose() }, modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp, end = 10.dp)
        ) {
            Text(text = "Close")
        }
        Button(
            onClick = { onSave() }, modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp, end = 5.dp)
        ) {
            Text(text = "Add")
        }
    }
}

@Composable
fun ScrumInfoCard(
    sliderPosition: Float,
    scrumTitle: String,
    onValueChange: (text: String) -> Unit,
    onChangeValue2: (pos: Float) -> Unit,
    mSelectedText: String,
    onThemeSelected: (theme: String) -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {

        Column(
            Modifier
                .padding(all = 10.dp)
        ) {
            TextField(
                value = scrumTitle,
                onValueChange = { onValueChange(it) },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.Gray,
                    disabledIndicatorColor = Color.Transparent
                ),
                placeholder = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Slider(
                    value = sliderPosition,
                    onValueChange = { onChangeValue2(it) },
                    valueRange = 0f..60f,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                    steps = 10,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colors.secondary,
                        activeTrackColor = MaterialTheme.colors.secondary
                    ),
                    modifier = Modifier.weight(3f)
                )
                Text(
                    text = "${sliderPosition.toInt()} Minutes", modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Theme",
                    modifier = Modifier
                        .weight(1f),
                    fontWeight = FontWeight(800)
                )
                DropdownMenu(mSelectedText) {
                    onThemeSelected(it)
                }
            }
        }
    }
}

@Composable
fun DropdownMenu(
    mSelectedText: String,
    modifier: Modifier = Modifier,
    onThemeSelected: (theme: String) -> Unit
) {
    var mExpanded by remember { mutableStateOf(false) }
    val mCities =
        listOf("SeaFoam", "Indigo", "Lavender", "Magenta", "navy", "Poppy", "Purple", "Sky", "Tan")

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(
        modifier = Modifier
            .padding(top = 10.dp)
    ) {
        Row {
            Text(text = mSelectedText,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    }
                    .width(120.dp))
            Icon(icon, "contentDescription",
                Modifier.clickable { mExpanded = !mExpanded })
        }
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            mCities.forEach { label ->
                DropdownMenuItem(onClick = {
                    onThemeSelected(label)
                    mExpanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun Attendees(
    modifier: Modifier = Modifier,
    name: String,
    attendeeList: SnapshotStateList<Attendee>,
    id: Int
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .border(1.dp, color = Color(226, 218, 227))
    ) {
        Text(
            text = name, modifier = Modifier
                .padding(start = 20.dp, top = 7.dp, bottom = 7.dp)
        )
        Icon(Icons.Filled.Close, "contentDescription",
            modifier = Modifier
                .clickable {
                    var attendee: Attendee? = null
                    attendeeList.forEach { item ->
                        if (item.id == id) attendee = item
                    }
                    attendeeList.remove(attendee)
                }
                .padding(end = 10.dp))
    }
}

@Composable
fun AttendeesSection(
    attendeeList: SnapshotStateList<Attendee>,
    onAdd: (num: Int, attendee: String) -> Unit
) {
    var count by remember { mutableStateOf(0) }
    val mContext = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        var attendeeName by remember { mutableStateOf("") }
        Text(
            text = "Attendees",
            modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 5.dp)
        )
        TextField(
            value = attendeeName,
            onValueChange = { attendeeName = it },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = "Enter attendee name") },
            trailingIcon = {
                Icon(Icons.Filled.Add, "contentDescription",
                    modifier = Modifier
                        .clickable {
                            if (attendeeName.isNotEmpty()) {
                                onAdd(count, attendeeName)
                                count++
                                attendeeName = ""
                            } else
                                mToast(mContext, "please enter attendee name")
                        }
                        .padding(bottom = 5.dp, start = 5.dp, end = 5.dp))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
        )
        Surface(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 2.dp)
        ) {
            LazyColumn(
                modifier = Modifier
            ) {
                items(
                    items = attendeeList,
                    key = { attendee -> attendee.id }
                ) { attendee ->
                    Attendees(name = attendee.name, attendeeList = attendeeList, id = attendee.id)
                }
            }
        }
    }
}

private fun mToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

//@Preview
//@Composable
//fun AttendeesPreview() {
//    DailyScrumTheme {
//        Surface(modifier = Modifier.fillMaxWidth()) {
//            AttendeesSection(attenddeeList())
//        }
//    }
//}


//@Preview
//@Composable
//fun ScrumInfoPreview() {
//    DailyScrumTheme {
//        Surface(modifier = Modifier.fillMaxWidth()) {
//            AddScrum()
//        }
//    }
//}
