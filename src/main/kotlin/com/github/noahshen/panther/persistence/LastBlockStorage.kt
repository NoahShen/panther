package com.github.noahshen.panther.persistence

interface LastBlockStorage {

    fun loadLastBlock(): LastBlockEntity?

    fun saveBlock(lastBlockEntity: LastBlockEntity)

}