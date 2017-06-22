package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.SoftAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = arrayOf(
        DataRepositoryIntTest.TestConfiguration::class
))
internal class DataRepositoryIntTest {

    @ComponentScan("homepage.data")
    @EnableConfigurationProperties
    class TestConfiguration {
        @Bean fun objectMapper() = ObjectMapper()
    }

    @Autowired
    lateinit var cut: DataRepository

    @Test
    fun `repository contains required files`() {
        cut.update()
        with(SoftAssertions()) {
            assertThat(workingDir().resolve("social-links.json")).isRegularFile()
            assertThat(workingDir().resolve("games")).isDirectory()
            assertAll()
        }
    }

    fun workingDir() = cut.getWorkingDirectory()

}