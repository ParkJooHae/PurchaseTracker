package kr.jhp.purchtrac.ui.screens.account.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kr.jhp.purchtrac.ui.state.account.detail.AccountDetailEvent
import kr.jhp.purchtrac.ui.state.account.detail.AccountDetailIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailScreen(
    accountId: Long?,
    navigateBack: () -> Unit,
    viewModel: AccountDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(key1 = true) {
        if (accountId != null && accountId > 0) {
            viewModel.processIntent(AccountDetailIntent.LoadAccount(accountId))
        }

        viewModel.event.collectLatest { event ->
            when (event) {
                is AccountDetailEvent.ShowToast -> {
                    // Toast 메시지 표시 로직
                }
                is AccountDetailEvent.NavigateBack -> {
                    navigateBack()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNewAccount) "새 계정" else "계정 수정") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // 저장 버튼
                    IconButton(
                        onClick = {
                            viewModel.processIntent(AccountDetailIntent.SaveAccount)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }

                    // 메뉴 (삭제 등)
                    if (!state.isNewAccount) {
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
                        .verticalScroll(rememberScrollState())
                ) {
                    // 사이트 이름 입력
                    OutlinedTextField(
                        value = state.siteName,
                        onValueChange = { siteName ->
                            viewModel.processIntent(AccountDetailIntent.UpdateSiteName(siteName))
                        },
                        label = { Text("사이트 이름*") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 사이트 URL 입력
                    OutlinedTextField(
                        value = state.siteUrl,
                        onValueChange = { siteUrl ->
                            viewModel.processIntent(AccountDetailIntent.UpdateSiteUrl(siteUrl))
                        },
                        label = { Text("사이트 URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 사용자 이름 입력
                    OutlinedTextField(
                        value = state.username,
                        onValueChange = { username ->
                            viewModel.processIntent(AccountDetailIntent.UpdateUsername(username))
                        },
                        label = { Text("사용자 이름*") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 비밀번호 입력
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { password ->
                            viewModel.processIntent(AccountDetailIntent.UpdatePassword(password))
                        },
                        label = { Text("비밀번호*") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 메모 입력
                    OutlinedTextField(
                        value = state.notes,
                        onValueChange = { notes ->
                            viewModel.processIntent(AccountDetailIntent.UpdateNotes(notes))
                        },
                        label = { Text("메모") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "* 필수 입력 항목",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                title = { Text("계정 삭제") },
                text = { Text("이 계정을 삭제하시겠습니까?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.processIntent(AccountDetailIntent.DeleteAccount)
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