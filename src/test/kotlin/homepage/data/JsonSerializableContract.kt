package homepage.data

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

interface JsonSerializableContract<T : Any> {

    val minInstance: T
    val maxInstance: T

    @Test
    fun `minimum instance can be (de)serialiazed to and from JSON`() {
        val instance = minInstance;
        val result = jsonRounTrip(instance)
        assertThat(result).isEqualToComparingFieldByField(instance)
    }

    @Test
    fun `maximum instance can be (de)serialiazed to and from JSON`() {
        val instance = maxInstance;
        val result = jsonRounTrip(instance)
        assertThat(result).isEqualToComparingFieldByField(instance)
    }

    fun jsonRounTrip(instance: T): T {
        val objectMapper = ObjectMapper()
        val json = objectMapper.writeValueAsString(instance)
        return objectMapper.readValue(json, instance.javaClass)
    }

}