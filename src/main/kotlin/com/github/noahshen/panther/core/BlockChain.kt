package com.github.noahshen.panther.core

import com.github.noahshen.panther.persistence.*
import java.math.BigInteger

/**
 *
 */
class BlockChain(
        val nodeId: String,
        val blockStorage: BlockStorage,
        val lastBlockStorage: LastBlockStorage,
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

        // 更新最新区块地址
        val lastBlockEntity = LastBlockEntity(blockEntity.blockHash)
        lastBlockStorage.saveBlock(lastBlockEntity)
    }

    private fun processTransaction(transaction: Transaction) {

        val senderAccountEntity = loadOrCreateAccount(transaction.senderAddress)
        val receiverAccountEntity = loadOrCreateAccount(transaction.receiverAddress)

        val newSenderAccountEntity = senderAccountEntity.copy(
                publicKey = senderAccountEntity.publicKey,
                nonce = senderAccountEntity.nonce + BigInteger.ONE,
                balance = senderAccountEntity.balance - transaction.amount)
        accountStorage.saveAccount(newSenderAccountEntity)

        val newReceiverAccountEntity = senderAccountEntity.copy(
                publicKey = receiverAccountEntity.publicKey,
                nonce = receiverAccountEntity.nonce + BigInteger.ONE,
                balance = receiverAccountEntity.balance + transaction.amount)
        accountStorage.saveAccount(newReceiverAccountEntity)

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