package kr.jhp.purchtrac.domain.usecase.memo

import kr.jhp.purchtrac.domain.repository.MemoRepository
import javax.inject.Inject

class ToggleMemoImportanceUseCase @Inject constructor(
    private val memoRepository: MemoRepository,
    private val getMemoByIdUseCase: GetMemoByIdUseCase
) {
    suspend operator fun invoke(memoId: Long) {
        val memo = getMemoByIdUseCase(memoId) ?: return
        val updatedMemo = memo.copy(isImportant = !memo.isImportant)
        memoRepository.updateMemo(updatedMemo)
    }
}