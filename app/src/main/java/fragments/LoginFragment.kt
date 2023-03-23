package fragments

import Data_Class.UserDatabase
import Validation.ValidationUtils
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.graphics.toColorInt
import androidx.fragment.app.FragmentManager
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {
    private  var loginBinding: FragmentLoginBinding?= null
    private lateinit var userDb: UserDatabase

    private val binding get() = loginBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loginBinding= FragmentLoginBinding.inflate(inflater,container,false)
        userDb= UserDatabase.getDatabase(requireContext())

       // rootView = findViewById(android.R.id.content)

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

//            if (binding.textInputLayoutPassword.endIconMode == TextInputLayout.END_ICON_PASSWORD_TOGGLE) {
//                binding.editTextPassword.transformationMethod = null
//                binding.textInputLayoutPassword.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE_OFF
//            } else {
//                binding.editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
//                binding.textInputLayoutPassword.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
//            }
        }

        //check if user data is stored in shared preferences
        val sharedPrefs= requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username= sharedPrefs.getString("username",null)
        val password= sharedPrefs.getString("password",null)

        if(username!=null && password!=null){
            binding.editTextUsername.setText(username)
            binding.editTextPassword.setText(password)
            binding.checkBox.isChecked= true
        }

        binding.buttonLogin.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                loginUser()
            }
        }
        binding.textViewSignup.setOnClickListener {
           // startActivity(Intent(this, SignupActivity::class.java))
            navigateToFragmentWithBackstack(requireActivity().supportFragmentManager,SignupFragment(),"loginToSignup")
        }
        return binding.root
    }
    suspend fun loginUser() {
        val username= binding.editTextUsername.text.toString()
        val password= binding.editTextPassword.text.toString()
        val rememberMe= binding.checkBox.isChecked

        currentemail =username
        val validate= ValidationUtils(requireContext())

        if(validate.isValidEmail(username) && validate.isValidPassword(password)){
            val user= userDb.userDao().findByEmailandPass(username,password)
            if(user!=null){
                if(rememberMe){
                    user.rememberMe= true

                    GlobalScope.launch(Dispatchers.IO) {
                        userDb.userDao().onUpdate(user)
                    }
                    //store user data in shared prefs

                    val sharedPrefs= requireActivity().getSharedPreferences(sharedPrefKey,Context.MODE_PRIVATE)
                    sharedPrefs.edit{
                        putString("username",username)
                        putString("password",password)
                    }
                }
                else if(!rememberMe){
                    val sharedPrefs= requireActivity().getSharedPreferences(sharedPrefKey,Context.MODE_PRIVATE)
                    sharedPrefs.edit{
                        putString("username",null)
                        putString("password",null)
                    }}
                //delay(500)
                val intent= Intent(requireContext(),MainHomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                //navigateToFragmentWithBackstack(requireActivity().supportFragmentManager,HomeFragment(),"loginToHome")
            }else{
                errorSnackbar("Invalid Credentials",requireContext())
            }

        }


    }


    companion object{
        const val sharedPrefKey="user_prefs"
        var currentemail=""


        fun errorSnackbar(message: String,context: Context){
            val rootView = (context as Activity).findViewById<View>(android.R.id.content)
            val snack = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
            snack.setTextColor("#fa0505".toColorInt())
            snack.setBackgroundTint("#f0f2f5".toColorInt())
            snack.show()
        }
        fun navigateToFragmentWithBackstack(fragmentManager: FragmentManager, destination: Fragment, tag:String){
            val transaction= fragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_exit_left,R.anim.slide_in_left,R.anim.slide_exit_right)
            transaction.replace(R.id.nav_host_fragment_container,destination)
            transaction.addToBackStack(tag)
            transaction.commit()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        loginBinding= null
    }
}