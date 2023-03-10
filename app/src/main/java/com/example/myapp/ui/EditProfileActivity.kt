package com.example.myapp.ui

import Data_Class.UserData
import Data_Class.UserDatabase
import Validation.ValidationUtils
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.example.myapp.databinding.ActivityEditProfileBinding

import kotlinx.coroutines.*
import java.util.*

class EditProfileActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userDb:UserDatabase
    private lateinit var datePickerDialog: DatePickerDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDb = UserDatabase.getDatabase(this)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        toolbar.title="Edit Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val userEmail = LoginActivity.currentemail
        println("userEmail profile is $userEmail")


        GlobalScope.launch {
            fetchUser(userEmail)
        }


        // user= userDb.userDao().getAll()

        val validation = ValidationUtils(this)

        binding.buttonSave.setOnClickListener {
            val fname = binding.editTextFname.text.toString()
            val lname = binding.editTextLname.text.toString()
            val email = binding.editTextEmail.text.toString()
            val pass = binding.editTextPassword.text.toString()
            val gender = binding.editTextGender.text.toString()
            val dob = binding.editTextDob.text.toString()
            val age = binding.editTextAge.text.toString()
            val validated = validation.isValidFname(fname) && validation.isValidLname(lname) &&
                    validation.isValidEmail(email) && validation.isValidPassword(pass)
                    && validation.isValidDob(dob) && validation.isValidAge(age) &&validation.isValidGender(gender)
            if (validated == true) {

                val userDet = UserData(null, fname, lname, email, pass, gender, dob, age,false)
                GlobalScope.launch {
                    updateDetails(userDet)
                }
                Toast.makeText(this,"UserData Updated",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DisplayProfile::class.java))
            }
        }

        binding.buttonCancel.setOnClickListener {
            startActivity(Intent(this, DisplayProfile::class.java))
        }
        binding.editTextDob.setOnClickListener {
            setDate()
        }

        }



    suspend fun updateDetails(userDet: UserData){
        withContext(Dispatchers.IO){
            val user= userDb.userDao().findByEmail(userDet.email!!)
            if(user!=null){
                user.firstname= userDet.firstname
                user.lastname= userDet.lastname
                user.password= userDet.password
                user.email= userDet.email
                user.gender= userDet.gender
                user.dob= userDet.dob
                user.age=userDet.age
                LoginActivity.currentemail= userDet.email!!

                val sharedPreferences = getSharedPreferences(LoginActivity.sharedPrefKey, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("password",user.password)
                editor.apply()


                println(LoginActivity.currentemail)
                userDb.userDao().onUpdate(user)
            }

        }

    }
    fun setDate(){
        //calender
        val cal= Calendar.getInstance()
        val year= cal.get(Calendar.YEAR)
        val month= cal.get(Calendar.MONTH)
        val day= cal.get(Calendar.DAY_OF_MONTH)
        val minAge=15
        val maxAge=60

        val maxYear= year-minAge
        val minYear= year-maxAge


        // set min and max dates
        val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, minYear)
        }.timeInMillis
        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, maxYear)
        }.timeInMillis


        datePickerDialog = DatePickerDialog(this@EditProfileActivity,this@EditProfileActivity, year, month, day)


        //on click of btn dob, datepicker dialog should be open
        binding.editTextDob.setOnClickListener {
            datePickerDialog.datePicker.apply {
                setMaxDate(maxDate)
                setMinDate(minDate)
            }
            datePickerDialog.show()
        }

    }



    suspend fun fetchUser(userEmail: String?) {
        withContext(Dispatchers.Main){
            val user= userDb.userDao().findByEmail(userEmail!!)
            if(user!=null){
                binding.editTextFname.setText(user.firstname)
                binding.editTextLname.setText(user.lastname)
                binding.editTextEmail.setText(user.email)
                binding.editTextPassword.setText(user.password)
                binding.editTextGender.setText(user.gender)
                binding.editTextDob.setText(user.dob)
                binding.editTextAge.setText(user.age.toString())




            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            onBackPressed()
            return true
        }
//        if(item.itemId==R.id.menu_logout){
//            finish()
//            val intent= Intent(this,MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
//            return true
//        }
        return super.onOptionsItemSelected(item)
    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate= String.format("%02d/%02d/%04d",dayOfMonth,month+1,year)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.editTextDob.setText(formattedDate)
        getAge(year,currentYear)
    }
    fun getAge(myYear: Int, currentYear: Int){
        val age= currentYear-myYear
        binding.editTextAge.setText(age.toString())
        binding.editTextAge.isEnabled=false


    }


}