package fragments

import Data_Class.UserDatabase
import Validation.ValidationUtils
import android.content.Context
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import com.example.myapp.BaseActivity
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment() {
    private var loginBinding: FragmentLoginBinding? = null
    private lateinit var userDb: UserDatabase

    private val binding get() = loginBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        userDb = UserDatabase.getDatabase(requireContext())

        // rootView = findViewById(android.R.id.content)

        binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_off_24)

        binding.textInputLayoutPassword.setEndIconOnClickListener {
            binding.editTextPassword.let {
                val selectionStart = it.selectionStart
                val selectionEnd = it.selectionEnd
                if (it.transformationMethod == PasswordTransformationMethod.getInstance()) {
                    it.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_24)

                } else {
                    it.transformationMethod = PasswordTransformationMethod.getInstance()
                    binding.textInputLayoutPassword.setEndIconDrawable(R.drawable.baseline_visibility_off_24)
                }
                it.setSelection(selectionStart, selectionEnd)
            }

        }

        //check if user data is stored in shared preferences
        val sharedPrefs =
            requireActivity().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
        val username = sharedPrefs.getString(Username, null)
        val password = sharedPrefs.getString(Password, null)

        if (username != null && password != null) {
            binding.editTextUsername.setText(username)
            binding.editTextPassword.setText(password)
            binding.checkBox.isChecked = true
        }

        binding.buttonLogin.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                loginUser()
            }
        }
        binding.textViewSignup.setOnClickListener {
            // startActivity(Intent(this, SignupActivity::class.java))
            replaceFragment(
                requireActivity().supportFragmentManager,
                SignupFragment(),
                getString(R.string.label_login_to_signup),
                0
            )
        }
        return binding.root
    }

    suspend fun loginUser() {
        val username = binding.editTextUsername.text.toString()
        val password = binding.editTextPassword.text.toString()
        val rememberMe = binding.checkBox.isChecked

        currentemail = username
        val validate = ValidationUtils(requireContext())

        if (validate.isValidEmail(username) && validate.isValidPassword(password)) {
            val user = userDb.userDao().findByEmailandPass(username, password)
            if (user != null) {
                if (rememberMe) {
                    user.rememberMe = true

                    GlobalScope.launch(Dispatchers.IO) {
                        userDb.userDao().onUpdate(user)
                    }
                    //store user data in shared prefs

                    val sharedPrefs =
                        requireActivity().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
                    sharedPrefs.edit {
                        putString(Username, username)
                        putString(Password, password)
                    }
                } else if (!rememberMe) {
                    val sharedPrefs =
                        requireActivity().getSharedPreferences(sharedPrefKey, Context.MODE_PRIVATE)
                    sharedPrefs.edit {
                        putString(Username, null)
                        putString(Password, null)
                    }
                }
                //delay(500)
                BaseActivity.isLoggedIn = true
                navigateWithIntent(MainHomeActivity::class.java)
                //replaceFragment(requireActivity().supportFragmentManager,HomeFragment(),getString(R.string.label_login_to_home),0)
            } else {
                showSnackbar("Invalid Credentials", requireContext())
            }

        }

    }


    companion object {
        var currentemail = ""
    }


    override fun onDestroyView() {
        super.onDestroyView()
        loginBinding = null
    }
}