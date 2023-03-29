package fragments

import Data_Class.UserData
import Data_Class.UserDatabase
import Validation.ValidationUtils
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentEditProfileBinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class EditProfileFragment : BaseFragment(),DatePickerDialog.OnDateSetListener {
    private var editProfileBinding: FragmentEditProfileBinding? = null
    private val binding get() = editProfileBinding!!
    private lateinit var userDb: UserDatabase
    private lateinit var datePickerDialog: DatePickerDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editProfileBinding = FragmentEditProfileBinding.inflate(inflater, container, false)

        userDb = UserDatabase.getDatabase(requireContext())
        val toolbar = binding.toolbar
        toolbar.title = "Edit Profile Screen"

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val userEmail = LoginFragment.currentemail
        println("userEmail profile is $userEmail")

        GlobalScope.launch {
            fetchUser(userEmail)
        }

        val validation = ValidationUtils(requireContext())

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
                    && validation.isValidDob(dob) && validation.isValidAge(age) && validation.isValidGender(
                gender
            )
            if (validated == true) {

                val userDet = UserData(null, fname, lname, email, pass, gender, dob, age, false)
                GlobalScope.launch {
                    updateDetails(userDet)
                }
                Toast.makeText(requireContext(), "User Details Updated", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }



        binding.buttonCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.editTextDob.setOnClickListener {
            setDate()
        }

        binding.imageViewProfile.setOnClickListener {
            replaceHomeFragment(
                requireActivity().supportFragmentManager,
                DisplayProfilePicFragment(),
                "displayPicFragment",
                0
            )
        }

        binding.editTextEmail.alpha=0.5f

        return binding.root
    }

    suspend fun updateDetails(userDet: UserData) {
        withContext(Dispatchers.IO) {
            val user = userDb.userDao().findByEmail(userDet.email!!)
            if (user != null) {
                user.firstname = userDet.firstname
                user.lastname = userDet.lastname
                user.password = userDet.password
                user.email = userDet.email
                user.gender = userDet.gender
                user.dob = userDet.dob
                user.age = userDet.age
                //  user.imagePath= imageData

                println("image= ${user.profilePic}")
                LoginFragment.currentemail = userDet.email!!

                val sharedPreferences = requireActivity().getSharedPreferences(
                  sharedPrefKey,
                    Context.MODE_PRIVATE
                )
                val editor = sharedPreferences.edit()
                editor.putString(Password, user.password)
                editor.apply()


                println(LoginFragment.currentemail)
                userDb.userDao().onUpdate(user)
            }

        }

    }

    fun setDate() {
        //calender
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val minAge = 15
        val maxAge = 60

        val maxYear = year - minAge
        val minYear = year - maxAge


        // set min and max dates
        val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, minYear)
        }.timeInMillis
        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, maxYear)
        }.timeInMillis


        datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)


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
        withContext(Dispatchers.Main) {
            val user = userDb.userDao().findByEmail(userEmail!!)
            if (user != null) {
                binding.editTextFname.setText(user.firstname)
                binding.editTextLname.setText(user.lastname)
                binding.editTextEmail.setText(user.email)
                binding.editTextPassword.setText(user.password)
                binding.editTextGender.setText(user.gender)
                binding.editTextDob.setText(user.dob)
                binding.editTextAge.setText(user.age.toString())

                if (user.profilePic == null) {
                    binding.imageViewProfile.setImageResource(R.drawable.baseline_person_24)
                } else {
                    //val bitmap= BitmapFactory.decodeByteArray(user.profilePic, 0, user.profilePic!!.size)
                    binding.imageViewProfile.setImageURI(Uri.parse(user.profilePic))
                }


            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFragmentManager.popBackStack()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        editProfileBinding=null
    }


}