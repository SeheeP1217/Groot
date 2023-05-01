package com.chocobi.groot.view.user.model




// 회원정보 조회
data class GetUserResponse(
    var result: String,
    var msg: String,
    var user: User
)

data class User(
    var id: Int,
    var userId: String,
    var nickName: String,
    var profile: String,
    var registerDate: Int
)


