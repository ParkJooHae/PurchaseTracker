package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.Account

interface AccountRepository {
    fun getAllAccounts(): Flow<List<Account>>
    fun getAccountsByUserId(userId: Long): Flow<List<Account>>
    suspend fun getAccountById(id: Long): Account?
    fun searchAccounts(query: String): Flow<List<Account>>
    suspend fun insertAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    suspend fun deleteAccount(accountId: Long)
}