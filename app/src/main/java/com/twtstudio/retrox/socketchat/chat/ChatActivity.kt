package com.twtstudio.retrox.socketchat.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.twtstudio.retrox.socketchat.R
import com.twtstudio.retrox.socketchat.findView
import com.twtstudio.retrox.socketchat.log
import com.twtstudio.retrox.socketchat.message.Message
import com.twtstudio.retrox.socketchat.model.ApiClient
import com.twtstudio.retrox.socketchat.model.UserManger
import com.twtstudio.retrox.socketchat.socket.WebSocketEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by retrox on 07/09/2017.
 */
class ChatActivity : AppCompatActivity() {
    val username = String(UserManger.username.toByteArray()) //缓存一下吧
    lateinit var sender: String
    val websocket = ApiClient.websocket
    lateinit var recyclerview: RecyclerView
    lateinit var editSendMsg: EditText
    lateinit var sendBtn: Button
    val adapter = ChatAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val toolbar = findView<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        sender = intent.getStringExtra("sender")
        title = sender
        recyclerview = findView(R.id.recyclerview)
        editSendMsg = findView(R.id.edit_message)
        sendBtn = findView(R.id.btn_send)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        websocket.observe()
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map { event: WebSocketEvent.StringMessageEvent -> event.text }
                .map { text: String -> ApiClient.gson.fromJson(text, Message::class.java) }
                .filter { (sender1) -> sender1 == sender }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ message: Message? ->
                    message?.let {
                        adapter.addMessage(message)
                    }
                },Throwable::printStackTrace)

        editSendMsg.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
//                mPasswordEdit.requestFocus();
                editSendMsg.requestFocus()
                send()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        sendBtn.setOnClickListener {
            send()
//            Toast.makeText(this@ChatActivity,result.toString(),Toast.LENGTH_SHORT).show()
        }

    }

    fun send() {
        val message = Message(sender = username,content = editSendMsg.text.toString(),receiver = sender)
        val stringMessage = "{\"Sender\":\"$username\",\"MessageType\":\"send_to_user\",\"Content\":\"${editSendMsg.text.toString()}\",\"Reciver\":\"$sender\"}"
        log("send To $sender > $stringMessage")
        websocket.send(stringMessage)
        adapter.addMessage(message)
        editSendMsg.setText("",TextView.BufferType.EDITABLE)

    }
}