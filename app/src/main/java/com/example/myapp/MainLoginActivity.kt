package com.example.myapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import fragments.HomeFragment
import fragments.LoginFragment

class MainLoginActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_login_fragment)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if(isLoggedIn==true){
          navigateWithIntent(MainHomeActivity::class.java)
        }else{
            fragmentTransaction.add(R.id.nav_host_fragment_container, LoginFragment())
        }
        fragmentTransaction.commit()


    }


}
