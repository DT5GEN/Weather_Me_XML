package com.deeppowercrew.weathermexml.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.deeppowercrew.weathermexml.adapters.WeatherAdapter
import com.deeppowercrew.weathermexml.data.WeatherModel
import com.deeppowercrew.weathermexml.databinding.FragmentHoursBinding
import com.deeppowercrew.weathermexml.viewModel.MainViewModel
import org.json.JSONArray
import org.json.JSONObject


class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
//        model.liveDataCurrent.observe(viewLifecycleOwner){
//            adapter.submitList(getHoursList(it))
//        }

        updateHoursInfo()
    }

    private fun updateHoursInfo() = with(binding) {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            adapter.submitList(getHoursList(it))
//            textData.text = it.time
//            tvCity.text = it.city
//            textCurrentTemp.text = "${it.currentTemp}°C"
//            textCondition.text = it.condition
//            textTempMaxMin.text = maxMinTemp
//            Picasso.get().load("https:" + it.conditionIconUrl).into(imageWeather)

        }
    }

    private fun getHoursList(weatherItem: WeatherModel): List<WeatherModel> {
        val hoursArray = JSONArray(weatherItem.hours)
        val list = ArrayList<WeatherModel>()
        for (i in 0 until hoursArray.length()) {
            val item = WeatherModel(
                weatherItem.city,
                (hoursArray[i] as JSONObject).getString("time"),
                      (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                (hoursArray[i] as JSONObject).getString("temp_c"),"",
                "",
                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                ""
            )
            list.add(item)
        }
        return list
    }

    private fun initRecyclerView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rcView.adapter = adapter


    }

    companion object {

        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}