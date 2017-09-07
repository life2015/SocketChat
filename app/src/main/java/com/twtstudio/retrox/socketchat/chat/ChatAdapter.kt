package com.twtstudio.retrox.socketchat.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.twtstudio.retrox.socketchat.R
import com.twtstudio.retrox.socketchat.message.Message
import com.twtstudio.retrox.socketchat.model.UserManger

/**
 * Created by retrox on 07/09/2017.
 */
class ChatAdapter(val context: Context) : RecyclerView.Adapter<ChatViewHolder>() {
    val TYPE_RECEIVED = 0
    val TYPE_SENT = 1
    val username = String(UserManger.username.toByteArray()) //缓存一下吧
    val list = mutableListOf<Message>()
    var recyclerview: RecyclerView? = null

    fun addMessage(message: Message) {
        list.add(message)
        notifyItemInserted(list.size)
        recyclerview?.smoothScrollToPosition(list.size)

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerview = recyclerView
    }


    override fun onBindViewHolder(holder: ChatViewHolder?, position: Int) {
        holder?.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(context)
        if (viewType == TYPE_SENT) {
            val view = inflater.inflate(R.layout.item_my_message, parent, false)
            return ChatViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_recevied_message, parent, false)
            return ChatViewHolder(view)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        val message = list[position]
        if (message.sender == username) {
            return TYPE_SENT
        } else return TYPE_RECEIVED
    }
}