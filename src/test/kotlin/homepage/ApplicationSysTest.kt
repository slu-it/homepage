package homepage

import info.novatec.testit.webtester.browser.Browser
import info.novatec.testit.webtester.browser.factories.ChromeFactory
import info.novatec.testit.webtester.junit5.EnableWebTesterExtensions
import info.novatec.testit.webtester.junit5.extensions.browsers.CreateUsing
import info.novatec.testit.webtester.junit5.extensions.browsers.Managed
import info.novatec.testit.webtester.kotlin.pages.Page
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import pages.BasePage
import kotlin.reflect.KClass

@EnableWebTesterExtensions
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ApplicationSysTest {

    companion object {
        @JvmStatic @Managed @CreateUsing(ChromeFactory::class) lateinit var browser: Browser
    }

    @LocalServerPort
    var port: Int = -1

    @BeforeEach
    fun refreshData() {
        val url = baseUrl("/management/refresh")
        val response: ResponseEntity<String> = RestTemplate().getForEntity(url)
        assertThat(response.statusCode).isSameAs(HttpStatus.OK)
    }

    @Nested
    inner class `index page` : PageContract() {
        override val contextPath = "/"
    }

    @Nested
    inner class `games page` : PageContract() {
        override val contextPath = "/games"
    }

    abstract inner class PageContract {

        abstract val contextPath: String

        @Test
        fun `header is displayed`() {
            val page = openBaseUrl(BasePage::class)
            assertThat(page.headerIsDisplayed()).isTrue()
        }

        @Test
        fun `footer is displayed`() {
            val page = openBaseUrl(BasePage::class)
            assertThat(page.footerIsDisplayed()).isTrue()
        }

        fun <T : Page> openBaseUrl(pageClass: KClass<T>): T {
            return browser.open().url(baseUrl(contextPath), pageClass.java)
        }

    }

    fun baseUrl(contextPath: String) = "http://localhost:$port$contextPath"

}

