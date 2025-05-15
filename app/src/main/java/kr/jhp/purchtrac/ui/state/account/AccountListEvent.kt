package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.ui.state.UiEvent

sealed class AccountListEvent : UiEvent {
    data class ShowToast(val message: String) : AccountListEvent()
    data class NavigateToAccountDetail(val accountId: Long?) : AccountListEvent()
    object RequireAuthentication : AccountListEvent()
    object NavigateBack : AccountListEvent()
}