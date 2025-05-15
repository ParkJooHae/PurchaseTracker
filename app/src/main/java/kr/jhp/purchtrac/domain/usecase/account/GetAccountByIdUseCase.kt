package kr.jhp.purchtrac.domain.usecase.account

import kr.jhp.purchtrac.domain.model.Account
import javax.inject.Inject

class GetAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(id: Long): Account? {
        return accountRepository.getAccountById(id)
    }
}