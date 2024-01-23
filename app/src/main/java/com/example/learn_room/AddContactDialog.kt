package com.example.learn_room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

//khi gọi thì sẽ mở dialog để người dùng thêm contact mới
@Composable
fun AddContactDialog(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { //khi người dùng đóng dialog
            onEvent(ContactEvent.HideDialog)
        },
        title = { Text(text = "Add contact") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField( //dùng để cho người dùng nhập thông tin contact mới
                    value = state.firstName, //xác định giá trị hiện tại của textfield là rỗng
                    onValueChange = {//khi người dùng nhập giá trị mới vào
                        onEvent(ContactEvent.SetFirstName(it)) //gọi hàm ContactEvent.SetFirstName(it) với it là giá trị mới
                    },
                    placeholder = {
                        Text(text = "First name")
                    }
                )
                TextField(
                    value = state.lastName,
                    onValueChange = {
                        onEvent(ContactEvent.SetLastName(it))
                    },
                    placeholder = {
                        Text(text = "Last name")
                    }
                )
                TextField(
                    value = state.age,
                    onValueChange = {
                        onEvent(ContactEvent.SetAge(it))
                    },
                    placeholder = {
                        Text(text = "Age")
                    }
                )
                TextField(
                    value = state.phoneNum,
                    onValueChange = {
                        onEvent(ContactEvent.SetPhoneNum(it))
                    },
                    placeholder = {
                        Text(text = "Phone number")
                    }
                )
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(onClick = {
                    onEvent(ContactEvent.Savecontact)//khi bấm vào nút thì thực hiện sự kiện ContactEvent.Savecontact
                }) {
                    Text(text = "Save")
                }
            }
        }
    )
}