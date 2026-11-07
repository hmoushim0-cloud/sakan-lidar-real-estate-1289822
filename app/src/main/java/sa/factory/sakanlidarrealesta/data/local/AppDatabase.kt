package sa.factory.sakanlidarrealesta.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SakanLidarEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun real_estateDao(): SakanLidarDao
}
