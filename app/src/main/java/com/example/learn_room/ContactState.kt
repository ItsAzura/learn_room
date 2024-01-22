package com.example.learn_room

//1 data class để biểu diễn state
data class ContactState (
    //1 danh sách các đối tượng contact mặc định là rỗng
    val contacts: List<Contact> = emptyList(),
    //đại diện cho các trương trong 1 contact
    val firstName: String = "",
    val lastName: String = "",
    val phoneNum: String = "",
    //xác định xem app đang trong quá trình thêm 1 contact mới hay không
    val isAddingContact: Boolean = false,
    //xác định kiểu sắp xếp cho danh sách liên hệ
    val sortType: SortType = SortType.FIRST_NAME
)