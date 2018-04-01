package com.github.noahshen.panther.network

import com.github.noahshen.panther.network.message.RegisterRequest
import com.github.noahshen.panther.network.message.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.junit.Before
import org.junit.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.HttpStatusCode


class PeerConnectionMgrTest {
    val networkEnv = "dev"
    val networkId = "dev"

    val gson = Gson()

    var peerConnectionMgr: PeerConnectionMgr? = null;
    @Before
    fun setUp() {
        val peerNodeId = "nodeId456"
        val peerNodeAddress = "127.0.0.1:1088"

        val registerInfo = RegisterRequest(peerNodeAddress, networkId, networkEnv)
        val response = RegisterResponse(true, null, peerNodeId, peerNodeAddress, "mock-server123", networkId, networkEnv)

        println(gson.toJson(response))
        MockServerClient("localhost", 1087)
                .`when`(
                        request()
                                .withMethod("POST")
                                .withPath(NetworkConstants.REGISTER_NODE_PATH)
                                .withBody(gson.toJson(registerInfo))
                )
                .respond(
                        response()
                                .withStatusCode(HttpStatusCode.ACCEPTED_202.code())
                                .withBody(gson.toJson(response))
                )


        val nodeId = "nodeId123"
        val bootNodes = listOf("127.0.0.1:1087")


        peerConnectionMgr = PeerConnectionMgr(nodeId, bootNodes, networkEnv, networkId)
    }

    @Test
    fun start() {

        launch (CommonPool){
            peerConnectionMgr!!.start()

        }
        Thread.sleep(5000)
        // TODO
        println(peerConnectionMgr!!.otherNodes)
    }

}