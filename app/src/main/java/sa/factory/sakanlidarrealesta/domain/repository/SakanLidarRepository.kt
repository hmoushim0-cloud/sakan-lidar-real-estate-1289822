package sa.factory.sakanlidarrealesta.domain.repository

import sa.factory.sakanlidarrealesta.model.SakanLidarItem
import sa.factory.sakanlidarrealesta.model.SakanLidarOrder
import sa.factory.sakanlidarrealesta.model.SakanLidarTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SakanLidarRepository {
    val catalogFlow: Flow<List<SakanLidarItem>>
    val activeCart: StateFlow<List<SakanLidarItem>>
    val telemetryFlow: Flow<SakanLidarTelemetry>
    
    fun addToCart(item: SakanLidarItem)
    fun removeFromCart(item: SakanLidarItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): SakanLidarOrder
    suspend fun refreshCatalog()
}
