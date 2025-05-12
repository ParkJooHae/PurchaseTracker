package kr.jhp.purchtrac.ui.screens.memo.detail

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
import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.domain.usecase.memo.DeleteMemoUseCase
import kr.jhp.purchtrac.domain.usecase.memo.GetMemoByIdUseCase
import kr.jhp.purchtrac.domain.usecase.memo.SaveMemoUseCase
import kr.jhp.purchtrac.ui.state.memo.detail.MemoDetailEvent
import kr.jhp.purchtrac.ui.state.memo.detail.MemoDetailIntent
import kr.jhp.purchtrac.ui.state.memo.detail.MemoDetailState
import javax.inject.Inject

@HiltViewModel
class MemoDetailViewModel @Inject constructor(
    private val getMemoByIdUseCase: GetMemoByIdUseCase,
    private val saveMemoUseCase: SaveMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(MemoDetailState())
    val state: StateFlow<MemoDetailState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<MemoDetailEvent>()
    val event: SharedFlow<MemoDetailEvent> = _event.asSharedFlow()

    init {
        // 메모 ID가 있으면 메모 로드
        savedStateHandle.get<Long>("memoId")?.let { memoId ->
            if (memoId != -1L) {
                processIntent(MemoDetailIntent.LoadMemo(memoId))
            } else {
                _state.update { it.copy(isNewMemo = true) }
            }
        }
    }

    fun processIntent(intent: MemoDetailIntent) {
        when (intent) {
            is MemoDetailIntent.LoadMemo -> loadMemo(intent.memoId)
            is MemoDetailIntent.UpdateTitle -> updateTitle(intent.title)
            is MemoDetailIntent.UpdateContent -> updateContent(intent.content)
            is MemoDetailIntent.ToggleImportant -> toggleImportant()
            is MemoDetailIntent.SaveMemo -> saveMemo()
            is MemoDetailIntent.DeleteMemo -> deleteMemo()
        }
    }

    private fun loadMemo(memoId: Long?) {
        if (memoId == null || memoId <= 0) {
            _state.update { it.copy(isNewMemo = true) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                val memo = getMemoByIdUseCase(memoId)
                if (memo != null) {
                    _state.update { state ->
                        state.copy(
                            memoId = memo.id,
                            userId = memo.userId,
                            title = memo.title,
                            content = memo.content,
                            isImportant = memo.isImportant,
                            isLoading = false,
                            isNewMemo = false,
                            error = null
                        )
                    }
                } else {
                    // 해당 ID의 메모를 찾지 못함 (새 메모로 처리)
                    _state.update { it.copy(
                        isLoading = false,
                        isNewMemo = true,
                        error = "메모를 찾을 수 없습니다"
                    ) }
                    _event.emit(MemoDetailEvent.ShowToast("메모를 찾을 수 없습니다"))
                }
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
                _event.emit(MemoDetailEvent.ShowToast("오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun updateTitle(title: String) {
        _state.update { it.copy(title = title) }
    }

    private fun updateContent(content: String) {
        _state.update { it.copy(content = content) }
    }

    private fun toggleImportant() {
        _state.update { it.copy(isImportant = !it.isImportant) }
    }

    private fun saveMemo() {
        val currentState = _state.value

        // 제목이나 내용이 비어있는지 확인
        if (currentState.title.isBlank() && currentState.content.isBlank()) {
            viewModelScope.launch {
                _event.emit(MemoDetailEvent.ShowToast("제목이나 내용을 입력해주세요"))
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            try {
                val userId = 1L

                val memo = Memo(
                    id = currentState.memoId ?: 0,
                    userId = currentState.userId,
                    title = currentState.title,
                    content = currentState.content,
                    isImportant = currentState.isImportant,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                val savedId = saveMemoUseCase(memo)
                _state.update { it.copy(
                    memoId = savedId,
                    isSaving = false,
                    isNewMemo = false
                ) }

                _event.emit(MemoDetailEvent.MemoSaved)
                _event.emit(MemoDetailEvent.ShowToast("메모가 저장되었습니다"))
                _event.emit(MemoDetailEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isSaving = false,
                    error = e.message
                ) }
                _event.emit(MemoDetailEvent.ShowToast("저장 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }

    private fun deleteMemo() {
        val memoId = _state.value.memoId ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            try {
                deleteMemoUseCase(memoId)
                _state.update { it.copy(isLoading = false) }
                _event.emit(MemoDetailEvent.MemoDeleted)
                _event.emit(MemoDetailEvent.ShowToast("메모가 삭제되었습니다"))
                _event.emit(MemoDetailEvent.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    error = e.message
                ) }
                _event.emit(MemoDetailEvent.ShowToast("삭제 중 오류가 발생했습니다: ${e.message}"))
            }
        }
    }
}