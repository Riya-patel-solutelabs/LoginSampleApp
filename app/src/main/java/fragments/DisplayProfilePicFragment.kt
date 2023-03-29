package fragments

import Data_Class.UserDatabase
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.myapp.R
import com.example.myapp.databinding.FragmentDisplayProfilePicBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class DisplayProfilePicFragment : BaseFragment() {
    private var displayProfilePicBinding: FragmentDisplayProfilePicBinding? = null
    private val binding get() = displayProfilePicBinding!!
    private lateinit var userDb: UserDatabase
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val GALLERY_PERMISSION_REQUEST_CODE = 2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        displayProfilePicBinding =
            FragmentDisplayProfilePicBinding.inflate(inflater, container, false)
        userDb = UserDatabase.getDatabase(requireContext())
        val toolbar = binding.toolbar
        toolbar.title = "View Profile"
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        GlobalScope.launch(Dispatchers.Main) {
            val user = userDb.userDao().findByEmail(LoginFragment.currentemail)
            if (user != null) {
                if (user.profilePic != null) {
                    binding.imageViewProfile.setImageURI(Uri.parse(user.profilePic))
                } else {
                    binding.imageViewProfile.setImageResource(R.drawable.baseline_person_null)
                    //binding.imageViewProfile.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), PorterDuff.Mode.SRC_IN)
                }
            }


        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        displayProfilePicBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_appbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                editProfile()
                return true
            }
            android.R.id.home -> {
                if (finalsnackbar != null && finalsnackbar!!.isShown) {
                    finalsnackbar!!.dismiss()
                }
                requireActivity().supportFragmentManager.popBackStack()
                return true
            }


            else -> {
                super.onOptionsItemSelected(item)
            }
        }


    }

    private fun editProfile() {

        val snackbar = Snackbar.make(requireView(), "", Snackbar.LENGTH_INDEFINITE)
        finalsnackbar = snackbar
        val snackbarLayout = snackbar.view as SnackbarLayout
        val snackView =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_snackbar, null)
        snackbarLayout.addView(snackView, 0)

        snackView.findViewById<ImageButton>(R.id.image_camera).setOnClickListener {
            requestCameraPermission()


        }
        snackView.findViewById<ImageButton>(R.id.image_gallery).setOnClickListener {
            requestGalleryPermission()

        }
        snackView.findViewById<ImageButton>(R.id.image_delete).setOnClickListener {
            deleteImage()

        }
        snackbar.show()

    }

    private fun deleteImage() {
        val directory = File(requireContext().getExternalFilesDir(null), "user_images")
        if (!directory.exists()) {
            directory.mkdir()
        }

        val regex = Regex("[^A-Za-z\\d]")
        val fileName = "${LoginFragment.currentemail.replace(regex, "")}.jpg"
        val file = File(directory, fileName)

        if (file.exists()) {
            file.delete()
            GlobalScope.launch(Dispatchers.IO) {
                val user = userDb.userDao().findByEmail(LoginFragment.currentemail)
                if (user != null) {
                    user.profilePic = null
                }
                userDb.userDao().onUpdate(user!!)
            }
            binding.imageViewProfile.setImageResource(R.drawable.baseline_person_null)
        }

        finalsnackbar!!.dismiss()


    }


    // Function to request camera permission
    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    // Function to request gallery permission


    private fun requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_PERMISSION_REQUEST_CODE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_PERMISSION_REQUEST_CODE)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(requireContext(), "Gallery permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                // Handle camera image
                val image = data.extras?.get("data") as Bitmap
                //      binding.imageViewProfile.setImageBitmap(image)
                //val image2= data.extras?.get("data") as String
                saveImageToStorage(image)
                println(image)

            } else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
                val galleryPic = data.data
                // Get the image bitmap from the URI
                val imageBitmap =
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, galleryPic)
                //        binding.imageViewProfile.setImageBitmap(imageBitmap)
                saveImageToStorage(imageBitmap)

            }
        }
    }

    private fun saveImageToStorage(bitmap: Bitmap) {
        val directory = File(requireContext().getExternalFilesDir(null), "user_images")
        if (!directory.exists()) {
            directory.mkdir()
        }
        val regex = Regex("[^A-Za-z\\d]")
        val filename = "${LoginFragment.currentemail.replace(regex, "")}.jpg"
        val file = File(directory, filename)
        if (file.exists()) {
            file.delete()
        } else {
        }
        storeImageToRoom(file.absolutePath)

        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        //storeImageToRoom(file.absolutePath)

        finalsnackbar!!.dismiss()
        reloadImage()

        //Glide.with(this).load(file).into(binding.imageViewProfile)

    }


    fun reloadImage() {

        val directory = File(requireContext().getExternalFilesDir(null), "user_images")
        if (!directory.exists()) {
            directory.mkdir()
        }


        val regex = Regex("[^A-Za-z0-9]")
        val fileName = "${LoginFragment.currentemail.replace(regex, "")}.jpg"

        val file = File(directory, fileName)
        if (file.exists()) {
            Glide.with(this)
                .load(file)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.baseline_person_null) // placeholder image
                .error(R.drawable.baseline_person_null) // error image
                .into(binding.imageViewProfile)

        } else {
            // Load the default image into the ImageView
            binding.imageViewProfile.setImageResource(R.drawable.baseline_person_null)
        }

    }

    private fun storeImageToRoom(imagePath: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val user = userDb.userDao().findByEmail(LoginFragment.currentemail)
            if (user != null) {
                user.profilePic = imagePath
            }
            userDb.userDao().onUpdate(user!!)
        }

        Toast.makeText(requireContext(), "Profile Pic Updated", Toast.LENGTH_SHORT).show()

    }


    companion object {
        var finalsnackbar: Snackbar? = null
    }

}