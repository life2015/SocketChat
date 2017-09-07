package com.twtstudio.retrox.socketchat

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.twtstudio.retrox.socketchat.home.HomeActivity
import com.twtstudio.retrox.socketchat.message.Message
import com.twtstudio.retrox.socketchat.model.ApiClient
import com.twtstudio.retrox.socketchat.model.UserManger
import com.twtstudio.retrox.socketchat.socket.WebSocketEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

/**
 * Created by retrox on 06/09/2017.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var username: TextInputEditText
    lateinit var password: TextInputEditText
    lateinit var loginBtn: Button
    lateinit var logoutBtn: Button
    lateinit var testBtn: Button
    lateinit var goBtn: Button
    lateinit var ipBtn: Button
    lateinit var ipEditText: TextInputEditText
    val websocket = ApiClient.websocket
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar = findView<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        title = "Login"

        username = findView(R.id.login_username)
        password = findView(R.id.login_password)
        loginBtn = findView(R.id.login_button)
        logoutBtn = findView(R.id.logout_button)
        testBtn = findView(R.id.test_button)
        goBtn = findView(R.id.go_button)
        ipEditText = findView(R.id.edit_ip)
        ipBtn = findView(R.id.setip_button)

        username.setText(UserManger.username, TextView.BufferType.EDITABLE)
        password.setText(UserManger.password, TextView.BufferType.EDITABLE)
        ipEditText.setText(UserManger.ip,TextView.BufferType.EDITABLE)


        loginBtn.setOnClickListener {
            login()
        }

        logoutBtn.setOnClickListener {
            logout()
        }

        testBtn.setOnClickListener {
            UserManger.username = username.text.toString()
            UserManger.password = password.text.toString()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        goBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            this@LoginActivity.startActivity(intent)
        }

        ipBtn.setOnClickListener {
            UserManger.ip = ipEditText.text.toString()
        }

    }

    fun login() {
        val disposeable = ApiClient.api.login(username.text.toString(), password.text.toString())
                .map {
                    response: ResponseBody ->
                    response.string()
                }
                .flatMap { t: String? ->
                    log(t)
                    if ((t ?: "error") == "success") {
                        return@flatMap websocket.observe()
                    } else throw RuntimeException("cannot login")
                }
                .doOnNext {
                    if (it is WebSocketEvent.StringMessageEvent) {
                        Toast.makeText(this@LoginActivity, it.text, Toast.LENGTH_SHORT).show()
                        log(it.text)
                    }
                }
                .filter { event: WebSocketEvent -> event is WebSocketEvent.OpenedEvent }
                .cast(WebSocketEvent.OpenedEvent::class.java)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val message = Message(username.text.toString(), "login", "null", "null")
                    log(message.toJson())
                    val result = websocket.send(message.toJson())
                    log("login:: $result")
                    Toast.makeText(this@LoginActivity, "Login sucessfully", Toast.LENGTH_SHORT).show()
                    UserManger.username = username.text.toString()
                    UserManger.password = password.text.toString()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    this@LoginActivity.startActivity(intent)
                    this@LoginActivity.finish()
                }, {
                    t: Throwable? ->
                    t?.apply {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                        printStackTrace()
                    }
                })

        compositeDisposable.add(disposeable)
    }

    fun logout() {
        val disposeable = ApiClient.api.logout(username.text.toString())
                .map { response: ResponseBody -> response.string() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
//                    websocket.close(-1,"exit")
                }, Throwable::printStackTrace)
        compositeDisposable.add(disposeable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}