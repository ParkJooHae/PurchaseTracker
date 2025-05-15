package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.ui.state.UiIntent

sealed class AccountIntent : UiIntent {
    object LoadAccounts : AccountIntent()
    data class SearchAccounts(val query: String) : AccountIntent()
    data class DeleteAccount(val accountId: Long) : AccountIntent()
    data class UpdateAccount(val account: Account) : AccountIntent()
    object ClearSearchQuery : AccountIntent()
}