package fragments

import Data_Class.UserDatabase
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileFragment : Fragment() {
    private var profileBinding:FragmentProfileBinding?=null
    private val binding get()= profileBinding!!
    private lateinit var userDb: UserDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        profileBinding= FragmentProfileBinding.inflate(inflater,container,false)
        userDb= UserDatabase.getDatabase(requireContext())

        val toolbar=binding.toolbar
        toolbar.title="Profile Screen"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        setHasOptionsMenu(true)
        val userEmail= LoginFragment.currentemail
        println("userEmail display profile is $userEmail")

        val bottomNav= binding.bottomNavigationView
        bottomNav.selectedItemId= R.id.profile
        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId) {
                R.id.home -> {
                    // Load HomeActivity
                    MainHomeActivity.replaceFragment(requireActivity().supportFragmentManager,HomeFragment(),"homeFragment")
                    true
                }
                R.id.profile->{
                    // Load Profile Activity

                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    MainHomeActivity.replaceFragment(requireActivity().supportFragmentManager,SettingFragment(),"settingFragment")
                    true
                }
                else -> false
            }

        }



        GlobalScope.launch {
            fetchUser(userEmail)
        }


        return binding.root
    }

    suspend fun fetchUser(userEmail: String?) {
        withContext(Dispatchers.Main){
            val user= userDb.userDao().findByEmail(userEmail!!)
            if(user!=null){
                binding.textViewFname.text= user.firstname.toString()
                binding.textViewLname.text=user.lastname
                binding.textViewEmail.text=user.email
                binding.textViewPass.text=user.password
                binding.textViewGender.text=user.gender
                binding.textViewDob.text= user.dob
                binding.textViewAge.text= user.age.toString()

                if(user.profilePic==null){
                    binding.imageViewProfile.setImageResource(R.drawable.baseline_person_24)
                }else{
                    //val bitmap= BitmapFactory.decodeByteArray(user.imagePath, 0, user.imagePath!!.size)
                    binding.imageViewProfile.setImageURI(Uri.parse(user.profilePic))
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        profileBinding= null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_edit->{
                MainHomeActivity.replaceFragment(requireActivity().supportFragmentManager,EditProfileFragment(),"profiletoEditProfile")
            }

                //EditProfileFragment.navigateWithoutBackstack(requireActivity().supportFragmentManager,EditProfileFragment())


        }
        return super.onOptionsItemSelected(item)

    }
    override fun onResume() {
        super.onResume()
        val navView = binding.bottomNavigationView
        navView.selectedItemId=R.id.profile // set the checked item to the ID of the home Fragment
        requireActivity().supportFragmentManager.popBackStack("profiletoEditProfile",FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_appbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


}