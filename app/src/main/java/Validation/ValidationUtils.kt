package Validation

import Data_Class.UserDatabase
import android.app.Activity
import android.content.Context
import android.util.Patterns
import android.view.View
import androidx.core.graphics.toColorInt
import com.google.android.material.snackbar.Snackbar

class ValidationUtils(private val context: Context) {
    val rootView = (context as Activity).findViewById<View>(android.R.id.content)
    var hasNumber= false
    var hasUpper=false
    var hasLower= false
    var hasSpecial= false
    var hasLength= false
    var validgender=false

    fun isValidFname(fname:String):Boolean{
        if(fname.isNotEmpty()){
            return true
        }else{
            showSnackbar("First name cannot be empty")
        }
        return false
    }
    fun isValidLname(lname:String):Boolean{
        if(lname.isNotEmpty()){
            return true
        }else{
            showSnackbar("Last name cannot be empty")
        }
        return false
    }
    fun isValidHobby(hobby:String):Boolean{
        if(hobby.isNotEmpty()){
            return true
        }else{
            showSnackbar("Hobby cannot be empty")
        }
        return false
    }
    fun isValidGender(selectBtn:Int):Boolean{
        if(selectBtn ==-1){
            showSnackbar("Gender cannot be empty")
        }else{
           return true
        }
        return false
    }
    fun isValidDob(dob:String):Boolean{
        if(dob.isNotEmpty()){
            return true
        }else{
            showSnackbar("DOB cannot be empty")
        }
        return false
    }
    fun isValidAge(age:String):Boolean{
        if(age.isNotEmpty()){
            return true
        }else{
            showSnackbar("Age cannot be empty")
        }
        return false
    }

    fun isValidEmail(email: String):Boolean{
        if(email.isEmpty()) {
            showSnackbar("Email cannot be empty")
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true
        }else{
            showSnackbar("Enter Valid Email Id")
        }
        return false
    }


    fun isValidPassword(password: String): Boolean {
        if (password.isEmpty()) {
            showSnackbar("Password cannot be empty")
            return false
        }
        if (password.length < 8) {
            showSnackbar("Length of password should be greater than 8")
            return false
        }

        var hasLower = false
        var hasUpper = false
        var hasNumber = false
        var hasSpecial = false

        for (cha in password) {
            if (Character.isLowerCase(cha)) {
                hasLower = true
            }
            if (Character.isUpperCase(cha)) {
                hasUpper = true
            }
            if (Character.isDigit(cha)) {
                hasNumber = true
            }
            if (!Character.isLetterOrDigit(cha)) {
                hasSpecial = true
            }
        }

        if (!hasLower) {
            showSnackbar("Password must contain lower case")
            return false
        }
        if (!hasUpper) {
            showSnackbar("Password must contain upper case")
            return false
        }
        if (!hasNumber) {
            showSnackbar("Password must contain a digit")
            return false
        }
        if (!hasSpecial) {
            showSnackbar("Password must contain a special character")
            return false
        }

        return true
    }



    fun isValidGender(gender:String):Boolean{
        if(gender=="Male" || gender=="male"){
            validgender=true
        }
        else if(gender=="Female" || gender=="female"){
            validgender=true
        }else{
            showSnackbar("Gender should be Male or Female")
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