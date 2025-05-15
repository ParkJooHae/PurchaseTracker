package kr.jhp.purchtrac.ui.screens.memo.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kr.jhp.purchtrac.ui.state.memo.detail.MemoDetailEvent
import kr.jhp.purchtrac.ui.state.memo.detail.MemoDetailIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoDetailScreen(
    memoId: Long?,
    navigateBack: () -> Unit,
    viewModel: MemoDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(key1 = true) {
        if (memoId != null && memoId > 0) {
            viewModel.processIntent(MemoDetailIntent.LoadMemo(memoId))
        }

        viewModel.event.collectLatest { event ->
            when (event) {
                is MemoDetailEvent.ShowToast -> {
                    // Toast 메시지 표시 로직
                }
                is MemoDetailEvent.NavigateBack -> {
                    navigateBack()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNewMemo) "새 메모" else "메모 수정") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // 중요 표시 토글 버튼
                    IconButton(
                        onClick = {
                            viewModel.processIntent(MemoDetailIntent.ToggleImportant)
                        }
                    ) {
                        Icon(
                            imageVector = if (state.isImportant)
                                Icons.Default.Star
                            else
                                Icons.Outlined.Star,
                            contentDescription = "Toggle Important",
                            tint = if (state.isImportant)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // 저장 버튼
                    IconButton(
                        onClick = {
                            viewModel.processIntent(MemoDetailIntent.SaveMemo)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }

                    // 메뉴 (삭제 등)
                    if (!state.isNewMemo) {
                        IconButton(
                            onClick = { showDeleteConfirmDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // 제목 입력
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { title ->
                            viewModel.processIntent(MemoDetailIntent.UpdateTitle(title))
                        },
                        label = { Text("제목") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 내용 입력
                    OutlinedTextField(
                        value = state.content,
                        onValueChange = { content ->
                            viewModel.processIntent(MemoDetailIntent.UpdateContent(content))
                        },
                        label = { Text("내용") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(1f),
                        singleLine = false
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

                // 저장 중 표시
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        // 삭제 확인 다이얼로그
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = { Text("메모 삭제") },
                text = { Text("이 메모를 삭제하시겠습니까?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.processIntent(MemoDetailIntent.DeleteMemo)
                            showDeleteConfirmDialog = false
                        }
                    ) {
                        Text("삭제")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}