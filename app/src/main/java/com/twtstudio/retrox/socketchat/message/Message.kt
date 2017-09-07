package com.twtstudio.retrox.socketchat.message

import com.google.gson.annotations.SerializedName
import com.twtstudio.retrox.socketchat.model.ApiClient

/**
 * Created by retrox on 04/09/2017.
 */
data class Message(@SerializedName("Sender") val sender: String,
                   @SerializedName("MessageType") val messageType: String = "send_to_user",
                   @SerializedName("Content") val content: String = "",
                   @SerializedName("Receiver") val receiver: String
                   ){
    fun toJson(): String = ApiClient.gson.toJson(this)
}
