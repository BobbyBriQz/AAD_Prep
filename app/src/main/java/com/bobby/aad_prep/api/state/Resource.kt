package com.bobby.aad_prep.api.state

import java.util.*

class Resource<T> constructor(val status: Status, var data: T? = null, val message: String? = null, val date: Date? = null) {

    companion object {
        fun <T> success(data: T?, date: Date?): Resource<T> {
            return Resource(status = Status.SUCCESS, data = data, date = date)
        }

        fun <T> error(msg: String?, data: T?, date: Date?): Resource<T> {
            return Resource(Status.ERROR, data, msg, date)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(status = Status.LOADING, data = data)
        }

        fun <T> cached(data: T?, date: Date?): Resource<T> {
            return Resource(status = Status.CACHED, data = data, date = date)
        }

        //This might not be necessary
        fun <T> reAuthenticate(): Resource<T> {
            return Resource(Status.REAUTH)
        }

        //This might not be necessary
        fun <T> logout(): Resource<T> {
            return Resource(Status.LOGOUT)
        }
    }

}
