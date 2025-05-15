package kr.jhp.purchtrac.ui.state.account

import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.ui.state.UiState

data class AccountListState(
    val accounts: List<Account> = emptyList(),
    val filteredAccounts: List<Account> = emptyList(),
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
) : UiState