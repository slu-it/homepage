package homepage

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.TimeUnit

@SpringBootApplication
class Application {

    @Configuration
    @EnableScheduling
    class SchedulingConfiguration(
            val repository: LocalGitRepository
    ) : SchedulingConfigurer {

        override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
            taskRegistrar.addFixedDelayTask(repository::refresh, TimeUnit.HOURS.toMillis(1))
        }

    }

}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
