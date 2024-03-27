package pl.jw.bank.model

data class Bank(
//    @JsonProperty("account_number")
    val accountNumber: String,

//    @JsonProperty("trust")
    val trust: Double,

//    @JsonProperty("default_transaction_fee")
    val transactionFee: Int
)
