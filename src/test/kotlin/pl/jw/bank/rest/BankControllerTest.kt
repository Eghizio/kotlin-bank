package pl.jw.bank.rest

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import pl.jw.bank.And
import pl.jw.bank.Expect
import pl.jw.bank.Given
import pl.jw.bank.Then
import pl.jw.bank.When
import pl.jw.bank.model.Bank

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(
    var mockMvc: MockMvc,
    var objectMapper: ObjectMapper,
) {
    private val baseUrl = "/api/banks"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            Expect()
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].accountNumber") { value("123") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return bank for its account number`() {
            Given()
            val accountNumber = 123

            Expect()
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$.trust") { value("1.0") }
                    jsonPath("$.transactionFee") { value("1") }
                }
        }

        @Test
        fun `should return Not Found if the account number does not exist`() {
            Given()
            val accountNumber = "does_not_exist"

            Expect()
            mockMvc.get("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add the new bank`() {
            Given()
            val newBank = Bank("test_account", 3.1415, 42)

            When()
            val postRequest = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            Then()
            postRequest
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(newBank))
                    }
//                    jsonPath("$.accountNumber") { value(newBank.accountNumber) }
//                    jsonPath("$.trust") { value(newBank.trust) }
//                    jsonPath("$.transactionFee") { value(newBank.transactionFee) }
                }

            And()
            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(newBank))
                    }
                }
        }

        @Test
        fun `should return Bad Request if bank with given account number already exists`() {
            Given()
            val invalidBank = Bank("123", 1.0, 1)

            When()
            val postRequest = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            Then()
            postRequest
                .andDo { print() }
                .andExpect {
                    status { isBadRequest() }
                }
        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(PER_CLASS)
    inner class PatchExistingBank {
        @Test
        fun `should update an existing bank`() {
            Given()
            val updatedBank = Bank("123", 42.0, 42)

            When()
            val patchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }

            Then()
            patchRequest
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }

            And()
            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect {
                    content {
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }
        }

        @Test
        fun `should return Bad Request if no bank with given account number exists`() {
            Given()
            val invalidBank = Bank("does_not_exist", 1.0, 1)

            When()
            val patchRequest = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            Then()
            patchRequest
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(PER_CLASS)
    inner class DeleteExistingBank {
//        @Disabled // This test previously was deleting account `123` which was affecting other test cases. Requires isolation.
        @Test
        @DirtiesContext // Isolates.
        fun `should delete the bank with the given account number`() {
            Given()
            val accountNumber = 2137

            Expect()
            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNoContent() }
                }

            And()
            mockMvc.get("$baseUrl/$accountNumber")
                .andExpect {
                    status { isNotFound() }
                }
        }

        @Test
        fun `should return Not Found if no bank with given account number exists`() {
            Given()
            val invalidAccountNumber = "does_not_exist"

            Expect()
            mockMvc.delete("$baseUrl/$invalidAccountNumber")
                .andDo { print() }
                .andExpect {
                    status { isNotFound() }
                }
        }
    }
}
