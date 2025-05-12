package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.Memo

interface MemoRepository {
    fun getAllMemos(): Flow<List<Memo>>
    fun getMemosByUserId(userId: Long): Flow<List<Memo>>
    fun getImportantMemos(): Flow<List<Memo>>
    suspend fun getMemoById(id: Long): Memo?
    fun searchMemos(query: String): Flow<List<Memo>>
    suspend fun insertMemo(memo: Memo): Long
    suspend fun updateMemo(memo: Memo)
    suspend fun deleteMemo(memoId: Long)
}
