package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.data.City
import com.example.myapplication.data.Location
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.nio.file.Paths
import kotlin.math.abs

/*category info=====================
[0: 먹거리, 1: 쇼핑, 2: 역사, 3: 자연, 4: 문화, 5: 관광지, 6: 액티비티, 7: 사진, 8: 예술]
====================================*/

var sortedrank: List<Location> = emptyList()
val weights = arrayOf(
    doubleArrayOf(1.0, 0.5, 0.0, 0.0, 0.7, 0.3, 0.1, 0.5, 0.2),
    doubleArrayOf(0.5, 1.0, 0.0, 0.0, 0.5, 0.3, 0.2, 0.3, 0.1),
    doubleArrayOf(0.0, 0.0, 1.0, 0.6, 0.7, 0.7, 0.3, 0.6, 0.4),
    doubleArrayOf(0.0,0.0,0.6,1.0,0.4,0.5,0.1,0.7,0.2),
    doubleArrayOf(0.7,0.5,0.7,0.4,1.0,0.6,0.4,0.6,0.6),
    doubleArrayOf(0.3,0.3,0.7,0.5,0.6,1.0,0.7,0.5,0.4),
    doubleArrayOf(0.1,0.2,0.3,0.1,0.4,0.7,1.0,0.1,0.0),
    doubleArrayOf(0.5,0.3,0.6,0.7,0.6,0.5,0.1,1.0,0.7),
    doubleArrayOf(0.2,0.1,0.4,0.2,0.6,0.4,0.0,0.7,1.0)
)

fun main() {
//    Recommendation(20, listOf(6, 5), "혼자")
}
fun Recommendation(context: Context, age: Int, category: List<Int>, companion: String):City {
//    val path = System.getProperty("user.dir") + "/app/src/main/java/com/example/myapplication"
//    val jsonData = File("$path/data/trip_locations.json").readText()
    val inputStream = context.resources.openRawResource(R.raw.trip_locations)
    val jsonData = inputStream.bufferedReader().use { it.readText() }

    val gson = Gson()
    val cityToken = object: TypeToken<List<City>>() {}.type
    val cities: List<City> = gson.fromJson(jsonData, cityToken)

    var maxScore = 0.0
    var maxIdx = -1
    cities.forEachIndexed() { idx, city ->
        var localScore = 0.0
        city.locations.forEach{
            it.score = calculateScore(it, age, category, companion)
            localScore += it.score
        }

        if(localScore > maxScore) {
            maxScore = localScore
            maxIdx = idx
        }
    }
    print(cities[maxIdx])
    return cities[maxIdx]
//    return scoreList
}

fun calculateScore(location: Location, age: Int, category: List<Int>, companion: String): Double {
    var score:Double = 0.0
    var ageDiff = 50

    //    calculate category score
    for((idx, selectedCategory) in category.withIndex()) {
        if(idx == 0) {
            score += 22 * listOf(
                weights[category[0]][location.category[0]],
                weights[category[0]][location.category[1]],
                weights[category[0]][location.category[2]]
            ).maxOrNull()!!
        }
        else if(idx == 1) {
            score += 17 * listOf(
                weights[category[1]][location.category[0]],
                weights[category[1]][location.category[1]],
                weights[category[1]][location.category[2]]
            ).maxOrNull()!!
        }
        else {
            score += 11 * listOf(
                weights[category[2]][location.category[0]],
                weights[category[2]][location.category[1]],
                weights[category[2]][location.category[2]]
            ).maxOrNull()!!
        }
    }


    //    calculate age score
    location.ageGroup.forEach{
        var localAgeDiff = age - it
        if(abs(localAgeDiff) < ageDiff) ageDiff = localAgeDiff
    }
    if(ageDiff >= 0) {
        score += 20 * (1.0 - 0.02 * ageDiff)
    }
    else{
        score += 20 * (1.0 + ageDiff * 0.01)
    }


    //    calculate companion score
    if(location.with.contains(companion)) score += 30.0
    else score += 15.0

    return score
}