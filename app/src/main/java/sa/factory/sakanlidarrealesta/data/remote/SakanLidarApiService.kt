package sa.factory.sakanlidarrealesta.data.remote

import sa.factory.sakanlidarrealesta.model.SakanLidarItem
import sa.factory.sakanlidarrealesta.model.SakanLidarOrder

interface SakanLidarApiService {
    suspend fun fetchCatalog(): List<SakanLidarItem>
    suspend fun submitOrder(order: SakanLidarOrder): Boolean
}
