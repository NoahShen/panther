package com.github.noahshen.panther.persistence

interface TransactionStorage {

    fun loadTransaction(transId: String): TransactionEntity?

    fun saveTransaction(transactionEntity: TransactionEntity)

}