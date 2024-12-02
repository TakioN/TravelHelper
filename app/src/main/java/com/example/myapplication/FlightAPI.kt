package com.example.myapplication

import kotlinx.coroutines.runBlocking
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

public interface ApiService {
    @POST("graphql")
    @Headers(
        "Content-Type: application/json",
        "Referer: https://flight.naver.com/flights/international/ICN-KIX-20241210/KIX-ICN-20241213?adult=1&child=0&infant=0&fareType=Y"
    )
    suspend fun sendPayload(@Body payload: Payload): Response<Any>
}

object RetrofitInstance {
    private const val BASE_URL = "https://airline-api.naver.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기
            .build()
            .create(ApiService::class.java)
    }
}

data class Res(val travel: String, val galileo: String)
data class Itinerary(
    val departureAirport: String,
    val arrivalAirport: String,
    val departureDate: String
)
data class Variables(
    val adult: Int,
    val child: Int,
    val infant: Int,
    val where: String,
    val isDirect: Boolean,
    val galileoFlag: Boolean,
    val travelBizFlag: Boolean,
    val fareType: String,
    val itinerary: List<Itinerary>,
    val stayLength: String?,
    val trip: String,
    val galileoKey: String?,
    val travelBizKey: String?
)
data class Payload(
    val operationName: String,
    val variables: Variables,
    val query: String
)
fun FlightAPI():Payload {
    val payload = Payload(
        operationName = "getInternationalList",
        variables = Variables(
            adult = 1,
            child = 0,
            infant = 0,
            where = "pc",
            isDirect = false,
            galileoFlag = true,
            travelBizFlag = true,
            fareType = "Y",
            itinerary = listOf(
                Itinerary("ICN", "KIX", "20241210"),
                Itinerary("KIX", "ICN", "20241213")
            ),
            stayLength = "",
            trip = "RT",   //or RT
            galileoKey = "",
            travelBizKey = ""
        ),
        query = """
        query getInternationalList(
            """+ "\$" + """trip: InternationalList_TripType!,
            """ + "\$" + """itinerary: [InternationalList_itinerary]!,
            """ + "\$" + """adult: Int = 1,
            """ + "\$" + """child: Int = 0,
            """+"\$"+"""infant: Int = 0,
            """ + "\$" + """fareType: InternationalList_CabinClass!,
            """ + "\$" + """where: InternationalList_DeviceType = pc,
            """+"\$" + """isDirect: Boolean = false,
            """ + "\$" + """stayLength: String,
            """ + "\$" + """galileoKey: String,
            """ + "\$" + """galileoFlag: Boolean = true,
            """ + "\$" + """travelBizKey: String,
            """ + "\$" + """travelBizFlag: Boolean = true
        ) {
            internationalList(
                input: {
                    trip: """ + "\$" + """trip,
                    itinerary: """ + "\$" + """itinerary,
                    person: { adult: """ + "$" + "adult, child: " + "$" + "child, infant: $" + """"infant },
                    fareType: """ + "$" + """fareType,
                    where: """ + "$" + """where,
                    isDirect: """ + "$" + """isDirect,
                    stayLength: """ + "$" + """stayLength,
                    galileoKey: """ + "$" + """galileoKey,
                    galileoFlag: """ + "$" + """galileoFlag,
                    travelBizKey: """ + "$" + """travelBizKey,
                    travelBizFlag: """ + "$" + """travelBizFlag
                }
            ) {
                galileoKey
                galileoFlag
                travelBizKey
                travelBizFlag
                totalResCnt
                resCnt
                results {
                    airlines
                    airports
                    fareTypes
                    schedules
                    fares
                    errors
                }
            }
        }
    """.trimIndent()
    )
//    val payload1 = Payload(
//        operationName = "getInternationalList",
//        variables = Variables(
//            adult = 1,
//            child = 0,
//            infant = 0,
//            where = "pc",
//            isDirect = true,
//            galileoFlag = true,
//            travelBizFlag = true,
//            fareType = "Y",
//            itinerary = listOf(
//                Itinerary("ICN", "KIX", "20241210"),
//                Itinerary("KIX", "ICN", "20241213")
//            ),
//            stayLength = "",
//            trip = "OW",
//            galileoKey = "",
//            travelBizKey = ""
//        ),
//        query = """
//    query getInternationalList(
//        $trip: InternationalList_TripType!,
//        $itinerary: [InternationalList_itinerary]!,
//        $adult: Int = 1,
//        $child: Int = 0,
//        $infant: Int = 0,
//        $fareType: InternationalList_CabinClass!,
//        $where: InternationalList_DeviceType = pc,
//        $isDirect: Boolean = false,
//        $stayLength: String,
//        $galileoKey: String,
//        $galileoFlag: Boolean = true,
//        $travelBizKey: String,
//        $travelBizFlag: Boolean = true
//    ) {
//        internationalList(
//            input: {
//                trip: $trip,
//                itinerary: $itinerary,
//                person: { adult: $adult, child: $child, infant: $infant },
//                fareType: $fareType,
//                where: $where,
//                isDirect: $isDirect,
//                stayLength: $stayLength,
//                galileoKey: $galileoKey,
//                galileoFlag: $galileoFlag,
//                travelBizKey: $travelBizKey,
//                travelBizFlag: $travelBizFlag
//            }
//        ) {
//            galileoKey
//            galileoFlag
//            travelBizKey
//            travelBizFlag
//            totalResCnt
//            resCnt
//            results {
//                airlines
//                airports
//                fareTypes
//                schedules
//                fares
//                errors
//            }
//        }
//    }
//""".trimIndent()
//    )
    return payload
}
fun main() {
    val payload = FlightAPI()
    runBlocking {
        try{
            val res = RetrofitInstance.api.sendPayload(payload)

            if(res.isSuccessful) {
                println("success")
            }
            else {
                println("fuck + $res")
            }
        } catch(e:Exception) {
            println("err + $e")
        }
    }
}