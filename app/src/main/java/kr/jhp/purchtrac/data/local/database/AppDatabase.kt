package kr.jhp.purchtrac.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kr.jhp.purchtrac.data.local.dao.AccountDao
import kr.jhp.purchtrac.data.local.dao.MemoDao
import kr.jhp.purchtrac.data.local.dao.ProductDao
import kr.jhp.purchtrac.data.local.dao.RelationDao
import kr.jhp.purchtrac.data.local.dao.UserDao
import kr.jhp.purchtrac.data.local.entity.AccountEntity
import kr.jhp.purchtrac.data.local.entity.ProductEntity
import kr.jhp.purchtrac.data.local.entity.UserEntity
import kr.jhp.purchtrac.data.local.entity.MemoEntity

/**
 * Room 데이터베이스 클래스
 * 앱의 로컬 데이터베이스를 정의하고 관리합니다.
 */
@Database(
    entities = [
        UserEntity::class,
        AccountEntity::class,
        ProductEntity::class,
        MemoEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    // DAO 인터페이스 추상 메서드들
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun productDao(): ProductDao
    abstract fun memoDao(): MemoDao
    abstract fun relationDao(): RelationDao

    companion object {
        private const val DATABASE_NAME = "purchase_tracker_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

