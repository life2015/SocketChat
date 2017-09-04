package com.twtstudio.retrox.socketchat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.twtstudio.retrox.socketchat.socket.RxWebSocket
import com.twtstudio.retrox.socketchat.socket.WebSocketEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView

    val host = "172.23.88.7:8888"
    val url = "ws://$host/ws"
    val client = OkHttpClient.Builder().build()
    val request: Request = okhttp3.Request.Builder().url(url).build()
    var websocket: RxWebSocket = RxWebSocket.createAutoRxWebSocket(request)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findView(R.id.textview)

        websocket.observe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    login("allin")
                }
                .subscribe ({
                    event ->
                    when (event) {
                        is WebSocketEvent.StringMessageEvent -> {
                            textView.append(event.text)
                            print(event.text)
                            
                        }
                        else -> {
                            print(event.toString())
                        }
                    }
                },Throwable::printStackTrace)
    }


    fun login(name: String) {
        var message = """
    { "Sender": "$name",
            "MessageType": "login",
            "Content": "null",
            "Reciver": "null" }
     """
        websocket.send(message)
        print(message)
    }
}
