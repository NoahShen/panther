package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.core.Account
import com.github.noahshen.panther.persistence.TransactionEntity
import org.junit.Before
import org.junit.Test
import java.io.File
import java.math.BigInteger

class TransactionLevelDbStorageTest {

    lateinit var transactionStorage: TransactionLevelDbStorage

    @Before
    fun setUp() {
        val projectPath = System.getProperty("user.dir")
        println(projectPath)
        val filePath = projectPath + "/dbfiles/transactionSaveLoadTestStorage"
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
            file.deleteRecursively()
        }
        transactionStorage = TransactionLevelDbStorage(filePath)
    }


    @Test
    fun saveAndLoadTransaction() {

        val account1 = Account.createAccount()
        val account1PublicKey = account1.publicKey

        val account2 = Account.createAccount()
        val account2PublicKey = account2.publicKey

        val transHash = "123"
        val transactionEntity1 = TransactionEntity(
                transHash,
                account1PublicKey.encoded,
                account2PublicKey.encoded,
                BigInteger.ZERO,
                System.currentTimeMillis(),
                account1PublicKey.encoded,
                "signature123".toByteArray()
                )
        transactionStorage.saveTransaction(transactionEntity1)

        val transactionEntity2: TransactionEntity? = transactionStorage.loadTransaction(transHash)
        assert(transactionEntity2!!.transHash == transHash)
        assert(transactionEntity2!!.senderAddress.contentEquals(account1PublicKey.encoded))
        assert(transactionEntity2!!.receiverAddress.contentEquals(account2PublicKey.encoded))
    }

}