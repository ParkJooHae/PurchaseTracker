package kr.jhp.purchtrac.domain.usecase.memo

import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.domain.repository.MemoRepository
import javax.inject.Inject

class SaveMemoUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(memo: Memo): Long {
        return if (memo.id == 0L) {
            memoRepository.insertMemo(memo)
        } else {
            memoRepository.updateMemo(memo)
            memo.id
        }
    }
}