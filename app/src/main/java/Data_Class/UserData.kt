package Data_Class

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
    var rememberMe: Boolean?

)
