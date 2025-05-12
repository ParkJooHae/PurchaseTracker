package kr.jhp.purchtrac.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.data.local.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY releaseDate ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE userId = :userId ORDER BY releaseDate ASC")
    fun getProductsByUserId(userId: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE status = :status ORDER BY releaseDate ASC")
    fun getProductsByStatus(status: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE releaseDate BETWEEN :startDate AND :endDate ORDER BY releaseDate ASC")
    fun getProductsByDateRange(startDate: Long, endDate: Long): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR siteName LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}