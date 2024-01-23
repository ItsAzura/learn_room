package com.example.learn_room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//class interface được Room sử dụng để tạo các phương thức để truy cập với csdl SQLite thông quan queries
@Dao
interface ContactDao {
    @Upsert() //thực hiện cả chức năng insert và update 1 entity trong csdl
    suspend fun upsertContact(contact: Contact)

    @Delete //thực hiện cả chức năng delete 1 entity ra khỏi csdl
    suspend fun deleteContact(contact: Contact)

    //các truy vần theo firstname, lastname và phoneNum theo ASC và tự động cập nhật theo thời gian thực

    @Query("Select * from Contact Order by firstName ASC")
    fun getContactsOrderedByFirstName(): Flow<List<Contact>>

    @Query("Select * from Contact Order by lastName ASC")
    fun getContactsOrderedByLastName(): Flow<List<Contact>>

    @Query("Select * from Contact Order by phoneNum ASC")
    fun getContactsOrderedByPhoneNum(): Flow<List<Contact>>

    @Query("Select * from Contact Order by age ASC")
    fun getContactsOrderedByAge(): Flow<List<Contact>>
}