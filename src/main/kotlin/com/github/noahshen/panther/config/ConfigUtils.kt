package com.github.noahshen.panther.config;

import org.cfg4j.provider.ConfigurationProvider
import org.cfg4j.provider.ConfigurationProviderBuilder
import org.cfg4j.source.context.environment.ImmutableEnvironment
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider
import org.cfg4j.source.files.FilesConfigurationSource
import org.cfg4j.source.reload.strategy.PeriodicalReloadStrategy
import org.slf4j.LoggerFactory
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit






/**
 *
 */
class ConfigUtils private constructor() {

    companion object {
        val LOGGER = LoggerFactory.getLogger(ConfigUtils::class.java)

        val provider: ConfigurationProvider? by lazy { getInstance() }


        private fun getInstance(): ConfigurationProvider? {

            val configFilePath = System.getProperty("configPath")
            if (configFilePath == null) {
                LOGGER.info("***** ConfigRepoPath is empty *****")
                return null
            }
            val repoBranch = System.getProperty("configEnv")
            if (repoBranch == null) {
                LOGGER.info("***** repoBranch is empty *****")
                return null
            }
            var configReloadSec = System.getProperty("configReloadSec")
            if (configReloadSec == null) {
                configReloadSec = "5"
            }
            LOGGER.debug("Start loading config from file[{$configFilePath]")
            try {

                val configFilesProvider = ConfigFilesProvider {
                    mutableListOf(Paths.get("application.yaml"))
                }
                val source = FilesConfigurationSource(configFilesProvider)

                val environment = ImmutableEnvironment(configFilePath)

                val reloadStrategy = PeriodicalReloadStrategy(configReloadSec.toLong(), TimeUnit.SECONDS)

                // Create provider
                return ConfigurationProviderBuilder()
                        .withConfigurationSource(source)
                        .withReloadStrategy(reloadStrategy)
                        .withEnvironment(environment)
                        .build()
            } catch (e: Exception) {
                LOGGER.error("***** Init configuration error *****", e)
            }
            LOGGER.debug("Loading config complete")
            return null

        }

        fun getString(key: String, defaultValue: String? = null): String? {
            if (provider == null) {
                LOGGER.debug("Config init failed, use defaultValue=$defaultValue, requestKey=$key")
                return defaultValue
            }
            val configProvider = provider!!
            try {
                return configProvider.getProperty(key, String::class.java) ?: return defaultValue
            } catch (e: Exception) {
                LOGGER.error("***** load property error *****,key=$key", e)
            }
            return defaultValue
        }


        fun getDouble(key: String, defaultValue: Double? = null): Double? {
            if (provider == null) {
                LOGGER.debug("Config init failed, use defaultValue={}, requestKey={}", defaultValue, key)
                return defaultValue
            }
            val configProvider = provider!!

            try {
                return configProvider.getProperty(key, Double::class.java) ?: return defaultValue
            } catch (e: Exception) {
                LOGGER.error("***** load property error *****,key=$key", e)
            }
            return defaultValue
        }


        fun getInteger(key: String, defaultValue: Integer? = null): Integer? {
            if (provider == null) {
                LOGGER.debug("Config init failed, use defaultValue={}, requestKey={}", defaultValue, key)
                return defaultValue
            }
            val configProvider = provider!!
            try {
                return configProvider.getProperty(key, Integer::class.java) ?: return defaultValue
            } catch (e: Exception) {
                LOGGER.error("***** load property error *****,key=$key", e)
            }
            return defaultValue
        }

        fun getBoolean(key: String, defaultValue: Boolean? = null): Boolean? {
            if (provider == null) {
                LOGGER.debug("Config init failed, use defaultValue=$defaultValue, requestKey=$key")
                return defaultValue
            }
            val configProvider = provider!!

            try {
                return configProvider.getProperty(key, Boolean::class.java) ?: return defaultValue
            } catch (e: Exception) {
                LOGGER.error(String.format("***** load property error *****,key=%s", key), e)
            }
            return defaultValue
        }

        fun getIntList(key: String, splitChar: String = ","): List<Int> {
            val value = getString(key)
            if (value.isNullOrBlank()) {
                return Collections.emptyList()
            }
            val arr = value!!.split(splitChar.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (arr == null || arr.isEmpty()) {
                return Collections.emptyList()
            }
            return arr.map { it.toInt() }
        }


        fun getStringList(key: String, splitChar: String = ","): List<String> {
            val value = getString(key)
            if (value.isNullOrBlank()) {
                return Collections.emptyList()
            }
            val arr = value!!.split(splitChar.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
             if (arr == null || arr.size == 0) {
                return Collections.emptyList()
            }
            return arr.map { it }

        }

    }
}