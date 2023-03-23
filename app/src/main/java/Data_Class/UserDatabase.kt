package Data_Class

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserData::class], version = 1)
@TypeConverters(Convertors::class)
abstract class UserDatabase:RoomDatabase() {

    abstract fun userDao(): UserDao
    companion object{
        @Volatile

        private var INSTANCE: UserDatabase?=null

        fun getDatabase(context: Context):UserDatabase{
            val tempInstance= INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE=instance
                return instance

            }
        }

    }
}