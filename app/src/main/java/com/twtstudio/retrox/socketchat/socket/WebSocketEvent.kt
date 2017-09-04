package com.twtstudio.retrox.socketchat.socket

import okhttp3.WebSocket
import okhttp3.Response
import okio.ByteString

/**
 * Created by retrox on 03/09/2017.
 */
sealed class WebSocketEvent(val webSocket: WebSocket?){
    class BinaryMessageEvent(webSocket: WebSocket?, val bytes: ByteString?) : WebSocketEvent(webSocket)
    class OpenedEvent(webSocket: WebSocket?, val response: Response?) : WebSocketEvent(webSocket)
    class StringMessageEvent(webSocket: WebSocket?, val text: String?) : WebSocketEvent(webSocket)
    class FailureEvent(webSocket: WebSocket?, val t: Throwable?, val response: Response?) : WebSocketEvent(webSocket)
    class ClosedEvent(webSocket: WebSocket?, val code: Int, val reason: String?) : WebSocketEvent(webSocket)
    class ClosingEvent(webSocket: WebSocket?, val code: Int, val reason: String?) : WebSocketEvent(webSocket)
}