package com.example.learn_room

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.learn_room.ui.theme.Learn_RoomTheme

class MainActivity : ComponentActivity() {
    //tạo db
    private val db by lazy {
        Room.databaseBuilder( //dùng room tạo db
            applicationContext, //Nó được sử dụng để lấy truy cập đến Context của ứng dụng.
            ContactDatabase::class.java, //Đại diện cho lớp RoomDatabase được định nghĩa trong ứng dụng.
            "contacts.db" //Tên csdl
        ).build() //tạo db
    }
    //tạo đối tượng viewModel của lớp ContactViewModel
    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = { //tham số khuyến khích cho việc cung cấp một factory để tạo đối tượng ViewModel.
            object :ViewModelProvider.Factory{ //một giao diện được sử dụng để tạo đối tượng ViewModel.
                override fun <T : ViewModel> create(modelClass: Class<T>): T { //hương thức này được triển khai để tạo một đối tượng ViewModel dựa trên modelClass
                    return ContactViewModel(db.dao) as T //trả về 1 đối tượng ContactViewModel với đối số là db.dao là kiểu T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Learn_RoomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val state by viewModel.state.collectAsState()//lấy trạng thái của viewmodel thông qua flow
                    ContactScreen(state = state, onEvent = viewModel::onEvent) //gọi hàm compose ContactScreen
                }
            }
        }
    }
}

