package homepage

import homepage.persistence.LocalGitRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service

@Service
class RefreshService(
        private val repository: LocalGitRepository,
        private val cacheManager: CacheManager
) {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    fun refresh() {
        repository.update()
        cacheManager.cacheNames
                .forEach { cacheName ->
                    log.debug("clearing cache: {}", cacheName)
                    cacheManager.getCache(cacheName).clear()
                }
    }

}