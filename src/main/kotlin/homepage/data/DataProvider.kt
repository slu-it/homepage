package homepage.data

import homepage.business.games.GameData
import homepage.business.social.SocialLinks
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service


@Service
class DataProvider(
        private val repository: DataRepository
) {

    @Cacheable("socialLinks")
    fun getSocialLinks(): SocialLinks {
        return repository.findDatum(name = "social-links", dataClass = SocialLinks::class) ?: SocialLinks()
    }

    @Cacheable("games")
    fun getGames(): List<GameData> {
        return repository.findData(path = "games", extension = "game", dataClass = GameData::class)
    }

}
