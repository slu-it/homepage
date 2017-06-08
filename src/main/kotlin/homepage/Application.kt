package homepage

import homepage.persistence.LocalGitRepository
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import java.util.concurrent.TimeUnit


@EnableScheduling
@SpringBootApplication
class Application {

    @Bean
    fun schedulingConfigurer(repository: LocalGitRepository): SchedulingConfigurer = SchedulingConfigurer {
        it.addFixedDelayTask({ repository.refresh() }, TimeUnit.HOURS.toMillis(1))
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(Application::class.java, *args)
        }
    }

}
