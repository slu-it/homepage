package homepage

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/management")
class ManagmentController(
        private val refreshService: RefreshService
) {

    @GetMapping("/refresh")
    @PostMapping("/refresh")
    fun refreshApplicationData(): String {
        val start = now()
        refreshService.refresh()
        val duration = (now() - start)
        return "application data refreshed. (took: $duration ms)"
    }

    private fun now() = System.currentTimeMillis()


}