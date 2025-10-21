package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.ui.state.UiState

data class AccountState(
    val accounts: List<Account> = emptyList(),
    val filteredAccounts: List<Account> = emptyList(),
    val allUsers: List<User> = emptyList(),
    val currentUserId: Long = 1L,
    val isLoading: Boolean = false,
    val isLoadingUsers: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
) : UiState