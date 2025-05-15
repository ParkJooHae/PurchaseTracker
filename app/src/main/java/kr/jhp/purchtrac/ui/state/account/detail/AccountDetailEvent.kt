package kr.jhp.purchtrac.ui.state.account.detail

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class AccountDetailEvent : UiEvent {
    data class ShowToast(val message: String) : AccountDetailEvent()
    object AccountSaved : AccountDetailEvent()
    object AccountDeleted : AccountDetailEvent()
    object NavigateBack : AccountDetailEvent()
}