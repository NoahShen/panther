package com.github.noahshen.panther.network

import com.github.noahshen.panther.core.Block
import com.github.noahshen.panther.core.Transaction

/**
 *
 */
class Peer(
        val nodeId: String,
        val ipAddress: String,
        val port: Int,
        val versoin: Int,
        val networkId: Int,
        val networkEnv: Int
) {

    var lastActivityTime : Long = 0

    fun close() {
        //TODO
    }

    /**
     *
     */
    fun sendTransaction(trx: Transaction) {

    }


    fun sendBlocks(blocks: List<Block>) {

    }


    fun loadBlocks(fromHeight: Long, numOfBlocks: Int) {

    }

    /**
     *
     */
    fun loadBlockHeaders(fromHeight: Long, numOfBlocks: Int) {

    }

    /**
     *
     */
    fun loadPeers() {
    }


    override fun toString(): String {
        return "Peer(nodeId='$nodeId', ipAddress='$ipAddress', port=$port, versoin=$versoin, networkId=$networkId, networkEnv=$networkEnv, lastActivityTime=$lastActivityTime)"
    }


}
