package homepage

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import java.util.concurrent.TimeUnit


@EnableCaching
@EnableScheduling
@SpringBootApplication
class Application {

    @Bean
    fun schedulingConfigurer(refreshService: RefreshService): SchedulingConfigurer = SchedulingConfigurer {
        it.addFixedDelayTask({ refreshService.refresh() }, TimeUnit.HOURS.toMillis(1))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

}
