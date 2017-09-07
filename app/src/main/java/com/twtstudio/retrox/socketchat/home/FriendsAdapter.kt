package com.twtstudio.retrox.socketchat.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.twtstudio.retrox.socketchat.R
import com.twtstudio.retrox.socketchat.log

/**
 * Created by retrox on 06/09/2017.
 */
class FriendsAdapter(val names: MutableList<String>, val context: Context) : RecyclerView.Adapter<FriendViewHolder>() {

    fun setNames(newNames: List<String>) {
        names.clear()
        names.addAll(newNames)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = names.size

    override fun onBindViewHolder(holder: FriendViewHolder?, position: Int) {
        log(names[position])
        holder?.apply {
            bind(names[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_home_friend,parent,false)
        return FriendViewHolder(view)
    }

    override fun onViewDetachedFromWindow(holder: FriendViewHolder?) {
        holder?.unbind()
    }
}