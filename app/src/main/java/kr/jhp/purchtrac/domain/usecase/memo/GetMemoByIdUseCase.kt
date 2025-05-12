package kr.jhp.purchtrac.domain.usecase.memo

import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.domain.repository.MemoRepository
import javax.inject.Inject

class GetMemoByIdUseCase @Inject constructor(
    private val memoRepository: MemoRepository
) {
    suspend operator fun invoke(id: Long): Memo? {
        return memoRepository.getMemoById(id)
    }
}