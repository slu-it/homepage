package homepage.api

import homepage.business.ApplicationManagement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/management")
class ManagementController(
        private val management: ApplicationManagement
) {

    @GetMapping("/refresh")
    fun handleRefreshGetRequest() = handleRefreshRequest()

    @PostMapping("/refresh")
    fun handleRefreshPostRequest() = handleRefreshRequest()

    private fun handleRefreshRequest(): String {
        management.refreshApplication()
        return "Application data refreshed."
    }

}