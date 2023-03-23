package fragments

import Data_Class.UserData
import Data_Class.UserDatabase
import Validation.ValidationUtils
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import androidx.core.graphics.toColorInt
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentSignupBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class SignupFragment : Fragment(),DatePickerDialog.OnDateSetListener {
    private  var signupBinding: FragmentSignupBinding?=null
    private val binding get() =signupBinding!!

    private lateinit var userDb:UserDatabase
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signupBinding= FragmentSignupBinding.inflate(inflater,container,false)

        userDb=UserDatabase.getDatabase(requireContext())

        binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_off_24)

        binding.textInputLayoutPassword.setEndIconOnClickListener {
            binding.editTextPassword.let {
                val selectionStart=it.selectionStart
                val selectionEnd= it.selectionEnd
                if(it.transformationMethod== PasswordTransformationMethod.getInstance()){
                    it.transformationMethod= HideReturnsTransformationMethod.getInstance()
                    binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_24)

                }else{
                    it.transformationMethod= PasswordTransformationMethod.getInstance()
                    binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_off_24)
                }
                it.setSelection(selectionStart,selectionEnd)
            }
        }

        binding.buttonSignup.setOnClickListener {
            onSignup()
        }

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

        datePickerDialog = DatePickerDialog(requireContext(),this@SignupFragment, year, month, day)


        //on click of btn dob, datepicker dialog should be open
        binding.buttonDob.setOnClickListener {
            datePickerDialog.datePicker.apply {
                setMaxDate(maxDate)
                setMinDate(minDate)
            }
            datePickerDialog.show()
        }
        return binding.root
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate= String.format("%02d/%02d/%04d",dayOfMonth,month,year)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        binding.buttonDob.text=formattedDate
        getAge(year,currentYear)
    }

    fun getAge(myYear: Int, currentYear: Int){
        val age= currentYear-myYear
        binding.textInputLayoutAge.visibility= View.VISIBLE
        // binding.textInputLayoutAge.hint= age.toString()
        binding.editTextAge.setText(age.toString())
        binding.editTextAge.isEnabled=false
        binding.textInputLayoutAge.hintTextColor= ColorStateList.valueOf(Color.BLACK)


    }
    private fun onSignup(){
        val fname= binding.editTextFname.text.toString()
        val lname= binding.editTextLname.text.toString()
        val email= binding.editTextEmail.text.toString()
        val password= binding.editTextPassword.text.toString()
        val selectedBtn= binding.radioGroup.checkedRadioButtonId
        val gender= requireView().findViewById<RadioButton>(selectedBtn)
        val dob= binding.buttonDob.text.toString()
        val age= binding.editTextAge.text.toString()
        val validation= ValidationUtils(requireContext())

        if(validation.isValidFname(fname) && validation.isValidLname(lname)
            && validation.isValidEmail(email) && validation.isValidPassword(password) && validation.isValidGender(selectedBtn) &&
            validation.isValidDob(dob) && validation.isValidAge(age)){
            val userDet= UserData(null,fname,lname,email,password,gender.text.toString(),dob,age,false)

            GlobalScope.launch(Dispatchers.Main){
                val user= emailExists(email)
                println(user)
                if(user==false){
                    insertData(userDet)
                }

            }







        }
    }

    suspend fun insertData(userDet: UserData){
        withContext(Dispatchers.IO){
            userDb.userDao().insertData(userDet)
        }

        LoginFragment.currentemail=binding.editTextEmail.text.toString()
        println(LoginFragment.currentemail)
        val sharedPreferences = requireActivity().getSharedPreferences(LoginFragment.sharedPrefKey, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("password",null)
        editor.putString("username",null)
        editor.apply()

        //LoginFragment.navigateToFragmentWithBackstack(requireActivity().supportFragmentManager,HomeFragment(),"signuptohome")

        val intent= Intent(requireContext(), MainHomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }

    suspend fun emailExists(email:String):Boolean {
        return withContext(Dispatchers.Main) {

            val user = userDb.userDao().emailExists(email)
            if (user) {
                val snack = Snackbar.make(binding.root, "User is already Registered. Please Login", Snackbar.LENGTH_LONG)
                snack.setTextColor("#fa0505".toColorInt())
                snack.setBackgroundTint("#f0f2f5".toColorInt())
                snack.show()

                return@withContext true

            } else {
                return@withContext false
            }

        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        signupBinding=null
    }

}