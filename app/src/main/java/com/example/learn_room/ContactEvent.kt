package com.example.learn_room

//là 1 loại interface nhằm đảm bảo rằng các lớp triển khai của interface đó đều được khai báo trong cùng 1 nơi
sealed interface ContactEvent {
    //định nghĩa sự kiện lưu thông tin liên lạc
    object Savecontact: ContactEvent

    //định nghĩa các sự kiện đặt giá trị của từng trường
    data class SetFirstName(val firstName: String): ContactEvent
    data class SetLastName(val lastName: String): ContactEvent
    data class SetPhoneNum(val phoneNum: String): ContactEvent
    data class SetAge(val age: String): ContactEvent

    //định nghĩa sự kiện hiện và ẩn hộp thoại
    object ShowDialog: ContactEvent
    object HideDialog: ContactEvent

    //định nghĩa sự kiện sắp xếp danh sách theo "SortType"
    data class SortContacts(val sortType: SortType): ContactEvent

    //định nghĩa các sự kiện xoá 1 contact củ thể
    data class DeleteContact(val contact: Contact): ContactEvent
}