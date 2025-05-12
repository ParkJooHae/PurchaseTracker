package kr.jhp.purchtrac.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jhp.purchtrac.domain.repository.MemoRepository
import kr.jhp.purchtrac.domain.repository.MemoRepositoryImpl
import kr.jhp.purchtrac.domain.repository.UserRepository
import kr.jhp.purchtrac.domain.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMemoRepository(memoRepositoryImpl: MemoRepositoryImpl): MemoRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

//    @Binds
//    @Singleton
//    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindAccountRepository(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository
//
//    @Binds
//    @Singleton
//    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}