package Data_Class

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [UserData::class], version = 3)
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
                ).addMigrations(MIGRATION_1_3)
                    .build()
                INSTANCE=instance
                return instance

            }
        }

        val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new column 'path' to the 'user_table'
                database.execSQL("ALTER TABLE user_table ADD COLUMN profilePic TEXT DEFAULT '' NOT NULL")
            }
        }


    }


}