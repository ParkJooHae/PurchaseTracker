package kr.jhp.purchtrac.domain.usecase.account

import kotlinx.coroutines.flow.Flow
import kr.jhp.purchtrac.domain.model.Account
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    operator fun invoke(): Flow<List<Account>> {
        return accountRepository.getAllAccounts()
    }
}