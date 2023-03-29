package com.example.myapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class BaseActivity: AppCompatActivity() {

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount>0){
            supportFragmentManager.popBackStack()
        }
        else if(supportFragmentManager.backStackEntryCount==0){
            super.onBackPressed()
            finish()
        }
    }

    fun navigateWithIntent(activityClass:Class<*>){
        val intent= Intent(this,activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
    companion object{
        var isLoggedIn:Boolean= false
    }

}