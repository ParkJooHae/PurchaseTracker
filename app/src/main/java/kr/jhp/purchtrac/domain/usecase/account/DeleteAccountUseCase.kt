package kr.jhp.purchtrac.domain.usecase.account

import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {
    suspend operator fun invoke(accountId: Long) {
        accountRepository.deleteAccount(accountId)
    }
}