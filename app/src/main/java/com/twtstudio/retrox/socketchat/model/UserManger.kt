package com.twtstudio.retrox.socketchat.model

import com.feinno.marketingdemo.delegate.Preference

/**
 * Created by retrox on 06/09/2017.
 */
object UserManger {
    var username: String by Preference("username","")
    var password: String by Preference("password","")
}