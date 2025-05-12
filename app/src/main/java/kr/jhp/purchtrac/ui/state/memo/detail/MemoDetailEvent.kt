package kr.jhp.purchtrac.ui.state.memo.detail

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class MemoDetailEvent : UiEvent {
    data class ShowToast(val message: String) : MemoDetailEvent()
    object NavigateBack : MemoDetailEvent()
    object MemoSaved : MemoDetailEvent()
    object MemoDeleted : MemoDetailEvent()
}