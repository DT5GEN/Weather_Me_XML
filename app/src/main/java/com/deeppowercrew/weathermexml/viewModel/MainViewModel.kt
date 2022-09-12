package com.deeppowercrew.weathermexml.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deeppowercrew.weathermexml.data.WeatherModel

class MainViewModel: ViewModel() {

    val liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()

}