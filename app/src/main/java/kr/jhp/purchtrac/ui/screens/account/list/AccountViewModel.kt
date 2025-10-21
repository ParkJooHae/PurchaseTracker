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
import kr.jhp.purchtrac.domain.error.ErrorHandler
import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.domain.usecase.account.DeleteAccountUseCase
import kr.jhp.purchtrac.domain.usecase.account.GetAccountsUseCase
import kr.jhp.purchtrac.domain.usecase.user.CreateUserUseCase
import kr.jhp.purchtrac.domain.usecase.user.GetAllUsersUseCase
import kr.jhp.purchtrac.ui.state.account.AccountEvent
import kr.jhp.purchtrac.ui.state.account.AccountIntent
import kr.jhp.purchtrac.ui.state.account.AccountState
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getAccountsUseCase: GetAccountsUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<AccountEvent>()
    val event: SharedFlow<AccountEvent> = _event.asSharedFlow()

    init {
        processIntent(AccountIntent.LoadUsers)
        processIntent(AccountIntent.LoadAccounts)
    }

    fun processIntent(intent: AccountIntent) {
        when (intent) {
            is AccountIntent.LoadAccounts -> loadAccounts()
            is AccountIntent.LoadUsers -> loadUsers()
            is AccountIntent.SearchAccounts -> searchAccounts(intent.query)
            is AccountIntent.DeleteAccount -> deleteAccount(intent.accountId)
            is AccountIntent.ClearSearchQuery -> clearSearchQuery()
            is AccountIntent.UpdateAccount -> updateAccountList(intent.account)
            is AccountIntent.SelectUser -> selectUser(intent.userId)
            is AccountIntent.CreateUser -> createUser(intent.name)
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAccountsUseCase().catch { e ->
                val userMessage = ErrorHandler.getUserMessage(e)
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = userMessage
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
                val userMessage = ErrorHandler.getUserMessage(e)
                _event.emit(AccountEvent.ShowToast(userMessage))
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

    private fun loadUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingUsers = true) }
            getAllUsersUseCase().catch { e ->
                val userMessage = ErrorHandler.getUserMessage(e)
                _state.update { state ->
                    state.copy(
                        isLoadingUsers = false,
                        error = userMessage
                    )
                }
            }.collect { users ->
                _state.update { state ->
                    state.copy(
                        allUsers = users,
                        isLoadingUsers = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun selectUser(userId: Long) {
        _state.update { it.copy(currentUserId = userId) }
        applyFilters()
    }

    private fun createUser(name: String) {
        viewModelScope.launch {
            try {
                createUserUseCase(name)
                // 사용자 목록 새로고침
                processIntent(AccountIntent.LoadUsers)
                _event.emit(AccountEvent.ShowToast("사용자 '$name'이(가) 추가되었습니다"))
            } catch (e: Exception) {
                val userMessage = ErrorHandler.getUserMessage(e)
                _event.emit(AccountEvent.ShowToast(userMessage))
            }
        }
    }

    private fun applyFilters() {
        val state = _state.value
        val filteredList = state.accounts
            .filter { account -> account.userId == state.currentUserId }
            .filter { account ->
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