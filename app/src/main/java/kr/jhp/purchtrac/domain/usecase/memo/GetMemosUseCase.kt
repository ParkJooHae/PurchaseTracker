package kr.jhp.purchtrac.domain.usecase.memo

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.domain.repository.MemoRepository
import javax.inject.Inject

class GetMemosUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    operator fun invoke(): Flow<List<Memo>> {
        return memoRepository.getAllMemos()
    }
}