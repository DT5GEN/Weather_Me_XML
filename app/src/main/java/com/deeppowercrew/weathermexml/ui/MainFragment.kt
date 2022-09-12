package com.deeppowercrew.weathermexml.ui

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.deeppowercrew.weathermexml.adapters.ViewPagerAdapter
import com.deeppowercrew.weathermexml.databinding.FragmentMainBinding
import com.deeppowercrew.weathermexml.isPermissionsGranted
import com.google.android.material.tabs.TabLayoutMediator

const val API_KEY = "9c4dca2eee744d2f9ba134332220209"


class MainFragment : Fragment() {
    private val fragmentList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private lateinit var binding: FragmentMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val tabsList = listOf(
        "Hours",
        "Days"    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        init()
        requestWeatherData("Kyoto")
    }

    private fun init() = with(binding) {
        val adapter = ViewPagerAdapter(activity as AppCompatActivity, fragmentList)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = tabsList[pos]
        }.attach()
    }


    private fun permissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                Toast.makeText(activity, "Permissions is $it", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkPermissions() {
        if (!isPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }


    }

    private fun requestWeatherData(city: String){
        val url =
            "https://api.weatherapi.com/v1/forecast.json" + "?key=$API_KEY&" + "q=$city" + "&days=" +
                    "7" +
                    "&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url, { response ->
//
                Log.d("MyLog", "Volley response $response")
            }, { error ->
                Log.d("MyLog", "Volley Error $error")

            }
        )


        queue.add(stringRequest)
    }


    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}