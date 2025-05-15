package kr.jhp.purchtrac.ui.screens.account.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.domain.usecase.account.DeleteAccountUseCase
import kr.jhp.purchtrac.domain.usecase.account.GetAccountsUseCase
import kr.jhp.purchtrac.ui.state.account.AccountEvent
import kr.jhp.purchtrac.ui.state.account.AccountIntent
import kr.jhp.purchtrac.ui.state.account.AccountState
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<AccountEvent>()
    val event: SharedFlow<AccountEvent> = _event.asSharedFlow()

    init {
        processIntent(AccountIntent.LoadAccounts)
    }

    fun processIntent(intent: AccountIntent) {
        when (intent) {
            is AccountIntent.LoadAccounts -> loadAccounts()
            is AccountIntent.SearchAccounts -> searchAccounts(intent.query)
            is AccountIntent.DeleteAccount -> deleteAccount(intent.accountId)
            is AccountIntent.ClearSearchQuery -> clearSearchQuery()
            is AccountIntent.UpdateAccount -> updateAccountList(intent.account)
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAccountsUseCase().catch { e ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }.collect { accounts ->
                updateAccountList(accounts)
                _state.update { state ->
                    state.copy(isLoading = false, error = null)
                }
            }
        }
    }

    private fun searchAccounts(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun clearSearchQuery() {
        _state.update { it.copy(searchQuery = "") }
        applyFilters()
    }

    private fun deleteAccount(accountId: Long) {
        viewModelScope.launch {
            try {
                deleteAccountUseCase(accountId)
                _event.emit(AccountEvent.ShowToast("계정이 삭제되었습니다"))
            } catch (e: Exception) {
                _event.emit(AccountEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun updateAccountList(accounts: List<Account>) {
        _state.update { it.copy(accounts = accounts) }
        applyFilters()
    }

    private fun updateAccountList(account: Account) {
        val currentAccounts = _state.value.accounts.toMutableList()
        val index = currentAccounts.indexOfFirst { it.id == account.id }

        if (index != -1) {
            currentAccounts[index] = account
        } else {
            currentAccounts.add(account)
        }

        _state.update { it.copy(accounts = currentAccounts) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _state.value
        val filteredList = state.accounts.filter { account ->
            // 검색어 필터
            state.searchQuery.isEmpty() ||
                    account.siteName.contains(state.searchQuery, ignoreCase = true) ||
                    account.username.contains(state.searchQuery, ignoreCase = true) ||
                    account.siteUrl.contains(state.searchQuery, ignoreCase = true)
        }

        _state.update { it.copy(filteredAccounts = filteredList) }
    }

    fun navigateToDetail(accountId: Long?) {
        viewModelScope.launch {
            _event.emit(AccountEvent.NavigateToAccountDetail(accountId))
        }
    }
}