package com.github.noahshen.panther.network

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.junit.Before
import org.junit.Test

class PeerConnectionMgrTest {
    var peerConnectionMgr: PeerConnectionMgr? = null;
    @Before
    fun setUp() {
        val nodeId = "nodeId123"

        val bootNodes = listOf("127.0.0.1:1087")
        val networkEnv = "dev"
        val networkId = "dev"

        peerConnectionMgr = PeerConnectionMgr(nodeId, bootNodes, networkEnv, networkId)
    }

    @Test
    fun start() {

        launch (CommonPool){
            peerConnectionMgr!!.start()

        }
        Thread.sleep(5000)
        // TODO
    }

}