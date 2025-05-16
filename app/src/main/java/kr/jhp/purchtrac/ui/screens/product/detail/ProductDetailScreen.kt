package kr.jhp.purchtrac.ui.screens.product.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kr.jhp.purchtrac.domain.model.ProductStatus
import kr.jhp.purchtrac.ui.components.YearMonthPickerDialog
import kr.jhp.purchtrac.ui.state.product.detail.ProductDetailEvent
import kr.jhp.purchtrac.ui.state.product.detail.ProductDetailIntent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Long?,
    navigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showStatusMenu by remember { mutableStateOf(false) }
    var showReleaseDatePicker by remember { mutableStateOf(false) }
    var showPurchaseDatePicker by remember { mutableStateOf(false) }

    // 이벤트 처리
    LaunchedEffect(key1 = true) {
        if (productId != null && productId > 0) {
            viewModel.processIntent(ProductDetailIntent.LoadProduct(productId))
        }

        viewModel.event.collectLatest { event ->
            when (event) {
                is ProductDetailEvent.ShowToast -> {
                    // Toast 메시지 표시 로직
                }
                is ProductDetailEvent.NavigateBack -> {
                    navigateBack()
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isNewProduct) "새 예약 상품" else "예약 상품 수정") },
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
                            viewModel.processIntent(ProductDetailIntent.SaveProduct)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save"
                        )
                    }

                    // 삭제 버튼 (기존 상품인 경우만)
                    if (!state.isNewProduct) {
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
                    // 상품명 입력
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { name ->
                            viewModel.processIntent(ProductDetailIntent.UpdateName(name))
                        },
                        label = { Text("상품명*") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 설명 입력
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { description ->
                            viewModel.processIntent(ProductDetailIntent.UpdateDescription(description))
                        },
                        label = { Text("설명") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        singleLine = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 가격 입력
                    OutlinedTextField(
                        value = state.price,
                        onValueChange = { price ->
                            viewModel.processIntent(ProductDetailIntent.UpdatePrice(price))
                        },
                        label = { Text("가격") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        prefix = { Text("₩") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 사이트 이름 입력
                    OutlinedTextField(
                        value = state.siteName,
                        onValueChange = { siteName ->
                            viewModel.processIntent(ProductDetailIntent.UpdateSiteName(siteName))
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
                            viewModel.processIntent(ProductDetailIntent.UpdateSiteUrl(siteUrl))
                        },
                        label = { Text("사이트 URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 이미지 URL 입력
                    OutlinedTextField(
                        value = state.imageUrl,
                        onValueChange = { imageUrl ->
                            viewModel.processIntent(ProductDetailIntent.UpdateImageUrl(imageUrl))
                        },
                        label = { Text("이미지 URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 발매일 선택
                    val releaseDateFormat = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "발매일*",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = releaseDateFormat.format(Date(state.releaseDate)),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            IconButton(onClick = { showReleaseDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Select Date"
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 구매일 선택 (선택 사항)
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "구매일 (선택)",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = if (state.purchaseDate != null)
                                        releaseDateFormat.format(Date(state.purchaseDate!!))
                                    else
                                        "구매일 선택",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Row {
                                if (state.purchaseDate != null) {
                                    IconButton(onClick = {
                                        viewModel.processIntent(ProductDetailIntent.UpdatePurchaseDate(null))
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear Date"
                                        )
                                    }
                                }
                                IconButton(onClick = { showPurchaseDatePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Select Date"
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 상태 선택
                    OutlinedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "상태",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = when (state.status) {
                                        ProductStatus.PLANNED -> "예정"
                                        ProductStatus.PURCHASED -> "구매 완료"
                                        ProductStatus.CANCELED -> "취소"
                                    },
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Box {
                                IconButton(onClick = { showStatusMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Status"
                                    )
                                }
                                DropdownMenu(
                                    expanded = showStatusMenu,
                                    onDismissRequest = { showStatusMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("예정") },
                                        onClick = {
                                            viewModel.processIntent(ProductDetailIntent.UpdateStatus(ProductStatus.PLANNED))
                                            showStatusMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("구매 완료") },
                                        onClick = {
                                            viewModel.processIntent(ProductDetailIntent.UpdateStatus(ProductStatus.PURCHASED))
                                            showStatusMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("취소") },
                                        onClick = {
                                            viewModel.processIntent(ProductDetailIntent.UpdateStatus(ProductStatus.CANCELED))
                                            showStatusMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // 알림 설정
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "발매일 알림",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = state.reminderEnabled,
                            onCheckedChange = {
                                viewModel.processIntent(ProductDetailIntent.UpdateReminderEnabled(it))
                            }
                        )
                    }

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

        // 발매일 선택 다이얼로그
        if (showReleaseDatePicker) {
            YearMonthPickerDialog(
                initialDate = state.releaseDate,
                onDateSelected = { date ->
                    viewModel.processIntent(ProductDetailIntent.UpdateReleaseDate(date))
                },
                onDismissRequest = { showReleaseDatePicker = false }
            )
        }

        // 구매일 선택 다이얼로그
        if (showPurchaseDatePicker) {
            YearMonthPickerDialog(
                initialDate = state.purchaseDate ?: System.currentTimeMillis(),
                onDateSelected = { date ->
                    viewModel.processIntent(ProductDetailIntent.UpdatePurchaseDate(date))
                },
                onDismissRequest = { showPurchaseDatePicker = false }
            )
        }

        // 삭제 확인 다이얼로그
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = { Text("상품 삭제") },
                text = { Text("이 예약 상품을 삭제하시겠습니까?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.processIntent(ProductDetailIntent.DeleteProduct)
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