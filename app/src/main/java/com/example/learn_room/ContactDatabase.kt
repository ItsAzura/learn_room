package com.example.learn_room

import androidx.room.Database
import androidx.room.RoomDatabase

//đánh dấu là 1 lớp csdl Room
@Database(
    entities = [Contact::class], //các thuộc tính của các bảng entity
    version = 1 //phiên bảng của csdl
)

//là 1 abstract class mở rộng từ RoomDatabase() là 1 lớp csdl của room và cung cấp các phương thức để truy cập DAO và quản lý csdl
abstract class ContactDatabase : RoomDatabase() {
    abstract val dao : ContactDao //dao cung cấp các phương thức để thực hiện các thao tác với csdl
}