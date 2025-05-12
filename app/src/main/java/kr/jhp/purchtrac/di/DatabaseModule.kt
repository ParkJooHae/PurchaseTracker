package kr.jhp.purchtrac.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.jhp.purchtrac.data.local.dao.AccountDao
import kr.jhp.purchtrac.data.local.dao.MemoDao
import kr.jhp.purchtrac.data.local.dao.ProductDao
import kr.jhp.purchtrac.data.local.dao.RelationDao
import kr.jhp.purchtrac.data.local.dao.UserDao
import kr.jhp.purchtrac.data.local.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.accountDao()
    }

    @Provides
    fun provideProductDao(appDatabase: AppDatabase): ProductDao {
        return appDatabase.productDao()
    }

    @Provides
    fun provideMemoDao(appDatabase: AppDatabase): MemoDao {
        return appDatabase.memoDao()
    }

    @Provides
    fun provideRelationDao(appDatabase: AppDatabase): RelationDao {
        return appDatabase.relationDao()
    }
}