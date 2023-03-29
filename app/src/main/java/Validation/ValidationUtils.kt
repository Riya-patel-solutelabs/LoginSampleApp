package Validation

import Data_Class.UserDatabase
import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.View
import androidx.core.graphics.toColorInt
import com.example.myapp.R
import com.google.android.material.snackbar.Snackbar
import fragments.BaseFragment

class ValidationUtils(private val context: Context) {
    val rootView = (context as Activity).findViewById<View>(android.R.id.content)
    var validgender=false

    fun isValidFname(fname:String):Boolean{
        if(fname.isNotEmpty()){
            return true
        }else{
            showSnackbar(context.getString(R.string.label_fname_error))
        }
        return false
    }
    fun isValidLname(lname:String):Boolean{
        if(lname.isNotEmpty()){
            return true
        }else{
            showSnackbar(context.getString(R.string.label_lname_error))
        }
        return false
    }
    fun isValidHobby(hobby:String):Boolean{
        if(hobby.isNotEmpty()){
            return true
        }else{
            showSnackbar(context.getString(R.string.label_hobby_error))
        }
        return false
    }
    fun isValidGender(selectBtn:Int):Boolean{
        if(selectBtn ==-1){
            showSnackbar(context.getString(R.string.label_gender_error))
        }else{
           return true
        }
        return false
    }
    fun isValidDob(dob:String):Boolean{
        if(dob.isNotEmpty()){
            return true
        }else{
            showSnackbar(context.getString(R.string.label_dob_error))
        }
        return false
    }
    fun isValidAge(age:String):Boolean{
        if(age.isNotEmpty()){
            return true
        }else{
            showSnackbar(context.getString(R.string.label_age_error))
        }
        return false
    }

    fun isValidEmail(email: String):Boolean{
        val regex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}")
        if(email.isEmpty()) {
            showSnackbar(context.getString(R.string.label_empty_email))
        }else if(regex.matches(email)){
            return true
        }
//        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            return true
//        }
      else{
            showSnackbar(context.getString(R.string.label_valid_email_error))
        }
        return false
    }


    fun isValidPassword(password: String): Boolean {
        val regex= Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}\$")
        if (password.isEmpty()) {
            showSnackbar(context.getString(R.string.lael_password_error))
            return false
        }
        if(regex.matches(password)){
            return true
        }else{
            showSnackbar(context.getString(R.string.label_password_error))
        }
//        if (password.length < 8) {
//            showSnackbar("Length of password should be greater than 8")
//            return false
//        }
//
//        var hasLower = false
//        var hasUpper = false
//        var hasNumber = false
//        var hasSpecial = false
//
//        for (cha in password) {
//            if (Character.isLowerCase(cha)) {
//                hasLower = true
//            }
//            if (Character.isUpperCase(cha)) {
//                hasUpper = true
//            }
//            if (Character.isDigit(cha)) {
//                hasNumber = true
//            }
//            if (!Character.isLetterOrDigit(cha)) {
//                hasSpecial = true
//            }
//        }
//
//        if (!hasLower) {
//            showSnackbar("Password must contain lower case")
//            return false
//        }
//        if (!hasUpper) {
//            showSnackbar("Password must contain upper case")
//            return false
//        }
//        if (!hasNumber) {
//            showSnackbar("Password must contain a digit")
//            return false
//        }
//        if (!hasSpecial) {
//            showSnackbar("Password must contain a special character")
//            return false
//        }

        return false
    }



    fun isValidGender(gender:String):Boolean{
        if(gender=="Male" || gender=="male"){
            validgender=true
        }
        else if(gender=="Female" || gender=="female"){
            validgender=true
        }else{
            showSnackbar(context.getString(R.string.label_gender_select_error))
        }
            return validgender
    }


    fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(rootView,message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#fa0505".toColorInt())
        snackbar.setBackgroundTint("#f0f2f5".toColorInt())
        snackbar.show()
    }
}