package com.example.myapp

import Data_Class.UserID
import Validation.ValidationUtils
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener{
    private lateinit var btnDatePicker: Button
    private lateinit var btnHobbyDialog: Button
    private lateinit var txtAge: TextInputEditText
    private lateinit var ageLayout: TextInputLayout
    private lateinit var btnRadioMale: RadioButton
    private lateinit var btnRadioFemale: RadioButton
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var btnSubmit: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var datePickerDialog: DatePickerDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        btnHobbyDialog = findViewById(R.id.button_hobby)
        btnHobbyDialog.setOnClickListener {
            showDialogBox()
        }

        btnRadioMale = findViewById(R.id.radioButton_male)
        btnRadioFemale = findViewById(R.id.radioButton_female)
        btnDatePicker = findViewById(R.id.button_dob)
        txtAge = findViewById(R.id.editText_age)
        ageLayout = findViewById(R.id.textInputLayout_age)
        firstName = findViewById(R.id.editText_fname)
        lastName = findViewById(R.id.editText_lname)
        btnSubmit = findViewById(R.id.button_submit)
        radioGroup=findViewById(R.id.radioGroup_1)

        val bundle= Bundle()


        //on click of submit button
        val myValidation= ValidationUtils(this)

        btnSubmit.setOnClickListener {
            val fname:String= firstName.text.toString()
            val lname:String=lastName.text.toString()
            val hobby:String= btnHobbyDialog.text.toString()
            val selectBtn:Int= radioGroup.checkedRadioButtonId
            val gender= findViewById<RadioButton>(selectBtn)
            val dob:String= btnDatePicker.text.toString()
            val age:String= ageLayout.hint.toString()

            if(myValidation.isValidFname(fname) && myValidation.isValidLname(lname)&& myValidation.isValidHobby(hobby)
                && myValidation.isValidGender(selectBtn) && myValidation.isValidDob(dob) && myValidation.isValidAge(age)){
                Toast.makeText(this,"User Registered Successfully",Toast.LENGTH_SHORT).show()
            //    val userDet= UserID(fname,lname,hobby,gender.text.toString(),dob,age)

               // bundle.putParcelable("user_det",userDet)
                val intent= Intent(this,ProfileActivity::class.java)
              //  intent.putExtra("user_det",userDet)
                startActivity(intent)

            }else{
               // Toast.makeText(this,"InValid User",Toast.LENGTH_SHORT).show()
            }
        }



        //calender
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val minAge=15
        val maxAge=60
        val currenYear= cal.get(Calendar.YEAR)
        val minYear= currenYear-maxAge
        val maxYear= currenYear-minAge


        datePickerDialog = DatePickerDialog(this@MainActivity,this@MainActivity, year, month, day)

        // set min and max dates
        val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, minYear)
        }.timeInMillis
        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, maxYear)
        }.timeInMillis


        //onclick of btn show calender dialog
        btnDatePicker.setOnClickListener {

            datePickerDialog.datePicker.apply {
                setMinDate(minDate)
                setMaxDate(maxDate)
            }
            datePickerDialog.show()


        }
    }

    private fun showDialogBox() {
        // initialise the list items for the alert dialog
        val listItems =
            arrayOf("Cricket", "Dancing", "Listening Music", "Reading", "Singing", "Traveling")
        val checkedItems = BooleanArray(listItems.size)
        //create instance variable for alert dialog to allocate memory only once
        var myDialog: AlertDialog? = null

        //convert array into list
      //  val selectedItems = mutableListOf(*listItems)
        val selectedItems= ArrayList<Int>()
        btnHobbyDialog.setOnClickListener {
            btnHobbyDialog.text = null
            if (myDialog==null) {
                // initialise the alert dialog builder
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Choose Your Hobbies")
                //multiple choice builder
                builder.setMultiChoiceItems(listItems, checkedItems) { _, which, isChecked ->
//                    checkedItems[which] = isChecked
                    if (isChecked) {
                        selectedItems.add(which)
                    } else {
                        selectedItems.remove(Integer.valueOf(which))
                    }
                }
                builder.setCancelable(false)


                builder.setPositiveButton("OK") { _, _ ->
                    val sb = StringBuilder()
                    for (i in selectedItems.indices) {
                        sb.append(listItems[selectedItems[i]])
                        if (i < selectedItems.size - 1) {
                            sb.append(", ")
                        }
                    }
                    btnHobbyDialog.text = sb.toString()
                    // set the text to a TextView or EditText
                }

                //negative btn
                builder.setNegativeButton("CANCEL") { dialog, which -> dialog.dismiss() }

                //clear btn
                builder.setNeutralButton("CLEAR ALL") { dialog: DialogInterface?, which: Int ->
                    Arrays.fill(checkedItems, false)
                }
                myDialog = builder.create()
            }
            myDialog?.show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val myYear = year
        val myMonth = month
        val myDay = dayOfMonth

        val formattedDate= String.format("%02d/%02d/%04d",myDay,myMonth,myYear)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        btnDatePicker.text=formattedDate
        getAge(myYear)


    }

    fun getAge(year: Int) {

        ageLayout.visibility = View.VISIBLE

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val age = currentYear - year
        txtAge.isEnabled = false

        if (age > 60 || age < 15) {
            ageLayout.error = "Age should be between 15-60"
        } else {
            ageLayout.error = null }

        ageLayout.hint = "Age: $age"
    }


}






