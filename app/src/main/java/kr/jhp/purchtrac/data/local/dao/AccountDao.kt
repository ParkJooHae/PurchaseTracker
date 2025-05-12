package kr.jhp.purchtrac.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.data.local.entity.AccountEntity

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    fun getAccountsByUserId(userId: Long): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): AccountEntity?

    @Query("SELECT * FROM accounts WHERE siteName LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%'")
    fun searchAccounts(query: String): Flow<List<AccountEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Delete
    suspend fun deleteAccount(account: AccountEntity)
}