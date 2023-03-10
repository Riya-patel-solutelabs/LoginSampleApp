package Data_Class

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface UserDao {

    @Query("SELECT * FROM user_table")
    fun getAll(): LiveData<List<UserData>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertData(user: UserData)

    @Delete
    suspend fun deleteData(user: UserData)

    @Query("SELECT * FROM user_table WHERE email = :email")
    suspend fun findByEmail(email: String): UserData?

    @Query("SELECT EXISTS(SELECT 1 FROM user_table WHERE email = :email LIMIT 1)")
    suspend fun emailExists(email: String): Boolean

    @Query("SELECT * FROM user_table where email= :email and password= :password")
    suspend fun findByEmailandPass(email: String,password: String):UserData?

    @Update
    suspend fun onUpdate(userData: UserData)






}