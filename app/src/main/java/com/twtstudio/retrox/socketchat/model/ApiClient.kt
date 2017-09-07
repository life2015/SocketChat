package com.twtstudio.retrox.socketchat.model

import com.google.gson.Gson
import com.twtstudio.retrox.socketchat.socket.RxWebSocket
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type




/**
 * Created by retrox on 03/09/2017.
 */
object ApiClient {
    val retrofit = Retrofit.Builder()
            .baseUrl("http://165.227.30.227/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    val api: Api = retrofit.create(Api::class.java)

    private val host = "165.227.30.227:80"
    private val url = "ws://$host/ws"
    private val request: Request = okhttp3.Request.Builder().url(url).build()
    val websocket: RxWebSocket = RxWebSocket.createAutoRxWebSocket(request)

    val gson = Gson()


    private object StringConverter : Converter<ResponseBody, String> {
        override fun convert(value: ResponseBody?): String = value.toString()
    }

    private class StringConverterFactory : Converter.Factory() {

        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
            if (type === String::class.java) {
                return StringConverter
            }
            //其它类型我们不处理，返回null就行
            return null
        }

        companion object {

            val INSTANCE = StringConverterFactory()

            fun create(): StringConverterFactory {
                return INSTANCE
            }
        }
    }
}