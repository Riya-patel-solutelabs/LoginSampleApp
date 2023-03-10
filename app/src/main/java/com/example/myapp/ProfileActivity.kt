package com.example.myapp

import Data_Class.UserID
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class ProfileActivity : AppCompatActivity() {

     private lateinit var welcome:TextView
     private lateinit var username:TextView
     private lateinit var hobby:TextView
     private lateinit var age:TextView
     private lateinit var dob:TextView
     private lateinit var gender:TextView
     private lateinit var toolbar: Toolbar
     private lateinit var nextbtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        nextbtn= findViewById(R.id.button_next)
        welcome=findViewById(R.id.textView_welcome_user)
        username= findViewById(R.id.textView_username)
        hobby= findViewById(R.id.textView_hobby)
        age=findViewById(R.id.textView_age)
        dob= findViewById(R.id.textView_dob)
        gender=findViewById(R.id.textView_gender)


        val userDet: UserID = intent.getParcelableExtra("user_det")!!

        welcome.text= buildString {
        append("Welcome, ")
        append(userDet.ufname)
    }
        username.text= buildString {
            append(userDet.ufname)
            append(" ")
            append(userDet.ulname)
    }
        hobby.text= userDet.uemail
        gender.text=userDet.ugender
        age.text= userDet.uage
        dob.text= userDet.udob


        //navigate to recycleractivity on click of next btn
        nextbtn.setOnClickListener {
            val intent=Intent(this,RecyclerActivity::class.java)
            startActivity(intent)
        }
    }


    // to enable the back btn
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_appbar_menu, menu)
        return true
    }

}