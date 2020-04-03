package ao.corona.coronasos.controller

import ao.corona.coronasos.service.MedicalSupplyPostService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest
class MedicalSupplyPostControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean(relaxed = true)
    private lateinit var medicalSupplyPostService: MedicalSupplyPostService

    private val objectMapper = ObjectMapper()

    @Test
    fun `create post should return 200`() {
        println("Your request = ${objectMapper.writeValueAsString(medicalSupplyPost)}")
        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/posts")
                .content(objectMapper.writeValueAsString(medicalSupplyPost))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk)
    }

    @Test
    fun `create post with malformed request should return 400`() {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/v1/posts")
                .content("{bad: request}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `get post by Id should return 200`() {
        every { medicalSupplyPostService.getPostById(medicalSupplyPost.id) } returns Optional.of(medicalSupplyPost)

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/posts/${medicalSupplyPost.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk)

    }

    @Test
    fun `get post by Id with non-existing Id should return 404`() {
        every { medicalSupplyPostService.getPostById(medicalSupplyPost.id) } returns Optional.empty()

        mockMvc.perform(MockMvcRequestBuilders
                .get("/v1/posts/${UUID.randomUUID()}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

        ).andExpect(status().isNotFound)

    }
}