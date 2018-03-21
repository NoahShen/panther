package com.github.noahshen.panther.network

import com.github.noahshen.panther.config.ConfigUtils
import com.github.noahshen.panther.network.message.RegisterRequest
import com.github.noahshen.panther.network.message.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


data class RegisterResult(val success: Boolean, val peer: Peer?, val message:String? = null)

/**
 * 节点维护管理，后续可以加上定期从其他节点同步新节点
 */
class PeerConnectionMgr(
        val nodeId:String,
        val bootNodes: List<String>,
        val networkEnv: String,
        val networkId: String
){
    val JSON = MediaType.parse("application/json; charset=utf-8")

    val otherNodes = mutableMapOf<String, Peer>()

    val gson = Gson()

    suspend fun start() {

        val nodeAddress = ConfigUtils.getString("panther.nodeAddress")

        val registerJobs = mutableListOf<Deferred<RegisterResult>>()
        bootNodes.forEach { bootNode ->
            val registerJob = async(CommonPool) {
                // 连接到相应的节点
                registerToPeer(bootNode, nodeAddress!!)
            }
            registerJobs.add(registerJob)
        }

        // 获取注册结果
        registerJobs.forEach { registerJob ->
            val registerResult: RegisterResult = registerJob.await()
            if (registerResult.success) {
                val peer: Peer = registerResult.peer!!
                otherNodes[peer.nodeId] = peer
            }
        }
    }

    private fun registerToPeer(bootNode: String, nodeAddress: String): RegisterResult {
        val url = "$bootNode + ${NetworkConstants.REGISTER_NODE_PATH}"
        val client = OkHttpClient()

        val registerInfo = RegisterRequest(nodeAddress!!, networkId, networkEnv)

        val body = RequestBody.create(JSON, gson.toJson(registerInfo))
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val response = client.newCall(request).execute()
        val responseSting = response.body()!!.string()

        val registerResponse = gson.fromJson(responseSting, RegisterResponse::class.java)

        if (!registerResponse.success) {
            return RegisterResult(registerResponse.success, null, registerResponse.message)
        }

        val peerNodeId = registerResponse.nodeId
        val peerNodeAddress = registerResponse.nodeAddress
        val version = registerResponse.version
        val networkId = registerResponse.networkId
        val networkEnv = registerResponse.networkEnv

        if (this.nodeId == peerNodeId) {
            return RegisterResult(registerResponse.success, null, "节点ID非法，与本节点相同，peerNodeId=$peerNodeId")
        }
        // 验证节点信息
        if (this.networkId != networkId || this.networkEnv != networkEnv) {
            return RegisterResult(registerResponse.success, null, "节点信息不符，该节点networkId=$networkId, networkEnv=$networkEnv")
        }

        return RegisterResult(registerResponse.success, Peer(peerNodeId, peerNodeAddress, version, networkId, networkEnv))
    }
}