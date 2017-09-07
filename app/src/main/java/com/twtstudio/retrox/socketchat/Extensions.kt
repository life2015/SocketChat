package com.twtstudio.retrox.socketchat

import android.app.Activity
import android.support.annotation.IdRes
import android.util.Log
import android.view.View

/**
 * Created by retrox on 03/09/2017.
 */

inline fun <reified T> Activity.findView(@IdRes id: Int) = findViewById(id) as T

inline fun <reified T> View.findView(@IdRes id: Int) = findViewById(id) as T

fun Any.log(obj: Any?) {
    Log.d(Any::class.simpleName,obj?.toString())
}
