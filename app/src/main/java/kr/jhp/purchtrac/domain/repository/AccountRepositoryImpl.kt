package kr.jhp.purchtrac.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.jhp.purchtrac.data.local.dao.AccountDao
import kr.jhp.purchtrac.data.local.entity.AccountEntity
import kr.jhp.purchtrac.domain.model.Account
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(): Flow<List<Account>> {
        return accountDao.getAllAccounts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAccountsByUserId(userId: Long): Flow<List<Account>> {
        return accountDao.getAccountsByUserId(userId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getAccountById(id: Long): Account? {
        return accountDao.getAccountById(id)?.toDomainModel()
    }

    override fun searchAccounts(query: String): Flow<List<Account>> {
        return accountDao.searchAccounts(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun insertAccount(account: Account): Long {
        return accountDao.insertAccount(account.toEntity())
    }

    override suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account.toEntity())
    }

    override suspend fun deleteAccount(accountId: Long) {
        accountDao.getAccountById(accountId)?.let {
            accountDao.deleteAccount(it)
        }
    }

    // 확장 함수: Entity <-> Domain 모델 변환
    private fun AccountEntity.toDomainModel(): Account {
        return Account(
            id = this.id,
            userId = this.userId,
            siteName = this.siteName,
            siteUrl = this.siteUrl,
            username = this.username,
            password = this.password,
            notes = this.notes
        )
    }

    private fun Account.toEntity(): AccountEntity {
        return AccountEntity(
            id = this.id,
            userId = this.userId,
            siteName = this.siteName,
            siteUrl = this.siteUrl,
            username = this.username,
            password = this.password,
            notes = this.notes
        )
    }
}