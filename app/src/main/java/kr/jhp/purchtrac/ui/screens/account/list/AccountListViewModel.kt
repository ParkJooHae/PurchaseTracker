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
import kr.jhp.purchtrac.ui.state.account.AccountListEvent
import kr.jhp.purchtrac.ui.state.account.AccountListIntent
import kr.jhp.purchtrac.ui.state.account.AccountListState
import javax.inject.Inject

@HiltViewModel
class AccountListViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AccountListState())
    val state: StateFlow<AccountListState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<AccountListEvent>()
    val event: SharedFlow<AccountListEvent> = _event.asSharedFlow()

    init {
        // 인증이 필요하므로 처음에는 계정을 불러오지 않음
        // 인증 성공 후 loadAccounts() 호출
    }

    fun processIntent(intent: AccountListIntent) {
        when (intent) {
            is AccountListIntent.LoadAccounts -> loadAccounts()
            is AccountListIntent.SearchAccounts -> searchAccounts(intent.query)
            is AccountListIntent.DeleteAccount -> deleteAccount(intent.accountId)
            is AccountListIntent.ClearSearchQuery -> clearSearchQuery()
            is AccountListIntent.Authenticate -> requestAuthentication()
            is AccountListIntent.AuthenticationSucceeded -> authenticationSucceeded()
            is AccountListIntent.AuthenticationFailed -> authenticationFailed()
        }
    }

    private fun loadAccounts() {
        if (!_state.value.isAuthenticated) {
            viewModelScope.launch {
                _event.emit(AccountListEvent.RequireAuthentication)
            }
            return
        }

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
                updateAccountsList(accounts)
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
                _event.emit(AccountListEvent.ShowToast("계정이 삭제되었습니다"))
            } catch (e: Exception) {
                _event.emit(AccountListEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun updateAccountsList(accounts: List<Account>) {
        _state.update { it.copy(accounts = accounts) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _state.value
        val filteredList = state.accounts.filter { account ->
            // 검색어 필터
            state.searchQuery.isEmpty() ||
                    account.siteName.contains(state.searchQuery, ignoreCase = true) ||
                    account.username.contains(state.searchQuery, ignoreCase = true)
        }

        _state.update { it.copy(filteredAccounts = filteredList) }
    }

    private fun requestAuthentication() {
        viewModelScope.launch {
            _event.emit(AccountListEvent.RequireAuthentication)
        }
    }

    private fun authenticationSucceeded() {
        _state.update { it.copy(isAuthenticated = true) }
        loadAccounts()
    }

    private fun authenticationFailed() {
        _state.update { it.copy(isAuthenticated = false) }
        viewModelScope.launch {
            _event.emit(AccountListEvent.ShowToast("인증 실패: 계정 목록을 볼 수 없습니다"))
            _event.emit(AccountListEvent.NavigateBack)
        }
    }

    fun navigateToDetail(accountId: Long?) {
        viewModelScope.launch {
            _event.emit(AccountListEvent.NavigateToAccountDetail(accountId))
        }
    }
}