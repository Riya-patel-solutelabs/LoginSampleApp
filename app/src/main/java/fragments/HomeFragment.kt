package fragments

import Data_Class.UserDatabase
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.myapp.BaseActivity
import com.example.myapp.MainHomeActivity
import com.example.myapp.MainLoginActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentHomeBinding


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {
    private var homeBinding: FragmentHomeBinding?= null
    private val binding get() = homeBinding!!
    private lateinit var userDb: UserDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding= FragmentHomeBinding.inflate(inflater,container,false)
        val currentUser= LoginFragment.currentemail
        println(currentUser)

        val toolbar= binding.toolbar
        toolbar.inflateMenu(R.menu.home_menu)
        toolbar.title="Home Screen"
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        userDb= UserDatabase.getDatabase(requireContext())
        val bottomNav= binding.bottomNavigationView
        // Set Home selected
        bottomNav.selectedItemId = R.id.home

        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId) {

                R.id.home -> {
                    // Load HomeActivity
                    true
                }
                R.id.profile->{
                    // Load Profile Activity
                   replaceHomeFragment(requireActivity().supportFragmentManager,ProfileFragment(),"profileFragment",0)
                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    replaceHomeFragment(requireActivity().supportFragmentManager,SettingFragment(),"settingFragment",0)
                    true
                }
                else -> false
            }

        }



    return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding=null
    }

    override fun onResume() {
        super.onResume()
        val navView = binding.bottomNavigationView
        navView.selectedItemId=R.id.home // set the checked item to the ID of the home Fragment
        popExclusive(getString(R.string.label_login_to_home))
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.logout->{
                BaseActivity.isLoggedIn=false
                val intent = Intent(requireActivity(), MainLoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()

                return true
            }

            R.id.delete_user->{
               deleteData()
                return true
            }

            else->super.onOptionsItemSelected(item)
        }

    }

    fun deleteData(){
        val currentEmail= LoginFragment.currentemail
        GlobalScope.launch(Dispatchers.IO) {
            val user=userDb.userDao().findByEmail(currentEmail)
            if(user!=null){
                userDb.userDao().deleteData(user)
            }
            val sharedPreferences=requireActivity().getSharedPreferences(sharedPrefKey,Context.MODE_PRIVATE)
            val editor=sharedPreferences.edit()
            editor.putString(Username,null)
            editor.putString(Password,null)
            editor.apply()
        }
        startActivity(Intent(requireContext(),MainLoginActivity::class.java))

    }
}