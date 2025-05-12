package kr.jhp.purchtrac.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.data.local.entity.UserWithAccounts
import kr.jhp.purchtrac.data.local.entity.UserWithProducts

@Dao
interface RelationDao {
    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersWithAccounts(): Flow<List<UserWithAccounts>>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithAccounts(userId: Long): Flow<UserWithAccounts>

    @Transaction
    @Query("SELECT * FROM users")
    fun getUsersWithProducts(): Flow<List<UserWithProducts>>

    @Transaction
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserWithProducts(userId: Long): Flow<UserWithProducts>
}