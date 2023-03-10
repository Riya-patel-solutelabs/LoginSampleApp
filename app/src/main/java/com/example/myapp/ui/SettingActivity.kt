package com.example.myapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapp.HomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav= binding.bottomNavigationView
        // Set Setting selected
        bottomNav.selectedItemId = R.id.settings

        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId) {
                R.id.home -> {
                    // Load HomeActivity
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.profile->{
                    startActivity(Intent(this, DisplayProfile::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    true
                }
                else -> false
            }

        }
    }
    }
