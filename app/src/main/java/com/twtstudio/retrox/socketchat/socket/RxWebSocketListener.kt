package com.twtstudio.retrox.socketchat.socket

import io.reactivex.FlowableEmitter
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class RxWebSocketListener(private val emitter: FlowableEmitter<WebSocketEvent>, val errorHandler: (WebSocket?) -> Unit = {}) : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        emitter.onNext(WebSocketEvent.OpenedEvent(webSocket, response))
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        emitter.onNext(WebSocketEvent.FailureEvent(webSocket, t, response))
        errorHandler.invoke(webSocket)
    }

    override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
        emitter.onNext(WebSocketEvent.ClosingEvent(webSocket, code, reason))
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        emitter.onNext(WebSocketEvent.StringMessageEvent(webSocket, text))
    }

    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        emitter.onNext(WebSocketEvent.BinaryMessageEvent(webSocket, bytes))
    }

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        emitter.onNext(WebSocketEvent.ClosedEvent(webSocket, code, reason))
    }
}