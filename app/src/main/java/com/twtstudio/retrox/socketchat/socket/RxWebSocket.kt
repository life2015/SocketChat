package com.twtstudio.retrox.socketchat.socket

import io.reactivex.Flowable
import okhttp3.Request
import okhttp3.WebSocket

/**
 * Created by retrox on 03/09/2017.
 */
interface RxWebSocket : WebSocket {

    fun observe(): Flowable<WebSocketEvent>

    companion object {
        fun createAutoRxWebSocket(request: Request): RxWebSocket = AutoManagedWebSocket(request = request)
    }
}