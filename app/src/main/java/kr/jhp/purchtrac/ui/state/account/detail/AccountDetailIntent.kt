package kr.jhp.purchtrac.ui.state.account.detail

import kr.jhp.purchtrac.ui.state.UiIntent

sealed class AccountDetailIntent : UiIntent {
    data class LoadAccount(val accountId: Long?) : AccountDetailIntent()
    data class UpdateSiteName(val siteName: String) : AccountDetailIntent()
    data class UpdateSiteUrl(val siteUrl: String) : AccountDetailIntent()
    data class UpdateUsername(val username: String) : AccountDetailIntent()
    data class UpdatePassword(val password: String) : AccountDetailIntent()
    data class UpdateNotes(val notes: String) : AccountDetailIntent()
    object TogglePasswordVisibility : AccountDetailIntent()
    object SaveAccount : AccountDetailIntent()
    object DeleteAccount : AccountDetailIntent()
}