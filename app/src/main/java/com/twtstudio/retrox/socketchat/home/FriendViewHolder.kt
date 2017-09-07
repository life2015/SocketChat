package com.twtstudio.retrox.socketchat.home

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twtstudio.retrox.socketchat.R
import com.twtstudio.retrox.socketchat.chat.ChatActivity
import com.twtstudio.retrox.socketchat.findView
import com.twtstudio.retrox.socketchat.log
import com.twtstudio.retrox.socketchat.message.Message
import com.twtstudio.retrox.socketchat.model.ApiClient
import com.twtstudio.retrox.socketchat.model.UserManger
import com.twtstudio.retrox.socketchat.socket.WebSocketEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by retrox on 06/09/2017.
 */
class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findView(R.id.text_friend_name)
    val content: TextView = itemView.findView(R.id.text_friend_message)
    var subscription: Disposable? = null

    fun bind(name: String) {
        itemView.setOnClickListener {
            val intent = Intent(itemView.context,ChatActivity::class.java)
            intent.putExtra("sender",name)
            itemView.context.startActivity(intent)
        }
        this.name.text = name
        content.text = "_"
        subscription = ApiClient.websocket.observe()
                .doOnNext {
                    log(it)
                    if(it is WebSocketEvent.StringMessageEvent){
                        log("fuck ${it.text}")
                    }
                }
                .filter { it is WebSocketEvent.StringMessageEvent }
                .cast(WebSocketEvent.StringMessageEvent::class.java)
                .map { event: WebSocketEvent.StringMessageEvent -> event.text }
                .map { text: String -> ApiClient.gson.fromJson(text, Message::class.java) }
                .filter { mesage: Message -> mesage.sender == name }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: Message? -> content.text = t?.content }, Throwable::printStackTrace)
    }

    fun unbind() {
        if (!(subscription?.isDisposed ?: true)) {
            subscription?.dispose()
        }
    }
}