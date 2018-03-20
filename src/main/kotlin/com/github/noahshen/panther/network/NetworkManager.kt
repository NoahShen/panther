package com.github.noahshen.panther.network

import com.github.noahshen.panther.config.ConfigUtils
import org.slf4j.LoggerFactory

class NetworkManager {

    lateinit var nodeId: String

    companion object {
        private val LOGGER = LoggerFactory.getLogger(NetworkManager::class.java)
    }

    fun init(): Boolean {

        // 获取当前节点ID
        val nodeIdFromConfig = ConfigUtils.getString("panther.nodeId")
        if (nodeIdFromConfig.isNullOrBlank()) {
            LOGGER.info("Node id not found!")
            return false
        }
        nodeId = nodeIdFromConfig!!

        // 加载配置文件
        val bootNodes = ConfigUtils.getStringList("panther.bootNodes")
        if (bootNodes.isEmpty()) {
            LOGGER.info("Not boot nodes found!")
            // 没有其他启动节点，启动完成
            return true
        }

        // TODO 连接BootNodes

        // TODO 同步区块

        // TODO 启动本地http服务

        return true
    }
}