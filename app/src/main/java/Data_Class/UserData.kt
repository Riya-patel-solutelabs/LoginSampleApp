package Data_Class

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserData(
    @PrimaryKey(autoGenerate = true) val id:Int?,
    var firstname:String?,
    var lastname:String?,
    var email:String?,
    var password: String?,
    var gender:String?,
    var dob: String?,
    var age:String?,
    var rememberMe: Boolean?,
    var imagePath: ByteArray?=null,
//    var imagePath : Uri?=null





)
