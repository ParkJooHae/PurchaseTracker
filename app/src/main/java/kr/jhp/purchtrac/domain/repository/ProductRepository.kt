package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByUserId(userId: Long): Flow<List<Product>>
    fun getProductsByStatus(status: ProductStatus): Flow<List<Product>>
    fun getProductsByDateRange(startDate: Long, endDate: Long): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productId: Long)
}