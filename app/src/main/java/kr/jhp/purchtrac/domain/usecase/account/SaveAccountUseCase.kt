package kr.jhp.purchtrac.domain.usecase.account

import kr.jhp.purchtrac.domain.model.Account
import kr.jhp.purchtrac.domain.repository.AccountRepository
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account): Long {
        return if (account.id == 0L) {
            accountRepository.insertAccount(account)
        } else {
            accountRepository.updateAccount(account)
            account.id
        }
    }
}