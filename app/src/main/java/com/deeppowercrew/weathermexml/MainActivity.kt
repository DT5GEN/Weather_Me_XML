package com.deeppowercrew.weathermexml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.deeppowercrew.weathermexml.ui.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, MainFragment.newInstance())
            .commit()
    }
}