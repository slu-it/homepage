package homepage.business

import homepage.data.DataRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service

@Service
class ApplicationManagement(
        private val repository: DataRepository,
        private val cacheManager: CacheManager
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun refreshApplication() {
        updateData()
        clearCaches()
    }

    private fun updateData() {
        log.debug("updating repository...")
        repository.update()
        log.debug("...repository updated!")
    }

    private fun clearCaches() {
        log.debug("clearing caches:")
        cacheManager.cacheNames.stream()
                .peek { cacheName -> log.debug(" - {}", cacheName) }
                .map { cacheName -> cacheManager.getCache(cacheName) }
                .forEach { cache -> cache.clear() }
        log.debug("...all caches cleared!")
    }

}