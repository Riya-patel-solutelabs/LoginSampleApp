package com.example.myapp.ui

import Data_Class.UserDatabase
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.myapp.HomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.ActivityDisplayProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayProfile : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayProfileBinding
    private lateinit var userDb:UserDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDisplayProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userDb= UserDatabase.getDatabase(this)

        val toolbar= binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val bottomNav= binding.bottomNavigationView
        // Set Home selected
        bottomNav.selectedItemId = R.id.profile

        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId) {
                R.id.home -> {
                    // Load HomeActivity
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.profile->{
                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    startActivity(Intent(this, SettingActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> false
            }

        }


        val userEmail= LoginActivity.currentemail
        println("userEmail display profile is $userEmail")


        GlobalScope.launch {
            fetchUser(userEmail)


        }


        // user= userDb.userDao().getAll()
    }
    suspend fun fetchUser(userEmail: String?) {
        withContext(Dispatchers.Main){
            val user= userDb.userDao().findByEmail(userEmail!!)
            if(user!=null){
                binding.textViewFname.text= user.firstname.toString()
                binding.textViewLname.text=user.lastname
                binding.textViewEmail.text=user.email
                binding.textViewPass.text=user.password
                binding.textViewGender.text=user.gender
                binding.textViewDob.text= user.dob
                binding.textViewAge.text= user.age.toString()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.menu_edit){
            val intent= Intent(this,EditProfileActivity::class.java)
            intent.putExtra(LoginActivity.userEmail,binding.textViewEmail.text)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_appbar_menu, menu)
        return true
    }

}