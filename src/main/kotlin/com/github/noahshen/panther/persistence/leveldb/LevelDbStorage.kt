package com.github.noahshen.panther.persistence.leveldb

import com.google.gson.Gson
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import java.io.File

abstract class LevelDbStorage(
        val dataBaseFilePath: String
){

    lateinit var leveldb: DB

    val gson = Gson() // for pretty print feature

    init {

        val options = Options()
        options.createIfMissing(true)
        options.compressionType(CompressionType.NONE)
        options.blockSize(10 * 1024 * 1024)
        options.writeBufferSize(10 * 1024 * 1024)
        options.cacheSize(0)
        options.paranoidChecks(true)
        options.verifyChecksums(true)
        options.maxOpenFiles(32)

        val databaseDir = File(dataBaseFilePath).parentFile
        if (!databaseDir.exists() || !databaseDir.isDirectory) {
            databaseDir.mkdirs()
        }

        val factory = Iq80DBFactory.factory
        leveldb = factory.open(File(dataBaseFilePath), options)
    }
}