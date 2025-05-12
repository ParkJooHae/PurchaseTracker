package kr.jhp.purchtrac.ui.state.memo

import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.ui.state.UiState

data class MemoState(
    val memos: List<Memo> = emptyList(),
    val filteredMemos: List<Memo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val showOnlyImportant: Boolean = false
) : UiState