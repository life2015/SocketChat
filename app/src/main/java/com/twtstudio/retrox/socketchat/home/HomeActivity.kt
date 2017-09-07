package com.twtstudio.retrox.socketchat.home

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.twtstudio.retrox.socketchat.R
import com.twtstudio.retrox.socketchat.findView
import com.twtstudio.retrox.socketchat.log
import com.twtstudio.retrox.socketchat.model.ApiClient
import com.twtstudio.retrox.socketchat.model.UserManger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

/**
 * Created by retrox on 04/09/2017.
 */
class HomeActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var recyclerview: RecyclerView
    lateinit var srl: SwipeRefreshLayout
    val api = ApiClient.api
    val adapter = FriendsAdapter(mutableListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar = findView(R.id.toolbar)
        recyclerview = findView(R.id.recyclerview)
        srl = findView(R.id.srl_home)

        title = "Message"
        setSupportActionBar(toolbar)

        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        getFriends()

        srl.setOnRefreshListener {
            getFriends()
        }

    }

    fun getFriends() {
        api.getFriends(UserManger.username)
                .map { t: ResponseBody -> t.string() }
                .doOnNext { log(it) }
                .map { t: String -> t.split(",") }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: List<String>? ->
                    t?.let {
                        adapter.setNames(t)
                    }
                    if (srl.isRefreshing) {
                        srl.isRefreshing = false
                    }
                }, Throwable::printStackTrace)
    }

}