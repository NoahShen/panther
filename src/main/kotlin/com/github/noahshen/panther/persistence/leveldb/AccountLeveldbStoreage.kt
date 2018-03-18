package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.persistence.AccountEntity
import com.github.noahshen.panther.persistence.AccountStorage
import com.github.noahshen.panther.persistence.BlockEntity
import com.github.noahshen.panther.persistence.TransactionEntity
import com.google.gson.Gson
import org.iq80.leveldb.CompressionType
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options
import org.iq80.leveldb.impl.Iq80DBFactory
import java.io.File

class AccountLeveldbStoreage(
        private val dataBaseFilePath: String
) : AccountStorage {

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

    override fun saveAccount(accountEntity: AccountEntity) {
        leveldb.put(accountEntity.publicKey, gson.toJson(accountEntity).toByteArray())
    }

    override fun loadTransaction(transId: String): TransactionEntity? {
        val transactionBytes: ByteArray = leveldb.get(transId.toByteArray())
        if (transactionBytes.isEmpty()) {
            return null
        }
        return gson.fromJson(String(transactionBytes), TransactionEntity::class.java)
    }

    override fun saveTransaction(transactionEntity: TransactionEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBlock(blockHash: ByteArray): BlockEntity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveBlock(blockEntity: BlockEntity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAccount(publicKey: ByteArray): AccountEntity? {
        val accountBytes: ByteArray = leveldb.get(publicKey)
        if (accountBytes.isEmpty()) {
            return null
        }
        return gson.fromJson(String(accountBytes), AccountEntity::class.java)
    }
}