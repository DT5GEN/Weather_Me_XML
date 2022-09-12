package com.deeppowercrew.weathermexml.data

data class WeatherModel (
    val city: String,
    val time: String,
    val condition: String,
    val conditionIconUrl:String,
    val currentTemp:String,
    val tempMin:String,
    val tempMax:String,
    val hours:String
        )