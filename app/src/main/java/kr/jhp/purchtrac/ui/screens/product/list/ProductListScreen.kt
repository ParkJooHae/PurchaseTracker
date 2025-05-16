package kr.jhp.purchtrac.ui.screens.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.ui.components.EmptyContentView
import kr.jhp.purchtrac.ui.state.product.ProductEvent
import kr.jhp.purchtrac.ui.state.product.ProductIntent
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    navigateToProductDetail: (Long?) -> Unit
) {
    val state by viewModel.state.collectAsState()
    var showSearchBar by remember { mutableStateOf(false) }
    var searchActive by rememberSaveable { mutableStateOf(false) }
    var showFilterMenu by remember { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(key1 = true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is ProductEvent.ShowToast -> {
                    // Toast 메시지 표시 로직
                }
                is ProductEvent.NavigateToProductDetail -> {
                    navigateToProductDetail(event.productId)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("예약 구매") },
                actions = {
                    // 필터 아이콘
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }

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
                Icon(Icons.Default.Add, contentDescription = "Add Product")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 선택된 필터 표시
            if (state.selectedStatus != null) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "상태: ${getStatusText(state.selectedStatus!!)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(
                            onClick = {
                                viewModel.processIntent(ProductIntent.FilterProductsByStatus(null))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Filter"
                            )
                        }
                    }
                }
            }

            // 검색 바
            if (showSearchBar) {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChange = { query ->
                        viewModel.processIntent(ProductIntent.SearchProducts(query))
                    },
                    onSearch = {
                        searchActive = false
                    },
                    active = searchActive,
                    onActiveChange = { searchActive = it },
                    placeholder = { Text("상품 검색...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search Icon"
                        )
                    },
                    trailingIcon = {
                        if (state.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.processIntent(ProductIntent.ClearSearchQuery)
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
                } else if (state.filteredProducts.isEmpty()) {
                    // 빈 상태 UI
                    EmptyContentView(
                        modifier = Modifier.align(Alignment.Center),
                        icon = Icons.Default.Add,
                        title = "예약 상품이 없습니다",
                        description = if (state.searchQuery.isNotEmpty()) {
                            "검색 결과가 없습니다. 다른 검색어를 입력해보세요."
                        } else {
                            "+ 버튼을 눌러 예약 상품을 추가하세요"
                        }
                    )
                } else {
                    // 상품 목록
                    ProductList(
                        products = state.filteredProducts,
                        onProductClick = { productId ->
                            viewModel.navigateToDetail(productId)
                        },
                        onToggleReminder = { productId ->
                            viewModel.processIntent(ProductIntent.ToggleReminderEnabled(productId))
                        }
                    )
                }

                // 필터 메뉴
                if (showFilterMenu) {
                    FilterDropdownMenu(
                        selectedStatus = state.selectedStatus,
                        onStatusSelected = { status ->
                            viewModel.processIntent(ProductIntent.FilterProductsByStatus(status))
                            showFilterMenu = false
                        },
                        onDismiss = { showFilterMenu = false }
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
fun FilterDropdownMenu(
    selectedStatus: ProductStatus?,
    onStatusSelected: (ProductStatus?) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss,
        modifier = Modifier.width(200.dp)
    ) {
        Text(
            text = "상태별 필터",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Divider()

        // 전체 항목
        DropdownMenuItem(
            text = { Text("전체") },
            onClick = { onStatusSelected(null) },
            trailingIcon = {
                if (selectedStatus == null) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected"
                    )
                }
            }
        )

        // 예정 상품 항목
        DropdownMenuItem(
            text = { Text("예정") },
            onClick = { onStatusSelected(ProductStatus.PLANNED) },
            trailingIcon = {
                if (selectedStatus == ProductStatus.PLANNED) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected"
                    )
                }
            }
        )

        // 구매 완료 항목
        DropdownMenuItem(
            text = { Text("구매 완료") },
            onClick = { onStatusSelected(ProductStatus.PURCHASED) },
            trailingIcon = {
                if (selectedStatus == ProductStatus.PURCHASED) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected"
                    )
                }
            }
        )

        // 취소 항목
        DropdownMenuItem(
            text = { Text("취소") },
            onClick = { onStatusSelected(ProductStatus.CANCELED) },
            trailingIcon = {
                if (selectedStatus == ProductStatus.CANCELED) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected"
                    )
                }
            }
        )
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (Long) -> Unit,
    onToggleReminder: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = products,
            key = { it.id }
        ) { product ->
            ProductItem(
                product = product,
                onClick = { onProductClick(product.id) },
                onToggleReminder = { onToggleReminder(product.id) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
    onToggleReminder: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
    val priceFormat = NumberFormat.getCurrencyInstance().apply {
        currency = Currency.getInstance("KRW")
    }

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
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 사이트 이름
                Text(
                    text = product.siteName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 가격
                Text(
                    text = priceFormat.format(product.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 발매일과 상태
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateFormat.format(Date(product.releaseDate)),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 상태 표시 (칩)
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = getStatusColor(product.status, MaterialTheme.colorScheme)
                    ) {
                        Text(
                            text = getStatusText(product.status),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // 알림 설정 토글 버튼
            IconButton(onClick = onToggleReminder) {
                Icon(
                    imageVector = if (product.reminderEnabled)
                        Icons.Default.Notifications
                    else
                        Icons.Outlined.NotificationsOff,
                    contentDescription = "Toggle Reminder",
                    tint = if (product.reminderEnabled)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// 상태 텍스트 반환 함수
@Composable
fun getStatusText(status: ProductStatus): String {
    return when (status) {
        ProductStatus.PLANNED -> "예정"
        ProductStatus.PURCHASED -> "구매 완료"
        ProductStatus.CANCELED -> "취소"
    }
}

// 상태별 색상 반환 함수
@Composable
fun getStatusColor(status: ProductStatus, colorScheme: ColorScheme): androidx.compose.ui.graphics.Color {
    return when (status) {
        ProductStatus.PLANNED -> colorScheme.primaryContainer
        ProductStatus.PURCHASED -> colorScheme.secondaryContainer
        ProductStatus.CANCELED -> colorScheme.errorContainer
    }
}