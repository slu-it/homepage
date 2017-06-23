package homepage

import homepage.business.ApplicationManagement
import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.stereotype.Component
import java.time.Clock


@EnableCaching
@EnableScheduling
@SpringBootApplication
class Application {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

    @Bean fun clock(): Clock = Clock.systemUTC()


    @Component
    class InitialLoadCommandLineRunner(
            val management: ApplicationManagement
    ) : CommandLineRunner {

        val log: Logger = getLogger(javaClass)

        override fun run(vararg args: String?) {
            log.info("loading initial data")
            management.refreshApplication()
        }

    }

}
