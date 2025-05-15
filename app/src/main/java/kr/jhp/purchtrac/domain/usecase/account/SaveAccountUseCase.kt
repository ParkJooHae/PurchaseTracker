package kr.jhp.purchtrac.domain.usecase.account

import kr.jhp.purchtrac.domain.model.Account
import javax.inject.Inject

class SaveAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(account: Account): Long {
        return accountRepository.insertAccount(account)
    }
}
