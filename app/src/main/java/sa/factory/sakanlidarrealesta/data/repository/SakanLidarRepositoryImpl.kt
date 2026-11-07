package sa.factory.sakanlidarrealesta.data.repository

import sa.factory.sakanlidarrealesta.domain.repository.SakanLidarRepository
import sa.factory.sakanlidarrealesta.hardware.HardwareSensorDriver
import sa.factory.sakanlidarrealesta.model.SakanLidarItem
import sa.factory.sakanlidarrealesta.model.SakanLidarOrder
import sa.factory.sakanlidarrealesta.model.SakanLidarTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SakanLidarRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : SakanLidarRepository {

    private val _catalog = MutableStateFlow<List<SakanLidarItem>>(listOf(
        SakanLidarItem("1", "الباقة التنفيذية المعتمدة", "تكامل مباشر مع 360 LiDAR Spatial Scanner + Matterport Pro3", 180.0, "معتمد", "الخدمات الرئيسية"),
        SakanLidarItem("2", "خدمة المعالجة الفورية", "أداء أصيل بسرعة 60 إطار في الثانية", 95.0, "فوري", "المراقبة الحية"),
        SakanLidarItem("3", "باقة الصيانة والدعم السحابي", "تشفير كامل للبيانات المحلية ومزامنة آمنة", 250.0, "شامل", "التقارير الضريبية")
    ))
    override val catalogFlow: Flow<List<SakanLidarItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<SakanLidarItem>>(emptyList())
    override val activeCart: StateFlow<List<SakanLidarItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<SakanLidarTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            SakanLidarTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: SakanLidarItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: SakanLidarItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): SakanLidarOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = SakanLidarOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
