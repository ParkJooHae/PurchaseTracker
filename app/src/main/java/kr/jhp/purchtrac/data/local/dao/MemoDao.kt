package kr.jhp.purchtrac.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.data.local.entity.MemoEntity

@Dao
interface MemoDao {
    @Query("SELECT * FROM memos ORDER BY updatedAt DESC")
    fun getAllMemos(): Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getMemosByUserId(userId: Long): Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos WHERE isImportant = 1 ORDER BY updatedAt DESC")
    fun getImportantMemos(): Flow<List<MemoEntity>>

    @Query("SELECT * FROM memos WHERE id = :id")
    suspend fun getMemoById(id: Long): MemoEntity?

    @Query("SELECT * FROM memos WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun searchMemos(query: String): Flow<List<MemoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memo: MemoEntity): Long

    @Update
    suspend fun updateMemo(memo: MemoEntity)

    @Delete
    suspend fun deleteMemo(memo: MemoEntity)
}