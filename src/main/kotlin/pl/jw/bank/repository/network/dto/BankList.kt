package pl.jw.bank.repository.network.dto

import pl.jw.bank.model.Bank

data class BankList(
    val results: Collection<BankDto>,
)

data class BankDto(
    val account_number: String,
    val trust: Double,
    val default_transaction_fee: Int
) {
    fun toModel(): Bank = Bank(account_number, trust, default_transaction_fee)
}
