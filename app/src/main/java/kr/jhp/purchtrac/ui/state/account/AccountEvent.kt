package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class AccountEvent : UiEvent {
    data class ShowToast(val message: String) : AccountEvent()
    data class NavigateToAccountDetail(val accountId: Long?) : AccountEvent()
    object NavigateBack : AccountEvent()
}