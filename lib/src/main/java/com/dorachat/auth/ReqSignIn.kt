package com.dorachat.auth

data class ReqSignIn(
                     val erc20: String,
                     val authWord: String,
                     val partitionId: String? = null) : BaseReq() {

    init {
        payload = sort()
    }
}