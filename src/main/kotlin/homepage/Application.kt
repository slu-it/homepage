package homepage

import homepage.business.ApplicationManagement
import org.eclipse.jgit.api.Git
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import java.nio.file.Files
import java.time.Clock
import java.util.concurrent.TimeUnit


@EnableCaching
@EnableScheduling
@SpringBootApplication
class Application {

    @Bean
    fun clock(): Clock = Clock.systemUTC()

    @Bean
    fun schedulingConfigurer(applicationManagement: ApplicationManagement): SchedulingConfigurer = SchedulingConfigurer {
        it.addFixedDelayTask({ applicationManagement.refreshApplication() }, TimeUnit.HOURS.toMillis(1))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

}
