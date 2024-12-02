package com.example.myapplication.data

data class City(
    val city: String,               // 도시 이름
    val nation: String,             // 국가 이름
    val locations: List<Location>
)
data class Location(
    val name: String,               // 관광지 이름
    val ageGroup: List<Int>,        // 나이대
    val category: List<Int>,     // 카테고리
    val with: List<String>,     // 동행 유형
    var score: Double
)
