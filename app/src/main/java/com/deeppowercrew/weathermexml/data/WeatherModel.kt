package com.deeppowercrew.weathermexml.data

data class WeatherModel(
    val city: String,
    val time: String,
    val condition: String,
    val currentTemp: String,
    val tempMax: String,
    val tempMin: String,
    val conditionIconUrl: String,
    val hours: String
)