package com.github.noahshen.panther.network

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

/**
 *
 */
class PeerConnectionMgr(
        val nodeId:String
){

    val otherNodes = mutableMapOf<String, Peer>()


    fun start() {

        launch(CommonPool) {
            // 挂起当前上下文而非阻塞1000ms
            println("." + Thread.currentThread().name)
        }
    }
}