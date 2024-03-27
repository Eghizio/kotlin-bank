package pl.jw.bank.repository.mock

import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository
import pl.jw.bank.model.Bank
import pl.jw.bank.repository.BankRepository
import java.util.NoSuchElementException

@Repository("mock")
@Primary
class MockBankRepository : BankRepository {

    private val banks = mutableListOf(
        Bank("123", 1.0, 1),
        Bank("234", 2.0, 2),
        Bank("345", 3.0, 3),
        Bank("2137", 6.0, 9),
    )

    override fun getBanks(): Collection<Bank> = banks

    override fun getBank(accountNumber: String): Bank = findBankByAccountNumber(accountNumber)

    override fun createBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists.")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = findBankByAccountNumber(bank.accountNumber)

        banks.replaceAll { if (it == currentBank) bank else it }

        return bank
    }

    override fun deleteBank(accountNumber: String) {
        banks.remove(findBankByAccountNumber(accountNumber))
    }

    private fun findBankByAccountNumber(accountNumber: String): Bank =
        banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number '$accountNumber'")
}
