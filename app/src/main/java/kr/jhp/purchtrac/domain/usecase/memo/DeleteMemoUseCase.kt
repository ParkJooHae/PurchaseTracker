package kr.jhp.purchtrac.domain.usecase.memo

import kr.jhp.purchtrac.domain.repository.MemoRepository
import javax.inject.Inject

class DeleteMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(memoId: Long) {
        memoRepository.deleteMemo(memoId)
    }
}