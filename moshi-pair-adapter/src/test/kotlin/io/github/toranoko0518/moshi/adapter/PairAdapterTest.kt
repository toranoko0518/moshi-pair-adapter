package io.github.toranoko0518.moshi.adapter

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.junit.Before
import org.junit.Test

class PairAdapterTest {

    private lateinit var adapter: PairAdapter

    @Before
    fun setUp() {
        val moshi = Moshi.Builder()
            .add(PairAdapterFactory())
            .build()

        val type = Types.newParameterizedType(
            Pair::class.java,
            String::class.java,
            String::class.java
        )

        adapter = moshi.adapter<Pair<String, String>>(type) as PairAdapter
    }

    @Test
    fun toJson() = assertThat(adapter.toJson(Pair("foo", "bar"))).isEqualTo("[\"foo\",\"bar\"]")

    @Test
    fun valueEqualNull() {
        try {
            adapter.toJson(null)
        } catch (e: Exception) {
            assertThat(e).apply {
                isInstanceOf(NullPointerException::class.java)
                hasMessageThat().isEqualTo("value == null")
            }
        }
    }

    @Test
    fun fromJson() = assertThat(adapter.fromJson("[\"foo\",\"bar\"]")).isEqualTo(Pair("foo", "bar"))

    @Test
    fun nullSafe() = assertThat(adapter.fromJson("null")).isNull()

    @Test
    fun moreThanTwoElements() {
        try {
            adapter.fromJson("[\"foo\",\"bar\",\"baz\"]")
        } catch (e: Exception) {
            assertThat(e).apply {
                isInstanceOf(IllegalArgumentException::class.java)
                hasMessageThat().isEqualTo("Pair with more or less than two elements: [foo, bar, baz]")
            }
        }
    }

    @Test
    fun lessThanTwoElements() {
        try {
            adapter.fromJson("[\"foo\"]")
        } catch (e: Exception) {
            assertThat(e).apply {
                isInstanceOf(IllegalArgumentException::class.java)
                hasMessageThat().isEqualTo("Pair with more or less than two elements: [foo]")
            }
        }
    }

    @Test
    fun pairWithoutFirst() {
        try {
            adapter.fromJson("[\"null\",\"bar\"]")
        } catch (e: Exception) {
            assertThat(e).apply {
                isInstanceOf(IllegalStateException::class.java)
                hasMessageThat().isEqualTo("Pair without first")
            }
        }
    }

    @Test
    fun pairWithoutSecond() {
        try {
            adapter.fromJson("[\"foo\",\"null\"]")
        } catch (e: Exception) {
            assertThat(e).apply {
                isInstanceOf(IllegalStateException::class.java)
                hasMessageThat().isEqualTo("Pair without second")
            }
        }
    }
}
