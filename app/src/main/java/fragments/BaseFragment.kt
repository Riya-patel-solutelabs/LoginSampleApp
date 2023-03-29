package fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment: Fragment() {

    fun showSnackbar(message: String,context: Context) {
        val rootView = (context as Activity).findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView,message, Snackbar.LENGTH_LONG)
        snackbar.setTextColor("#fa0505".toColorInt())
        snackbar.setBackgroundTint("#f0f2f5".toColorInt())
        snackbar.show()
    }

    fun replaceFragment(fragmentManager: FragmentManager,fragment:Fragment,tag:String,flag:Int){
        val transaction= fragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_container,fragment,tag)
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_exit_left,
            R.anim.slide_in_left,
            R.anim.slide_exit_right)
        if(flag==0){
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    fun popExclusive(name:String){
        requireActivity().supportFragmentManager.popBackStack(name,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
    fun replaceHomeFragment(fragmentManager: FragmentManager,fragment:Fragment,tag:String,flag:Int){
        val transaction= fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container_home,fragment,tag)
        transaction.setCustomAnimations(
            R.anim.slide_in_right,
            R.anim.slide_exit_left,
            R.anim.slide_in_left,
            R.anim.slide_exit_right)
        if(flag==0){
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    fun navigateWithIntent(activityClass:Class<*>){
        val intent= Intent(requireContext(),activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        const val Username:String="username"
        const val Password:String="password"
        const val sharedPrefKey="user_prefs"
    }
}