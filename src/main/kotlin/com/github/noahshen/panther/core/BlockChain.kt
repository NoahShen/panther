package com.github.noahshen.panther.core

import com.github.noahshen.panther.persistence.*
import java.math.BigInteger

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

        // 保存区块
        val blockEntity = BlockEntity(
                block.hash,
                block.version,
                block.height,
                block.parentHash,
                block.coinBase,
                block.time.millis,
                block.difficulty,
                block.nonce,
                block.trxTrieRoot,
                block.transactions.map { it.hash() }

        )
        blockStorage.saveBlock(blockEntity)

        // 执行区块中的交易
        block.transactions.forEach { transaction: Transaction ->
            processTransaction(transaction)
        }
    }

    private fun processTransaction(transaction: Transaction) {

        val senderAccountEntity = loadOrCreateAccount(transaction.senderAddress)
        val receiverAccountEntity = loadOrCreateAccount(transaction.receiverAddress)

        // TODO 验证交易
        senderAccountEntity.balance > transaction.amount

        val transaction = TransactionEntity(
                String(transaction.hash()),
                transaction.senderAddress,
                transaction.receiverAddress,
                transaction.amount,
                transaction.time.millis,
                transaction.requestPublicKey.encoded,
                transaction.signature
        )
        transactionStorage.saveTransaction(transaction)
    }

    private fun loadOrCreateAccount(accountPublicKey: ByteArray): AccountEntity {
        var accountEntity = accountStorage.loadAccount(accountPublicKey)
        if (accountEntity != null) {
            return accountEntity
        }
        accountEntity =  AccountEntity(
                accountPublicKey,
                BigInteger.ZERO,
                BigInteger.ZERO
        )
        accountStorage.saveAccount(accountEntity)
        return accountEntity
    }


}