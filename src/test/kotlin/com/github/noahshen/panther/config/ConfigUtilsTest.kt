package com.github.noahshen.panther.config

import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory



class ConfigUtilsTest {
    val logger = LoggerFactory.getLogger(ConfigUtilsTest::class.java)

    @Before
    fun setUp() {
        System.setProperty("configPath", System.getProperty("user.dir") + "/configfiles")
        System.setProperty("configEnv", "dev") }

    @Test
    fun testLoadBaseConfig() {
        val nodeId = ConfigUtils.getString("panther.nodeId")
        println("nodeId:$nodeId")
    }
    @Test
    fun testLoadListConfig() {
        val listProp2 = ConfigUtils.getStringList("panther.listProp2")
        println("listProp2:$listProp2")
        logger.info("listProp2:$listProp2")
    }
}