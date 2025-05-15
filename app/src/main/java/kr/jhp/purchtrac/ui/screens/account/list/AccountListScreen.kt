package kr.jhp.purchtrac.ui.screens.account.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.ui.components.EmptyContentView
import kr.jhp.purchtrac.ui.state.account.AccountEvent
import kr.jhp.purchtrac.ui.state.account.AccountIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    navigateToAccountDetail: (Long?) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showSearchBar by remember { mutableStateOf(false) }
    var searchActive by rememberSaveable { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is AccountEvent.ShowToast -> {
                    // Toast 메시지 표시 로직
                }
                is AccountEvent.NavigateToAccountDetail -> {
                    navigateToAccountDetail(event.accountId)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("계정") },
                actions = {
                    // 검색 아이콘
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            imageVector = if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = if (showSearchBar) "Close Search" else "Search"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.navigateToDetail(null) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
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
                        viewModel.processIntent(AccountIntent.SearchAccounts(query))
                    },
                    onSearch = {
                        searchActive = false
                    },
                    active = searchActive,
                    onActiveChange = { searchActive = it },
                    placeholder = { Text("계정 검색...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.processIntent(AccountIntent.ClearSearchQuery)
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
                } else if (state.filteredAccounts.isEmpty()) {
                    // 빈 상태 UI
                    EmptyContentView(
                        modifier = Modifier.align(Alignment.Center),
                        icon = Icons.Default.Add,
                        title = "계정이 없습니다",
                        description = if (state.searchQuery.isNotEmpty()) {
                            "검색 결과가 없습니다. 다른 검색어를 입력해보세요."
                        } else {
                            "+ 버튼을 눌러 계정을 추가하세요"
                        }
                    )
                } else {
                    // 계정 목록
                    AccountList(
                        accounts = state.filteredAccounts,
                        onAccountClick = { accountId ->
                            viewModel.navigateToDetail(accountId)
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
fun AccountList(
    accounts: List<Account>,
    onAccountClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = accounts,
            key = { it.id }
        ) { account ->
            AccountItem(
                account = account,
                onClick = { onAccountClick(account.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountItem(
    account: Account,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 사이트 이름
            Text(
                text = account.siteName,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 사용자 이름
            Row {
                Text(
                    text = "사용자 이름: ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = account.username,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 사이트 URL이 있으면 표시
            if (account.siteUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = account.siteUrl,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}