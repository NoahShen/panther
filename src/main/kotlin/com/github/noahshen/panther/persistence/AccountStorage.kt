package com.github.noahshen.panther.persistence

interface AccountStorage {

    fun loadAccount(publicKey: ByteArray): AccountEntity?

    fun saveAccount(accountEntity: AccountEntity)

    fun loadTransaction(transId: String): TransactionEntity?

    fun saveTransaction(transactionEntity: TransactionEntity)

    fun loadBlock(blockHash: ByteArray): BlockEntity?

    fun saveBlock(blockEntity: BlockEntity)
}