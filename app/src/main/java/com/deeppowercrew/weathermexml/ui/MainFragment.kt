package com.deeppowercrew.weathermexml.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.deeppowercrew.weathermexml.adapters.ViewPagerAdapter
import com.deeppowercrew.weathermexml.data.WeatherModel
import com.deeppowercrew.weathermexml.databinding.FragmentMainBinding
import com.deeppowercrew.weathermexml.isPermissionsGranted
import com.deeppowercrew.weathermexml.utils.DialogManager
import com.deeppowercrew.weathermexml.viewModel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY = "9c4dca2eee744d2f9ba134332220209"


class MainFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val fragmentList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private lateinit var binding: FragmentMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val model: MainViewModel by activityViewModels()
    private val tabsList = listOf(
        "Hours",
        "Days"
    )


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
        updateHeadCard()
        requestWeatherData("London")
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun init() = with(binding) {
        val adapter = ViewPagerAdapter(activity as AppCompatActivity, fragmentList)
        viewPager.adapter = adapter
        updateLocationData()

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = tabsList[pos]
        }.attach()
        ibRefresh.setOnClickListener {
            tabLayout.selectTab(tabLayout.getTabAt(0))
            checkLocation()
        }
        ibSearch.setOnClickListener {
            DialogManager.searchCity(requireContext(), object : DialogManager.Listener{
                override fun onClick(name: String?) {
                    name?.let { it1 -> requestWeatherData(it1) }
                }

            })
        }
    }

    private fun updateLocationData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {

        val cancellationToken = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return Toast.makeText(requireContext(), "GPS blocked!", Toast.LENGTH_SHORT).show()
        }
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken.token
        ).addOnCompleteListener {
            requestWeatherData("${it.result.latitude},${it.result.longitude}")
        }
    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(requireContext(), object : DialogManager.Listener {
                override fun onClick(name:String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }


    private fun updateHeadCard() = with(binding) {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            val maxMinTemp = "${it.tempMax}°C/ ${it.tempMin}°C"
            textData.text = it.time
            tvCity.text = it.city
            textCurrentTemp.text = it.currentTemp.ifEmpty { maxMinTemp }
            textCondition.text = it.condition
            textTempMaxMin.text = if (it.currentTemp.isEmpty()) "" else maxMinTemp
            Picasso.get().load("https:" + it.conditionIconUrl).into(imageWeather)

        }
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

    private fun requestWeatherData(city: String) {
        val url =
            "https://api.weatherapi.com/v1/forecast.json" + "?key=$API_KEY&" + "q=$city" + "&days=" +
                    "7" +
                    "&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET,
            url, { response ->
                parseWeatherData(response)
                Log.d("MyLog", "Volley response $response")
            }, { error ->
                Log.d("MyLog", "Volley Error $error")

            }
        )


        queue.add(stringRequest)
    }

    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val list = parseForecastDays(mainObject)
        parseCurrentWeatherData(mainObject, list[0])
    }

    private fun parseCurrentWeatherData(mainObject: JSONObject, weatherItem: WeatherModel) {

        val itemHeadCard = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("current").getString("last_updated"),
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current").getString("temp_c").toFloat().toInt().toString(),
            weatherItem.tempMax,
            weatherItem.tempMin,
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("icon"),
            weatherItem.hours

        )
        model.liveDataCurrent.value = itemHeadCard
        Log.d("MyLog", "City: ${itemHeadCard.city}")
        Log.d("MyLog", "City: ${itemHeadCard.currentTemp}")
        Log.d("MyLog", "City: ${itemHeadCard.tempMax}")
        Log.d("MyLog", "City: ${itemHeadCard.tempMin}")
        Log.d("MyLog", "City: ${itemHeadCard.hours}")
    }

    private fun parseForecastDays(mainObject: JSONObject): List<WeatherModel> {
        val list = ArrayList<WeatherModel>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val name = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day").getJSONObject("condition").getString("text"),
                "",
                day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        model.liveDataList.value = list
        return list
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }
}