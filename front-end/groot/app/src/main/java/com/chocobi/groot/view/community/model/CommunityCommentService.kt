package com.chocobi.groot.view.community

import com.chocobi.groot.view.community.model.CommunityArticleDetailResponse
import com.chocobi.groot.view.community.model.CommunityArticleListResponse
import com.chocobi.groot.view.community.model.CommunityCommentResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CommunityCommentService {

    @GET("/api/comments/list/{articleId}") // 요청 url
    fun requestCommunityArticleDetail(
//        input 정의
        @Path("articleId") articleIdInput:Int,
        @Query("page") pageInput:Int,
        @Query("size") sizeInput:Int
        ) : Call<CommunityCommentResponse> // output 정의
}