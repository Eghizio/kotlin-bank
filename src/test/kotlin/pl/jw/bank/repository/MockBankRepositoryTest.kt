package pl.jw.bank.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.jw.bank.Then
import pl.jw.bank.When
import pl.jw.bank.repository.mock.MockBankRepository

internal class MockBankRepositoryTest {

    private val repository = MockBankRepository()

    @Test
    fun `should provide a collection of banks`() {
        When()
        val banks = repository.getBanks()

        Then()
        assertThat(banks.size).isGreaterThanOrEqualTo(3)
    }

    @Test
    fun `should provide some mock data`() {
        When()
        val banks = repository.getBanks()

        Then()
        assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        assertThat(banks).anyMatch() { it.trust != 0.0 }
        assertThat(banks).anyMatch() { it.transactionFee != 0 }
    }
}
