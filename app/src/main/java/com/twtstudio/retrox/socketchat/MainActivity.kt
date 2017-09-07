package com.twtstudio.retrox.socketchat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.github.salomonbrys.kotson.fromJson
import com.twtstudio.retrox.socketchat.message.Message
import com.twtstudio.retrox.socketchat.model.ApiClient
import com.twtstudio.retrox.socketchat.model.UserManger
import com.twtstudio.retrox.socketchat.socket.RxWebSocket
import com.twtstudio.retrox.socketchat.socket.WebSocketEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView

    val host = "165.227.30.227:80"
    val url = "ws://$host/ws"
    val client = OkHttpClient.Builder().build()
    val request: Request = okhttp3.Request.Builder().url(url).build()
    var websocket: RxWebSocket = RxWebSocket.createAutoRxWebSocket(request)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findView(R.id.textview)

        val flowable = ApiClient.api.login(UserManger.username, UserManger.password)
                .flatMap {
                    t: ResponseBody ->
                    log(t.string())
                    websocket.observe()
                }
                .doOnNext {
                    if (it is WebSocketEvent.OpenedEvent){
                        login(UserManger.username)
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .replay(3).refCount()

        flowable.subscribe({
            event ->
            when (event) {
                is WebSocketEvent.StringMessageEvent -> {
                    val message = ApiClient.gson.fromJson(event.text,Message::class.java)
                    textView.append("${message.sender} send to you > ${message.content} \n")
                    println(event.text)

                }
                else -> {
                    println(event.toString())
                }
            }
        }, Throwable::printStackTrace)
//        flowable.subscribe({
//            event ->
//            when (event) {
//                is WebSocketEvent.StringMessageEvent -> {
//                    textView.append(event.text)
//                    println(event.text)
//
//                }
//                else -> {
//                    println(event.toString())
//                }
//            }
//        }, Throwable::printStackTrace)
    }


    fun login(name: String) {
        var message = """
    { "Sender": "$name",
            "MessageType": "login",
            "Content": "null",
            "Reciver": "null" }
     """
        websocket.send(message)
        println(message)
    }
}
