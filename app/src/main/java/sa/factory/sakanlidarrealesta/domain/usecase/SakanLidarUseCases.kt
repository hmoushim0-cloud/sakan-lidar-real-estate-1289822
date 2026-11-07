package sa.factory.sakanlidarrealesta.domain.usecase

import sa.factory.sakanlidarrealesta.domain.repository.SakanLidarRepository
import sa.factory.sakanlidarrealesta.model.SakanLidarItem
import sa.factory.sakanlidarrealesta.model.SakanLidarOrder
import sa.factory.sakanlidarrealesta.model.SakanLidarTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: SakanLidarRepository) {
    operator fun invoke(): Flow<List<SakanLidarItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: SakanLidarRepository) {
    val cart = repository.activeCart
    fun add(item: SakanLidarItem) = repository.addToCart(item)
    fun remove(item: SakanLidarItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: SakanLidarRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): SakanLidarOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: SakanLidarRepository) {
    operator fun invoke(): Flow<SakanLidarTelemetry> = repository.telemetryFlow
}
