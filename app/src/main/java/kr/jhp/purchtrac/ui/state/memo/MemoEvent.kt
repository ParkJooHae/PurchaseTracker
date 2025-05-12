package kr.jhp.purchtrac.ui.state.memo

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class MemoEvent : UiEvent {
    data class ShowToast(val message: String) : MemoEvent()
    data class NavigateToMemoDetail(val memoId: Long?) : MemoEvent() // null은 새 메모
    object NavigateBack : MemoEvent()
}