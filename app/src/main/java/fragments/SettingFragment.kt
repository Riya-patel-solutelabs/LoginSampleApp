package fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var settingFragment:FragmentSettingBinding?=null
    private val binding get() = settingFragment!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        settingFragment= FragmentSettingBinding.inflate(inflater,container,false)

        val bottomNav= binding.bottomNavigationView
        // Set Setting selected
        bottomNav.selectedItemId = R.id.settings

        bottomNav.setOnNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId) {
                R.id.home -> {
                    // Load HomeActivity
                    MainHomeActivity.replaceFragment(requireActivity().supportFragmentManager,HomeFragment(),"homeFragment")
                  true
                }
                R.id.profile->{
                    MainHomeActivity.replaceFragment(requireActivity().supportFragmentManager,ProfileFragment(),"profileFragment")
                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    true
                }
                else -> false
            }

        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val navView = binding.bottomNavigationView
        navView.selectedItemId=R.id.settings // set the checked item to the ID of the home Fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingFragment=null
    }


}