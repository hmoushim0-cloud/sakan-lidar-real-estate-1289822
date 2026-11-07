package sa.factory.sakanlidarrealesta.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SakanLidarDao {
    @Query("SELECT * FROM real_estate_items_cache ORDER BY id ASC")
    fun observeAllItems(): Flow<List<SakanLidarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<SakanLidarEntity>)

    @Query("DELETE FROM real_estate_items_cache")
    suspend fun clearAll()
}
