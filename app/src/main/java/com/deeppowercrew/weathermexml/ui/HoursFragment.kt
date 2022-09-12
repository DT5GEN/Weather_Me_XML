package com.deeppowercrew.weathermexml.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.deeppowercrew.weathermexml.adapters.WeatherAdapter
import com.deeppowercrew.weathermexml.data.WeatherModel
import com.deeppowercrew.weathermexml.databinding.FragmentHoursBinding


class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter

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
    }

    private fun initRecyclerView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rcView.adapter = adapter
        val list = listOf(
            WeatherModel(
                "",
                "12 / 09 / 2022",
                "kayfovo",
                "",
                "33 C",
                "",
                "",
                ""
            ),
            WeatherModel(
                "",
                "13 / 09 / 2022",
                "kayfovo",
                "",
                "36 C",
                "",
                "",
                ""
            ),
            WeatherModel(
                "",
                "14 / 09 / 2022",
                "kayfovo",
                "",
                "39 C",
                "",
                "",
                ""
            )
        )
        adapter.submitList(list)

    }

    companion object {

        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}