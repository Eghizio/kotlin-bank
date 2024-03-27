package pl.jw.bank.service

import org.springframework.stereotype.Service
import pl.jw.bank.model.Bank
import pl.jw.bank.repository.BankRepository

@Service
class DefaultBankService(
//    @Qualifier("network") private val repository: BankRepository
    private val repository: BankRepository
) : BankService {

    override fun getBanks(): Collection<Bank> = repository.getBanks()

    override fun getBank(accountNumber: String): Bank = repository.getBank(accountNumber)

    override fun addBank(bank: Bank): Bank = repository.createBank(bank)

    override fun updateBank(bank: Bank): Bank = repository.updateBank(bank)

    override fun deleteBank(accountNumber: String) = repository.deleteBank(accountNumber)
}
