package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.jhp.purchtrac.data.local.dao.ProductDao
import kr.jhp.purchtrac.data.local.entity.ProductEntity
import kr.jhp.purchtrac.domain.model.Product
import kr.jhp.purchtrac.domain.model.ProductStatus
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getProductsByUserId(userId: Long): Flow<List<Product>> {
        return productDao.getProductsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getProductsByStatus(status: ProductStatus): Flow<List<Product>> {
        return productDao.getProductsByStatus(status.name).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getProductsByDateRange(startDate: Long, endDate: Long): Flow<List<Product>> {
        return productDao.getProductsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)?.toDomainModel()
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product.toEntity())
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }

    override suspend fun deleteProduct(productId: Long) {
        productDao.getProductById(productId)?.let {
            productDao.deleteProduct(it)
        }
    }

    // 확장 함수: Entity <-> Domain 모델 변환
    private fun ProductEntity.toDomainModel(): Product {
        return Product(
            id = this.id,
            userId = this.userId,
            name = this.name,
            description = this.description,
            price = this.price,
            siteName = this.siteName,
            siteUrl = this.siteUrl,
            imageUrl = this.imageUrl,
            releaseDate = this.releaseDate,
            purchaseDate = this.purchaseDate,
            reminderEnabled = this.reminderEnabled,
            reminderTime = this.reminderTime,
            status = ProductStatus.valueOf(this.status),
            created = this.created,
            updated = this.updated
        )
    }

    private fun Product.toEntity(): ProductEntity {
        return ProductEntity(
            id = this.id,
            userId = this.userId,
            name = this.name,
            description = this.description,
            price = this.price,
            siteName = this.siteName,
            siteUrl = this.siteUrl,
            imageUrl = this.imageUrl,
            releaseDate = this.releaseDate,
            purchaseDate = this.purchaseDate,
            reminderEnabled = this.reminderEnabled,
            reminderTime = this.reminderTime,
            status = this.status.name,
            created = this.created,
            updated = this.updated
        )
    }
}