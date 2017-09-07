package com.twtstudio.retrox.socketchat.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.twtstudio.retrox.socketchat.R
import com.twtstudio.retrox.socketchat.findView
import com.twtstudio.retrox.socketchat.message.Message

/**
 * Created by retrox on 07/09/2017.
 */
class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text: TextView = itemView.findView(R.id.text_message)

    fun bind(message: Message) {
        text.text = message.content
    }
}