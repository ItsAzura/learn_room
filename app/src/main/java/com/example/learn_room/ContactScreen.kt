package com.example.learn_room

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ContactScreen(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit
){
    // Scaffold: Cấu trúc giao diện chính, bao gồm thanh tiêu đề, thanh công cụ, nút thêm liên lạc
    Scaffold(
        //nút hành động thực hiện sự kiện add contact khi nhấn thì thực hiện mở Dialog
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ContactEvent.ShowDialog)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Contact"
                )
            }
        },
    ) {_ ->
        if(state.isAddingContact){ //Nếu người dùng thực hiện thêm contact
            AddContactDialog(state = state, onEvent = onEvent) //mở ra dialog hiện ra để người dùng thêm contact mới với tham số là state và onEvent
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            item{
                Row(//sắp xếp item theo chiều ngang
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()), //cuộn theo chiều ngang để có thể hiện thị nhiều hơn
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SortType.values().forEach {//duyệt qua các giá trị trong enum class "SortType"
                        Row(
                            modifier = Modifier.clickable {
                                onEvent(ContactEvent.SortContacts(it))//gọi sự kiện ContactEvent.SortContacts(it) it là giá trị user chọn
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RadioButton(
                                selected = state.sortType == it, //khi được chọn thì sắp xếp theo kiểu sắp xếp tượng ứng
                                onClick = {
                                    onEvent(ContactEvent.SortContacts(it)) //gọi sự kiện ContactEvent.SortContacts(it) it là giá trị user chọn
                                }
                            )
                            Text(text = it.name)//tên của kiểu sắp xếp đó
                        }
                    }
                }
            }
            //hiện 1 list các contact
            items(state.contacts){contact ->
                Row( //hiện thị theo row và chiếm full chiều rộng của màn hình
                    modifier = Modifier.fillMaxWidth()
                ) {//mỗi item
                    Column( //hiện thị tên, họ và số điện thoại theo chiều column và chiếm toàn bộ màn hình
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "${contact.firstName} ${contact.lastName}",
                            fontSize = 20.sp
                        )
                        Text(
                            text = contact.phoneNum,
                            fontSize = 12.sp
                        )
                    }
                    IconButton(onClick = {
                        onEvent(ContactEvent.DeleteContact(contact)) //khi bấm vào nút thì gọi sự kiện ContactEvent.DeleteContact(contact) contact ở đây chính là chính item đang được chọn
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Contact" )
                    }
                }
            }
        }
    }
}