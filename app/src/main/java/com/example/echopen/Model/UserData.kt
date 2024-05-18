//package com.example.echopen.Model
//
//data class UserData(
//    val email: String = "",
//    val name: String = "",
//    val profileImage: String = ""
//){
//    constructor(): this(name:"", email"", profileImage:"")
//}


package com.example.echopen.Model

data class UserData(
    val email: String = "",
    val name: String = "",
    val profileImage: String = ""
) {
    constructor() : this("", "", "")
}
