package com.deeppowercrew.weathermexml.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.deeppowercrew.weathermexml.R


class HoursFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hours, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}