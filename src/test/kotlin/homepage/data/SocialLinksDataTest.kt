package homepage.data

internal class SocialLinksDataTest : JsonSerializableContract<SocialLinksData> {

    override val minInstance = SocialLinksData()

    override val maxInstance = SocialLinksData().apply {
        github = "https://github.com/slu-it"
        twitter = "https://twitter.com/slu-it"
        xing = "https://xing.de/slu-it"
    }

}

