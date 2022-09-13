package com.deeppowercrew.weathermexml.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.deeppowercrew.weathermexml.adapters.WeatherAdapter
import com.deeppowercrew.weathermexml.data.WeatherModel
import com.deeppowercrew.weathermexml.databinding.FragmentDaysBinding
import com.deeppowercrew.weathermexml.viewModel.MainViewModel

class DaysFragment : Fragment(), WeatherAdapter.Listener {
    private lateinit var adapter: WeatherAdapter
    private lateinit var binding: FragmentDaysBinding
    private val model: MainViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
            binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDaysFragment()
        updateDaysInfo()
    }

    private fun initDaysFragment() = with(binding) {
        adapter = WeatherAdapter(this@DaysFragment)
        recyclerDays.layoutManager = LinearLayoutManager(activity)
        recyclerDays.adapter = adapter
    }

    private fun updateDaysInfo() = with(binding) {
        model.liveDataList.observe(viewLifecycleOwner) {
            adapter.submitList(it.subList(1,it.size))

        }
    }

//    private fun getDaysList(weatherItem: WeatherModel): List<WeatherModel> {
//        val hoursArray = JSONArray(weatherItem.hours)
//        val list = ArrayList<WeatherModel>()
//        for (i in 0 until hoursArray.length()) {
//            val item = WeatherModel(
//                weatherItem.city,
//                (hoursArray[i] as JSONObject).getString("time"),
//                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
//                (hoursArray[i] as JSONObject).getString("temp_c"),"",
//                "",
//                (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
//                ""
//            )
//            list.add(item)
//        }
//        return list
//    }


    companion object {

        @JvmStatic
        fun newInstance() =       DaysFragment()
    }

    override fun onClick(item: WeatherModel) {
        model.liveDataCurrent.value = item
    }
}