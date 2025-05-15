package kr.jhp.purchtrac.ui.state.account.detail

import kr.jhp.purchtrac.ui.state.UiState

data class AccountDetailState(
    val accountId: Long? = null,
    val userId: Long = 1L,
    val siteName: String = "",
    val siteUrl: String = "",
    val username: String = "",
    val password: String = "",
    val notes: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isNewAccount: Boolean = true,
    val error: String? = null
) : UiState