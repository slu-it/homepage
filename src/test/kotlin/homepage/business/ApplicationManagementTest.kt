package homepage.business

import com.nhaarman.mockito_kotlin.*
import homepage.data.DataRepository
import org.junit.jupiter.api.Test
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

internal class ApplicationManagementTest {

    val repository: DataRepository = mock()

    val cache1: Cache = mock()
    val cache2: Cache = mock()
    val cacheManager: CacheManager = mock {
        on { cacheNames } doReturn listOf("cache1", "cache2")
        on { getCache("cache1") } doReturn cache1
        on { getCache("cache2") } doReturn cache2
    }

    val cut = ApplicationManagement(repository, cacheManager)

    @Test
    fun `refreshing the application updates the repository`() {
        cut.refreshApplication()
        verify(repository).update()
        verifyNoMoreInteractions(repository)
    }

    @Test
    fun `refreshing the application clear all caches`() {
        cut.refreshApplication()
        verify(cache1).clear()
        verify(cache2).clear()
        verifyNoMoreInteractions(cache1, cache2)
    }

}