package com.example.learn_room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity //Đánh dấu 1 lớp "Contact" là 1 entity trong csdl. mỗi instant của "Contact" tương ứng với 1 dòng trọng bảng trong csdl
data class Contact( //Định nghĩa 1 entity "Contact" để lưu trữ dữ liệu trong csdl SQLite và dùng Room để quản lý và tương tác với dữ liệu
    //các trường của entity "Contact"
    val firstName: String,
    val lastName: String,
    val phoneNum: String,
    //Khoá chính
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
)
