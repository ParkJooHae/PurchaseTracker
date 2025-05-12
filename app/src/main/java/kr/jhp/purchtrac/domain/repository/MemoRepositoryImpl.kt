package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.jhp.purchtrac.data.local.dao.MemoDao
import kr.jhp.purchtrac.data.local.entity.MemoEntity
import kr.jhp.purchtrac.domain.model.Memo
import javax.inject.Inject

class MemoRepositoryImpl @Inject constructor(
    private val memoDao: MemoDao
) : MemoRepository {

    override fun getAllMemos(): Flow<List<Memo>> {
        return memoDao.getAllMemos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getMemosByUserId(userId: Long): Flow<List<Memo>> {
        return memoDao.getMemosByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getImportantMemos(): Flow<List<Memo>> {
        return memoDao.getImportantMemos().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getMemoById(id: Long): Memo? {
        return memoDao.getMemoById(id)?.toDomainModel()
    }

    override fun searchMemos(query: String): Flow<List<Memo>> {
        return memoDao.searchMemos(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun insertMemo(memo: Memo): Long {
        return memoDao.insertMemo(memo.toEntity())
    }

    override suspend fun updateMemo(memo: Memo) {
        memoDao.updateMemo(memo.toEntity())
    }

    override suspend fun deleteMemo(memoId: Long) {
        memoDao.getMemoById(memoId)?.let {
            memoDao.deleteMemo(it)
        }
    }

    // 확장 함수: Entity <-> Domain 모델 변환
    private fun MemoEntity.toDomainModel(): Memo {
        return Memo(
            id = this.id,
            userId = this.userId,
            title = this.title,
            content = this.content,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            isImportant = this.isImportant
        )
    }

    private fun Memo.toEntity(): MemoEntity {
        return MemoEntity(
            id = this.id,
            userId = this.userId,
            title = this.title,
            content = this.content,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            isImportant = this.isImportant
        )
    }
}