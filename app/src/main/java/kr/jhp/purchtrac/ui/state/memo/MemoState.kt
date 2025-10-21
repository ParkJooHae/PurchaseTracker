package kr.jhp.purchtrac.ui.state.memo

import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.domain.model.User
import kr.jhp.purchtrac.ui.state.UiState

data class MemoState(
    val memos: List<Memo> = emptyList(),
    val filteredMemos: List<Memo> = emptyList(),
    val allUsers: List<User> = emptyList(),
    val currentUserId: Long = 1L,
    val isLoading: Boolean = false,
    val isLoadingUsers: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val showOnlyImportant: Boolean = false
) : UiState