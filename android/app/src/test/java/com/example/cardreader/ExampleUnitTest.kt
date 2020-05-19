package com.example.cardreader

import com.example.cardreader.data.Response
import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
        val resp = Response("string")
        print(Gson().toJson(resp))

        val str = """{\"image\":\"Repairing card perspective....
            Detecting text in card...
            a tee

                    www. .LUSHMICHIGAN, COM
            GS11 CLINTONVILLE ROAD
            CLARKSTON, MICHIGAN 48348
            JON 248-343-5976
            _KAYLEN 784-552-8798
            a *
                    Rts
            ~-
            LAWN * PROPERTY ENHANCEMENT

                    . JSON 248-343-5976
            s KAYLEN 734-552-8798

            6811 CLINTONVILLE ROAD
            CLARKSTON, MICHIGAN 48348

            www. OA COM
                    Recognition completed.
            \"}"""

        }
}
