package com.example.learn_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(
    private val dao: ContactDao //tham số đầu vào 1 đối tượng từ lớp ContactDao
): ViewModel() {
    //dùng để duy trì và cập nhật và trạng thái của sortType
    private val _sortType = MutableStateFlow(SortType.FIRST_NAME)
    //dùng để duy trì và cập nhật và trạng thái của thông tin của contact
    private val _state = MutableStateFlow(ContactState())
    //dùng Kotlin Flow để lấy và quản lý danh sách contact dựa trên kiểu sắp xếp
    private val _contacts = _sortType
        //dùng flatMap để ánh xạ mối giá trị mới của "_sortType" thành 1 flow mới
        .flatMapLatest {sortType ->
            //dùng để xác định kiểu sắp xếp
            when(sortType){
                SortType.FIRST_NAME -> dao.getContactsOrderedByFirstName()
                SortType.LAST_NAME -> dao.getContactsOrderedByLastName()
                SortType.PHONE_NUMBER -> dao.getContactsOrderedByPhoneNum()
            }
        }
        .stateIn( //chuyển thành StateFlow và lưu trữ trong "_contacts"
            viewModelScope, //là 1 scope liên kết với viewmodel giúp quản lý công việc thực hiện trong vòng đời của ViewModel
            SharingStarted.WhileSubscribed(), //xác định cách chia sẻ dữ liệu giữa các người nghe
            emptyList()
        )
    val state = combine(_state,_sortType,_contacts) //kết hợp 3 flow lại với nhau
    { state,sortType,contacts -> //nhận 3 tham số tương ứng với giá trị mới nhất của mỗi flow
        state.copy(
            contacts = contacts, //danh sách contact dự trên kiểu sắp sắp xếp hiện tại
            sortType = sortType, //kiểu sắp xếp hiện tại
        )
    }.stateIn( //chuyển thành StateFlow và lưu trữ trong "state"
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), //xác định cách chia sẻ dữ liệu giữa các người nghe. nếu sau 5s không có người nghe nào đăng ký, dữ liệu sẽ ngừng được chia sẻ.
        ContactState() //giá trị mặc đinh khi chưa có dữ liệu đảm bào StateFlow không bao giờ null
    )

    fun onEvent(event: ContactEvent){
        //xác định kiểu dữ liệu
        when(event){
            //khi người dùng nhập tên
            is ContactEvent.SetFirstName -> {
                //_state.update để cập nhật trạng thái hiện tại bằng cách tạo một bản sao mới của ContactState với thông tin mới.
                _state.update {it.copy(
                    firstName = event.firstName
                ) }
            }
            //khi người dùng nhập họ
            is ContactEvent.SetLastName -> {
                _state.update {it.copy(
                    lastName = event.lastName
                ) }
            }
            //khi người dùng nhập sdt
            is ContactEvent.SetPhoneNum -> {
                _state.update {it.copy(
                    phoneNum = event.phoneNum
                ) }
            }
            //Khi người dùng chọn một kiểu sắp xếp mới
            is ContactEvent.SortContacts -> {
                _sortType.value = event.sortType //sự kiện này được gửi và giá trị _sortType được cập nhật dựa trên kiểu sắp xếp được chọn.
            }
            //Khi người dùng muốn lưu liên hệ mới
            ContactEvent.Savecontact -> {
                val fistName = state.value.firstName
                val lastName = state.value.lastName
                val phoneNum = state.value.phoneNum
                //kiểm tra xem các trường thông tin (firstName, lastName, phoneNum) có giá trị hay không
                if(fistName.isBlank() || lastName.isBlank() || phoneNum.isBlank()){
                    return
                }
                //Tạo một đối tượng Contact từ thông tin nhập vào
                val contact = Contact(
                    firstName = fistName,
                    lastName = lastName,
                    phoneNum = phoneNum,
                )
                //hực hiện upsertContact thông qua "dao"
                viewModelScope.launch {
                    dao.upsertContact(contact)
                }
                //Cập nhật trạng thái để đóng hộp thoại và xóa thông tin đã nhập.
                _state.update {it.copy(
                    isAddingContact = false,
                    firstName = "",
                    lastName = "",
                    phoneNum = ""
                ) }
            }
            //Khi người dùng muốn xóa một liên hệ
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact) //sự kiện này được gửi và thực hiện deleteContact thông qua dao.
                }
            }
            //Được sử dụng để hiển thị hộp thoại
            ContactEvent.ShowDialog -> {
                _state.update {it.copy(
                    isAddingContact = true
                ) }
            }
            //Được sử dụng để ẩn hộp thoại
            ContactEvent.HideDialog -> {
                _state.update {it.copy(
                    isAddingContact = false
                ) }
            }
        }
    }
}