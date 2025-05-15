package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.ui.state.UiIntent

sealed class AccountListIntent : UiIntent {
    object LoadAccounts : AccountListIntent()
    data class SearchAccounts(val query: String) : AccountListIntent()
    data class DeleteAccount(val accountId: Long) : AccountListIntent()
    object ClearSearchQuery : AccountListIntent()
    object Authenticate : AccountListIntent()
    object AuthenticationSucceeded : AccountListIntent()
    object AuthenticationFailed : AccountListIntent()
}