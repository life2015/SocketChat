package com.twtstudio.retrox.socketchat

import com.twtstudio.retrox.socketchat.socket.RxWebSocket
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.concurrent.TimeUnit
import io.reactivex.Scheduler
import io.reactivex.plugins.RxJavaPlugins


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    val host = "127.0.0.1:8888"
    val url = "ws://$host/ws"
    val client = OkHttpClient.Builder().build()
    val request: Request = okhttp3.Request.Builder().url(url).build()
    var websocket: RxWebSocket = RxWebSocket.createAutoRxWebSocket(request)

    @Before
    fun init() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun addition_isCorrect() {
        Flowable.just("11")
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
                .delay(1, TimeUnit.SECONDS)
                .doOnNext { print("2222\n") }
                .subscribe { print(it) }
        websocket.observe()
                .subscribe {
                    print(it.toString())
                }
        login("allin")

    }

    fun login(name: String) {
        var message = """
    "Sender": $name,
            "MessageType": "login",
            "Content": "null",
            "Reciver": "null"
     """
        websocket.send(message)
    }

}
