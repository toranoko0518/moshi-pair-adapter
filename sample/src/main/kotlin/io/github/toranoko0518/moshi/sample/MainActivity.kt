package io.github.toranoko0518.moshi.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.toranoko0518.moshi.adapter.PairAdapterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Pair("foo", "bar")

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(PairAdapterFactory())
            .build()

        val type = Types.newParameterizedType(
            Pair::class.java,
            String::class.java,
            String::class.java
        )

        val adapter = moshi.adapter<Pair<String, String>>(type)

        val json = adapter.toJson(data)
        val pair = adapter.fromJson(json)

        Log.d("DEBUG", "Json: $json")
        Log.d("DEBUG", "Pair: $pair")
    }
}
