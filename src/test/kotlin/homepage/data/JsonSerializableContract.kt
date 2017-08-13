package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal interface JsonSerializableContract<T : Any> {

    val minInstance: T
    val maxInstance: T

    @Test
    fun `minimum instance can be (de)serialiazed to and from JSON`() {
        val instance = minInstance;
        val result = jsonRoundTrip(instance)
        assertThat(result).isEqualToComparingFieldByField(instance)
    }

    @Test
    fun `maximum instance can be (de)serialiazed to and from JSON`() {
        val instance = maxInstance;
        val result = jsonRoundTrip(instance)
        assertThat(result).isEqualToComparingFieldByField(instance)
    }

    fun jsonRoundTrip(instance: T): T {
        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(instance)
        return objectMapper.readValue(json, instance.javaClass)
    }

}