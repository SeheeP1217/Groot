package com.chocobi.groot.view.community.model

// output을 만든다 : response

data class CommunityCommentResponse(
    val comment: Comment,
)

data class Comment (
    val content: List<CommentContent>,
    val pageable: Pageable
)

data class CommentContent (
    val commentId: Int,
    val userPK: Int,
    val nickName: String,
    val content: String,
    val profile: String?,
    val createTime: CreateTime,
    val updateTime: UpdateTime
)

