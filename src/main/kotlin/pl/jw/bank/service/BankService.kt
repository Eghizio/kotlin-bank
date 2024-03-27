package pl.jw.bank.service

import pl.jw.bank.model.Bank

interface BankService {
    fun getBanks(): Collection<Bank>
    fun getBank(accountNumber: String): Bank
    fun addBank(bank: Bank): Bank
    fun updateBank(bank: Bank): Bank
    fun deleteBank(accountNumber: String): Unit
}
