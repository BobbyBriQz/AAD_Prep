package com.bobby.aad_prep.api.state

enum class Status {
    SUCCESS, ERROR, LOADING, CACHED, REAUTH, LOGOUT //Note: Logout Cached and REAUTH might not be used
}