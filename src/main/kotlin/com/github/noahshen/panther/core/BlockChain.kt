package com.github.noahshen.panther.core

import com.github.noahshen.panther.persistence.AccountStorage
import com.github.noahshen.panther.persistence.BlockStorage
import com.github.noahshen.panther.persistence.TransactionStorage

/**
 *
 */
class BlockChain(
        val nodeId: String,
        val blockStorage: BlockStorage,
        val transactionStorage: TransactionStorage,
        val accountStorage: AccountStorage
) {

    fun processBlock(block: Block) {

    }

}