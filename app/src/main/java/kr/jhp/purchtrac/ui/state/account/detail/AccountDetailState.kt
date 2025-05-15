package kr.jhp.purchtrac.ui.state.account.detail

import kr.jhp.purchtrac.ui.state.UiState

data class AccountDetailState(
    val accountId: Long? = null,
    val userId: Long = 1L, // 기본값, 실제로는 선택된 사용자를 사용
    val siteName: String = "",
    val siteUrl: String = "",
    val username: String = "",
    val password: String = "",
    val notes: String = "",
    val isNewAccount: Boolean = true,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val passwordVisible: Boolean = false,
    val error: String? = null
) : UiState