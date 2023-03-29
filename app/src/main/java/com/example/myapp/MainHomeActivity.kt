package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapp.databinding.ActivityMainHomeBinding


import fragments.HomeFragment
import fragments.LoginFragment
import fragments.ProfileFragment
import fragments.SettingFragment




class MainHomeActivity : BaseActivity() {
    private lateinit var binding : ActivityMainHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.fragment_container_home,HomeFragment())
        fragmentTransaction.commit()

    }


//    private fun replaceFragment(fragment:Fragment,tag:String){
//        val transaction= supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.fragment_container_home,fragment)
//        //transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_exit_left,R.anim.slide_in_left,R.anim.slide_exit_right)
//        //transaction.addToBackStack(tag)
//        transaction.commit()
//
//    }
}