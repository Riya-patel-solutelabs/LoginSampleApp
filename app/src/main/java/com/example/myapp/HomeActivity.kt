package com.example.myapp

import Data_Class.UserDatabase
import Data_Class.UserID
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import com.example.myapp.databinding.ActivityHomeBinding
import com.example.myapp.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var binding: ActivityHomeBinding
    private lateinit var userDb: UserDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userDb= UserDatabase.getDatabase(this)
        val userEmail= LoginActivity.currentemail
        println("userEmail home is $userEmail")

        val toolbar= binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        bottomNav= binding.bottomNavigationView
        // Set Home selected
        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId) {
                R.id.home -> {
                    // Load HomeActivity
                    true
            }
                R.id.profile->{
                    // Load Profile Activity
                    val intent= Intent(this,DisplayProfile::class.java)
                    intent.putExtra(LoginActivity.userEmail,userEmail)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    startActivity(Intent(this,SettingActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                else -> false
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        val logoutMenuItem = menu!!.findItem(R.id.logout)
        logoutMenuItem.setIcon(R.drawable.baseline_power_settings_new_24)

        val deleteMenuItem = menu.findItem(R.id.delete_user)
        deleteMenuItem.setIcon(R.drawable.baseline_delete_24)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id= item.itemId

        if(id==R.id.logout) {
            finish()
            val intent= Intent(this,LoginActivity::class.java)
            intent.putExtra(LoginActivity.userEmail,LoginActivity.currentemail)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return true
        }
        if(id==R.id.delete_user){
            deleteData()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteData() {
       val email=LoginActivity.currentemail

        GlobalScope.launch { withContext(Dispatchers.IO){
            val user= userDb.userDao().findByEmail(email)
            if(user!=null){
                userDb.userDao().deleteData(user)
            }
        } }
        Toast.makeText(this,"User Deleted Successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))
    }

    private  fun loadFragment(fragment: Fragment){
        val fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}