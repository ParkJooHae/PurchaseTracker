package kr.jhp.purchtrac.ui.screens.memo.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kr.jhp.purchtrac.domain.model.Memo
import kr.jhp.purchtrac.ui.components.EmptyContentView
import kr.jhp.purchtrac.ui.state.memo.MemoEvent
import kr.jhp.purchtrac.ui.state.memo.MemoIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoListScreen(
    viewModel: MemoViewModel = hiltViewModel(),
    navigateToMemoDetail: (Long?) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showSearchBar by remember { mutableStateOf(false) }
    var searchActive by rememberSaveable { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is MemoEvent.ShowToast -> {
                    // Toast 메시지 표시 로직
                }
                is MemoEvent.NavigateToMemoDetail -> {
                    navigateToMemoDetail(event.memoId)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("메모") },
                actions = {
                    // 검색 아이콘
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            imageVector = if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (showSearchBar) "Close Search" else "Search"
                        )
                    }
                    // 중요 메모만 보기 토글
                    IconButton(
                        onClick = {
                            viewModel.processIntent(
                                MemoIntent.SetFilterImportant(!state.showOnlyImportant)
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (state.showOnlyImportant)
                                Icons.Default.Star
                            else
                                Icons.Outlined.Star,
                            contentDescription = "Filter Important"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigateToDetail(null) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Memo")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 검색 바
            if (showSearchBar) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = { query ->
                        viewModel.processIntent(MemoIntent.SearchMemos(query))
                    },
                    onSearch = {
                        searchActive = false
                    },
                    active = searchActive,
                    onActiveChange = { searchActive = it },
                    placeholder = { Text("메모 검색...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.processIntent(MemoIntent.ClearSearchQuery)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear Query"
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 검색 결과를 여기에 표시할 수 있습니다 (선택사항)
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.filteredMemos.isEmpty()) {
                    // 빈 상태 UI
                    EmptyContentView(
                        modifier = Modifier.align(Alignment.Center),
                        icon = Icons.Default.Add,
                        title = if (state.showOnlyImportant) {
                            "중요 메모가 없습니다"
                        } else {
                            "메모가 없습니다"
                        },
                        description = if (state.searchQuery.isNotEmpty()) {
                            "검색 결과가 없습니다. 다른 검색어를 입력해보세요."
                        } else {
                            "+ 버튼을 눌러 메모를 추가하세요"
                        }
                    )
                } else {
                    // 메모 목록
                    MemoList(
                        memos = state.filteredMemos,
                        onMemoClick = { memoId ->
                            viewModel.navigateToDetail(memoId)
                        },
                        onToggleImportant = { memoId ->
                            viewModel.processIntent(MemoIntent.ToggleImportant(memoId))
                        }
                    )
                }

                // 에러 표시
                state.error?.let { error ->
                    Snackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                    ) {
                        Text(error)
                    }
                }
            }
        }
    }
}

@Composable
fun MemoList(
    memos: List<Memo>,
    onMemoClick: (Long) -> Unit,
    onToggleImportant: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = memos,
            key = { it.id }
        ) { memo ->
            MemoItem(
                memo = memo,
                onClick = { onMemoClick(memo.id) },
                onToggleImportant = { onToggleImportant(memo.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoItem(
    memo: Memo,
    onClick: () -> Unit,
    onToggleImportant: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = memo.title.ifEmpty { "제목 없음" },
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = memo.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onToggleImportant) {
                Icon(
                    imageVector = if (memo.isImportant) Icons.Default.Star else Icons.Outlined.Star,
                    contentDescription = "Toggle Important",
                    tint = if (memo.isImportant) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}