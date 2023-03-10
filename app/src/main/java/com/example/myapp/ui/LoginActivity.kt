package com.example.myapp.ui

import Data_Class.UserDatabase
import Validation.ValidationUtils
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import com.example.myapp.HomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.ActivityLogin2Binding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityLogin2Binding
    private lateinit var userDb: UserDatabase
    private lateinit var rootView:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogin2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        userDb= UserDatabase.getDatabase(
            this
        )

        rootView = findViewById(android.R.id.content)

        binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_off_24)

        binding.textInputLayoutPassword.setEndIconOnClickListener {
            binding.editTextPassword.let {
                val selectionStart=it.selectionStart
                val selectionEnd= it.selectionEnd
                if(it.transformationMethod==PasswordTransformationMethod.getInstance()){
                    it.transformationMethod=HideReturnsTransformationMethod.getInstance()
                    binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_24)

                }else{
                    it.transformationMethod=PasswordTransformationMethod.getInstance()
                }
                it.setSelection(selectionStart,selectionEnd)
            }
        }

        //check if user data is stored in shared preferences
        val sharedPrefs= getSharedPreferences("user_prefs",Context.MODE_PRIVATE)
        val username= sharedPrefs.getString("username",null)
        val password= sharedPrefs.getString("password",null)

        if(username!=null && password!=null){
            binding.editTextUsername.setText(username)
            binding.editTextPassword.setText(password)
            binding.checkBox.isChecked= true
        }

        binding.buttonLogin.setOnClickListener {
            GlobalScope.launch (Dispatchers.Main){
                loginUser()
            }
        }

        binding.textViewSignup.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
    }

     suspend fun loginUser() {
         val username= binding.editTextUsername.text.toString()
         val password= binding.editTextPassword.text.toString()
         val rememberMe= binding.checkBox.isChecked

         currentemail=username
         val validate= ValidationUtils(this)

         if(validate.isValidEmail(username) && validate.isValidPassword(password)){
             val user= userDb.userDao().findByEmailandPass(username,password)
             if(user!=null){
                 if(rememberMe){
                     user.rememberMe= true

                     GlobalScope.launch(Dispatchers.IO) {
                         userDb.userDao().onUpdate(user)
                     }
                     //store user data in shared prefs

                     val sharedPrefs= getSharedPreferences("user_prefs",Context.MODE_PRIVATE)
                     sharedPrefs.edit{
                         putString("username",username)
                         putString("password",password)
                     }
                 }
                 else if(!rememberMe){
                     val sharedPrefs= getSharedPreferences(sharedPrefKey,Context.MODE_PRIVATE)
                     sharedPrefs.edit{
                         putString("username",null)
                         putString("password",null)
                 }}
                 //delay(500)
                 nextActivity()
             }else{
                 LoginActivity.errorSnackbar("Invalid Credentials",this@LoginActivity)
             }

         }


    }

      fun nextActivity() {

        val intent= Intent(this,HomeActivity::class.java)
         startActivity(intent)

    }

    companion object{
         const val userEmail="current_user"
        const val sharedPrefKey="user_prefs"

         var currentemail=""


        fun errorSnackbar(message: String,context: Context){
            val rootView = (context as Activity).findViewById<View>(android.R.id.content)
            val snack = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
            snack.setTextColor("#fa0505".toColorInt())
            snack.setBackgroundTint("#f0f2f5".toColorInt())
            snack.show()
        }


    }
}