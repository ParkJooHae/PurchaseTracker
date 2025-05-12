package kr.jhp.purchtrac.ui.state.memo

import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.ui.state.UiIntent

sealed class MemoIntent : UiIntent {
    object LoadMemos : MemoIntent()
    data class SearchMemos(val query: String) : MemoIntent()
    data class ToggleImportant(val memoId: Long) : MemoIntent()
    data class DeleteMemo(val memoId: Long) : MemoIntent()
    data class UpdateMemo(val memo: Memo) : MemoIntent()
    data class SetFilterImportant(val showOnlyImportant: Boolean) : MemoIntent()
    object ClearSearchQuery : MemoIntent()
}