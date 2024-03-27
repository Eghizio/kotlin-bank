package pl.jw.bank.service

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import pl.jw.bank.Then
import pl.jw.bank.When
import pl.jw.bank.repository.BankRepository

internal class DefaultBankServiceTest {

    private val repository: BankRepository = mockk(relaxed = true)
    private val service = DefaultBankService(repository)

    @Test
    fun `should call repository to get banks`() {
//        Given()
//        every { repository.getBanks() } returns emptyList()

        When()
        service.getBanks()

        Then()
        verify(exactly = 1) { repository.getBanks() }
    }
}
