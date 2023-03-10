package Data_Class

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserID(
    val ufname:String,
    val ulname:String,
    val uemail:String,
    val upass:String,
    val ugender:String,
    val udob:String,
    val uage:String
):Parcelable

