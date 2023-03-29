package fragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.example.myapp.MainHomeActivity
import com.example.myapp.R
import com.example.myapp.databinding.FragmentSettingBinding


class SettingFragment : BaseFragment() {

    private var settingFragment: FragmentSettingBinding?=null
    private val binding get() = settingFragment!!
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: NotificationCompat.Builder

    val CHANNEL_ID = "com.example.myapp"
    val DESCRIPTION= "Test Notification"
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
                    replaceHomeFragment(requireActivity().supportFragmentManager,HomeFragment(),"homeFragment",0)
                  true
                }
                R.id.profile->{
                    replaceHomeFragment(requireActivity().supportFragmentManager,ProfileFragment(),"profileFragment",0)
                    true
                }
                R.id.settings->{
                    // Load Settings Fragment
                    true
                }
                else -> false
            }

        }

        notificationManager= requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        binding.btnSendNotification.setOnClickListener {

            showNotification()
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

    fun showNotification(){

        if (notificationManager.areNotificationsEnabled()) {
            // Notifications are already enabled
        } else {
            // Ask for permission to post notifications
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val intent = Intent(requireContext(), MainHomeActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                DESCRIPTION,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setContentTitle("Demo")
                .setContentText(DESCRIPTION)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            builder.setContentIntent(pendingIntent)
        } else {
            builder= NotificationCompat.Builder(requireContext())
                .setContentTitle("LoginApp")
                .setContentText(DESCRIPTION)
                .setSmallIcon(R.drawable.baseline_circle_notifications_24)
                .setLargeIcon((BitmapFactory.decodeResource(
                    this.resources, R.drawable.baseline_power_settings_new_24)))

        }


        notificationManager.notify(1234,builder.build())
    }
    }

