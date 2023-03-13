package Data_Class

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

 class Convertors {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap) : ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }
    @TypeConverter
    fun toBitmap(byteArray: ByteArray) : Bitmap {
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }
}

//class Convertors {
//    @TypeConverter
//    fun fromUri(uri: Uri?): String? {
//        return uri.toString()
//    }
//
//    @TypeConverter
//    fun toUri(uriString: String?): Uri? {
//        return Uri.parse(uriString)
//    }
//}