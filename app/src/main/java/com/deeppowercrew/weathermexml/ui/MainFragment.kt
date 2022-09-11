package com.deeppowercrew.weathermexml.ui

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.deeppowercrew.weathermexml.databinding.FragmentMainBinding
import com.deeppowercrew.weathermexml.isPermissionsGranted

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
    }


    private fun permissionListener() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                Toast.makeText(activity, "Permissions is $it", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkPermissions(){
if (!isPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
    permissionListener()
    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
}


    }


    companion object {


        @JvmStatic
        fun newInstance() = MainFragment()
    }
}