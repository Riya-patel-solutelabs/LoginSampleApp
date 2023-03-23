package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapp.databinding.ActivityMainHomeBinding
import fragments.HomeFragment
import fragments.ProfileFragment
import fragments.SettingFragment




class MainHomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainHomeBinding
    private val homeFragment= HomeFragment()
    private val profileFragment= ProfileFragment()
    private val settingFragment= SettingFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(supportFragmentManager,HomeFragment(),"homeFragment")

    }

    companion object{

        fun replaceFragment(fragmentManager: FragmentManager,fragment: Fragment,tag:String){
            val transaction= fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_home,fragment,tag)
            transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_exit_left,R.anim.slide_in_left,R.anim.slide_exit_right)
            transaction.addToBackStack(tag)
            transaction.commit()


        }

    }

    override fun onBackPressed() {
       if(supportFragmentManager.backStackEntryCount>0){
           supportFragmentManager.popBackStack()
       }else{
           finishAfterTransition()
       }
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