package kr.jhp.purchtrac.ui.state.memo.detail

import kr.jhp.purchtrac.ui.state.UiState

data class MemoDetailState(
    val memoId: Long? = null,
    val userId: Long = 1, // 기본값으로 1 (필요에 따라 변경)
    val title: String = "",
    val content: String = "",
    val isImportant: Boolean = false,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isNewMemo: Boolean = true,
    val error: String? = null
) : UiState