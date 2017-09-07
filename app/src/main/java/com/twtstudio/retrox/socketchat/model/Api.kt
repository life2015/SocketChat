package com.twtstudio.retrox.socketchat.model

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by retrox on 04/09/2017.
 */
interface Api {
    @POST("login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Flowable<ResponseBody>

    @POST("logout")
    @FormUrlEncoded
    fun logout(@Field("username") username: String): Flowable<ResponseBody>

    @POST("getfriends")
    @FormUrlEncoded
    fun getFriends(@Field("username") username: String): Flowable<ResponseBody>

    @POST("addfriend")
    @FormUrlEncoded
    fun addFriend(@Field("username") username: String, @Field("friend") friendName: String): Flowable<String>

    @POST("deletefriend")
    @FormUrlEncoded
    fun deleteFriend(@Field("username") username: String, @Field("friend") friendName: String): Flowable<String>
}