package pl.jw.bank.repository.network

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import pl.jw.bank.model.Bank
import pl.jw.bank.repository.BankRepository
import pl.jw.bank.repository.network.dto.BankList
import java.io.IOException

@Repository("network")
class NetworkBankRepository(
    @Autowired private val restTemplate: RestTemplate
) : BankRepository {

    companion object {
        const val BASE_URL = "http://54.193.31.159/banks"
    }

    override fun getBanks(): Collection<Bank> {
        val response = restTemplate.getForEntity<BankList>(BASE_URL)

        return response.body
            ?.results?.map { it.toModel() }
            ?: throw IOException("Could not fetch banks from the network.")
    }

    override fun getBank(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }
}
