package kr.jhp.purchtrac.ui.screens.memo.list

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
import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.domain.usecase.memo.DeleteMemoUseCase
import kr.jhp.purchtrac.domain.usecase.memo.GetMemosUseCase
import kr.jhp.purchtrac.domain.usecase.memo.ToggleMemoImportanceUseCase
import kr.jhp.purchtrac.ui.state.memo.MemoEvent
import kr.jhp.purchtrac.ui.state.memo.MemoIntent
import kr.jhp.purchtrac.ui.state.memo.MemoState
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val getMemosUseCase: GetMemosUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    private val toggleMemoImportanceUseCase: ToggleMemoImportanceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MemoState())
    val state: StateFlow<MemoState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<MemoEvent>()
    val event: SharedFlow<MemoEvent> = _event.asSharedFlow()

    init {
        processIntent(MemoIntent.LoadMemos)
    }

    fun processIntent(intent: MemoIntent) {
        when (intent) {
            is MemoIntent.LoadMemos -> loadMemos()
            is MemoIntent.SearchMemos -> searchMemos(intent.query)
            is MemoIntent.ToggleImportant -> toggleImportant(intent.memoId)
            is MemoIntent.DeleteMemo -> deleteMemo(intent.memoId)
            is MemoIntent.SetFilterImportant -> filterImportant(intent.showOnlyImportant)
            is MemoIntent.ClearSearchQuery -> clearSearchQuery()
            is MemoIntent.UpdateMemo -> updateMemosList(intent.memo)
        }
    }

    private fun loadMemos() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getMemosUseCase().catch { e ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error occurred"
                    )
                }
            }.collect { memos ->
                updateMemosList(memos)
                _state.update { state ->
                    state.copy(isLoading = false, error = null)
                }
            }
        }
    }

    private fun searchMemos(query: String) {
        _state.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    private fun clearSearchQuery() {
        _state.update { it.copy(searchQuery = "") }
        applyFilters()
    }

    private fun toggleImportant(memoId: Long) {
        viewModelScope.launch {
            try {
                toggleMemoImportanceUseCase(memoId)
                _event.emit(MemoEvent.ShowToast("메모 중요도를 변경했습니다"))
            } catch (e: Exception) {
                _event.emit(MemoEvent.ShowToast("오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun deleteMemo(memoId: Long) {
        viewModelScope.launch {
            try {
                deleteMemoUseCase(memoId)
                _event.emit(MemoEvent.ShowToast("메모가 삭제되었습니다"))
            } catch (e: Exception) {
                _event.emit(MemoEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun filterImportant(showOnlyImportant: Boolean) {
        _state.update { it.copy(showOnlyImportant = showOnlyImportant) }
        applyFilters()
    }

    private fun updateMemosList(memos: List<Memo>) {
        _state.update { it.copy(memos = memos) }
        applyFilters()
    }

    private fun updateMemosList(memo: Memo) {
        val currentMemos = _state.value.memos.toMutableList()
        val index = currentMemos.indexOfFirst { it.id == memo.id }

        if (index != -1) {
            currentMemos[index] = memo
        } else {
            currentMemos.add(memo)
        }

        _state.update { it.copy(memos = currentMemos) }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _state.value
        val filteredList = state.memos.filter { memo ->
            // 검색어 필터
            val matchesSearch = state.searchQuery.isEmpty() ||
                    memo.title.contains(state.searchQuery, ignoreCase = true) ||
                    memo.content.contains(state.searchQuery, ignoreCase = true)

            // 중요 메모 필터
            val matchesImportant = !state.showOnlyImportant || memo.isImportant

            matchesSearch && matchesImportant
        }

        _state.update { it.copy(filteredMemos = filteredList) }
    }

    fun navigateToDetail(memoId: Long?) {
        viewModelScope.launch {
            _event.emit(MemoEvent.NavigateToMemoDetail(memoId))
        }
    }
}