package com.github.noahshen.panther.persistence.leveldb

import com.github.noahshen.panther.core.Account
import com.github.noahshen.panther.persistence.BlockEntity
import org.junit.Before
import org.junit.Test
import java.io.File

class BlockLevelDbStorageTest {

    lateinit var blockStorage: BlockLevelDbStorage

    @Before
    fun setUp() {
        val projectPath = System.getProperty("user.dir")
        println(projectPath)
        val filePath = projectPath + "/dbfiles/blockSaveLoadTestStorage"
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
            file.deleteRecursively()
        }
        blockStorage = BlockLevelDbStorage(filePath)
    }


    @Test
    fun saveAndLoadBlock() {
        val account1 = Account.createAccount()
        val publicKey = account1.publicKey

        val blockHash = "blockHash123".toByteArray()
        val blockEntity1 = BlockEntity(
                blockHash,
                 1,
                1L,
                "0".toByteArray(),
                publicKey.encoded,
                System.currentTimeMillis(),
                0L,
                123,
                "trxRoot".toByteArray(),
                listOf("transId123".toByteArray()))
        blockStorage.saveBlock(blockEntity1)

        val blockEntity2 = blockStorage.loadBlock(blockHash)
        assert(blockEntity2!!.coinBase.contentEquals(publicKey.encoded))
    }

}