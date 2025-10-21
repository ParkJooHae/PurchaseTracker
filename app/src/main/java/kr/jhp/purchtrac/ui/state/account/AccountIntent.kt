package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.ui.state.UiIntent

sealed class AccountIntent : UiIntent {
    object LoadAccounts : AccountIntent()
    object LoadUsers : AccountIntent()
    data class SearchAccounts(val query: String) : AccountIntent()
    data class DeleteAccount(val accountId: Long) : AccountIntent()
    data class UpdateAccount(val account: Account) : AccountIntent()
    data class SelectUser(val userId: Long) : AccountIntent()
    data class CreateUser(val name: String) : AccountIntent()
    object ClearSearchQuery : AccountIntent()
}