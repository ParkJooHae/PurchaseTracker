package kr.jhp.purchtrac.ui.state.memo.detail

import kr.jhp.purchtrac.ui.state.UiIntent

sealed class MemoDetailIntent : UiIntent {
    data class LoadMemo(val memoId: Long?) : MemoDetailIntent()
    data class UpdateTitle(val title: String) : MemoDetailIntent()
    data class UpdateContent(val content: String) : MemoDetailIntent()
    object ToggleImportant : MemoDetailIntent()
    object SaveMemo : MemoDetailIntent()
    object DeleteMemo : MemoDetailIntent()
}