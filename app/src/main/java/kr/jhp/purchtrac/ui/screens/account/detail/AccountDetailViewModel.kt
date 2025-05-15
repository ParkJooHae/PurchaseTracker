package kr.jhp.purchtrac.ui.screens.account.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.domain.usecase.account.DeleteAccountUseCase
import kr.jhp.purchtrac.domain.usecase.account.GetAccountByIdUseCase
import kr.jhp.purchtrac.domain.usecase.account.SaveAccountUseCase
import kr.jhp.purchtrac.ui.state.account.detail.AccountDetailEvent
import kr.jhp.purchtrac.ui.state.account.detail.AccountDetailIntent
import kr.jhp.purchtrac.ui.state.account.detail.AccountDetailState
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val saveAccountUseCase: SaveAccountUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AccountDetailState())
    val state: StateFlow<AccountDetailState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<AccountDetailEvent>()
    val event: SharedFlow<AccountDetailEvent> = _event.asSharedFlow()

    init {
        // 계정 ID가 있으면 계정 로드
        savedStateHandle.get<Long>("accountId")?.let { accountId ->
            if (accountId != -1L) {
                processIntent(AccountDetailIntent.LoadAccount(accountId))
            } else {
                _state.update { it.copy(isNewAccount = true) }
            }
        }
    }

    fun processIntent(intent: AccountDetailIntent) {
        when (intent) {
            is AccountDetailIntent.LoadAccount -> loadAccount(intent.accountId)
            is AccountDetailIntent.UpdateSiteName -> updateSiteName(intent.siteName)
            is AccountDetailIntent.UpdateSiteUrl -> updateSiteUrl(intent.siteUrl)
            is AccountDetailIntent.UpdateUsername -> updateUsername(intent.username)
            is AccountDetailIntent.UpdatePassword -> updatePassword(intent.password)
            is AccountDetailIntent.UpdateNotes -> updateNotes(intent.notes)
            is AccountDetailIntent.TogglePasswordVisibility -> togglePasswordVisibility()
            is AccountDetailIntent.SaveAccount -> saveAccount()
            is AccountDetailIntent.DeleteAccount -> deleteAccount()
        }
    }

    private fun loadAccount(accountId: Long?) {
        if (accountId == null || accountId <= 0) {
            _state.update { it.copy(isNewAccount = true) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val account = getAccountByIdUseCase(accountId)
                if (account != null) {
                    _state.update { state ->
                        state.copy(
                            accountId = account.id,
                            userId = account.userId,
                            siteName = account.siteName,
                            siteUrl = account.siteUrl,
                            username = account.username,
                            password = account.password,
                            notes = account.notes ?: "",
                            isLoading = false,
                            isNewAccount = false,
                            error = null
                        )
                    }
                } else {
                    // 해당 ID의 계정을 찾지 못함 (새 계정으로 처리)
                    _state.update { it.copy(
                        isLoading = false,
                        isNewAccount = true,
                        error = "계정을 찾을 수 없습니다"
                    ) }
                    _event.emit(AccountDetailEvent.ShowToast("계정을 찾을 수 없습니다"))
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
                _event.emit(AccountDetailEvent.ShowToast("오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun updateSiteName(siteName: String) {
        _state.update { it.copy(siteName = siteName) }
    }

    private fun updateSiteUrl(siteUrl: String) {
        _state.update { it.copy(siteUrl = siteUrl) }
    }

    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun updateNotes(notes: String) {
        _state.update { it.copy(notes = notes) }
    }

    private fun togglePasswordVisibility() {
        _state.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    private fun saveAccount() {
        val currentState = _state.value

        // 필수 필드 검증
        if (currentState.siteName.isBlank() || currentState.username.isBlank() || currentState.password.isBlank()) {
            viewModelScope.launch {
                _event.emit(AccountDetailEvent.ShowToast("사이트 이름, 사용자 이름, 비밀번호는 필수 입력사항입니다"))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            try {
                val account = Account(
                    id = currentState.accountId ?: 0,
                    userId = currentState.userId,
                    siteName = currentState.siteName,
                    siteUrl = currentState.siteUrl,
                    username = currentState.username,
                    password = currentState.password,
                    notes = if (currentState.notes.isBlank()) null else currentState.notes
                )

                val savedId = saveAccountUseCase(account)
                _state.update { it.copy(
                    accountId = savedId,
                    isSaving = false,
                    isNewAccount = false
                ) }

                _event.emit(AccountDetailEvent.AccountSaved)
                _event.emit(AccountDetailEvent.ShowToast("계정이 저장되었습니다"))
                _event.emit(AccountDetailEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isSaving = false,
                    error = e.message
                ) }
                _event.emit(AccountDetailEvent.ShowToast("저장 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun deleteAccount() {
        val accountId = _state.value.accountId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                deleteAccountUseCase(accountId)
                _state.update { it.copy(isLoading = false) }
                _event.emit(AccountDetailEvent.AccountDeleted)
                _event.emit(AccountDetailEvent.ShowToast("계정이 삭제되었습니다"))
                _event.emit(AccountDetailEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
                _event.emit(AccountDetailEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }
}