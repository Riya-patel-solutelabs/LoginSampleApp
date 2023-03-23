package com.example.myapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.myapp.R
import com.example.myapp.databinding.ActivityShowProfileBinding

class ShowProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityShowProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar= binding.toolbarProfile
        toolbar.title="View Profile"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            onBackPressed()
            return true
        }
        if(item.itemId==R.id.menu_edit){
           showProfileDialog()
            overridePendingTransition(0,0)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showProfileDialog() {
        TODO("Not yet implemented")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_appbar_menu,menu)
        return true
    }

}