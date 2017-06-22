package homepage.api

import com.nhaarman.mockito_kotlin.verify
import homepage.business.ApplicationManagement
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.reset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@ExtendWith(SpringExtension::class)
@WebMvcTest(ManagementController::class)
internal class ManagementControllerTest {

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var management: ApplicationManagement

    @BeforeEach
    fun resetMocks() = reset(management)

    @Test
    fun `GET on refresh endpoint triggers application refresh`() {
        mvc.perform(get("/management/refresh"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Application data refreshed."))
        verify(management).refreshApplication()
    }

    @Test
    fun `POST on refresh endpoint triggers application refresh`() {
        mvc.perform(post("/management/refresh"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Application data refreshed."))
        verify(management).refreshApplication()
    }

}