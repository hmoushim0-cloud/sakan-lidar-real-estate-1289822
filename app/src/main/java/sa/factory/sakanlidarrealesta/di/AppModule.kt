package sa.factory.sakanlidarrealesta.di

import sa.factory.sakanlidarrealesta.data.repository.SakanLidarRepositoryImpl
import sa.factory.sakanlidarrealesta.domain.repository.SakanLidarRepository
import sa.factory.sakanlidarrealesta.domain.usecase.*
import sa.factory.sakanlidarrealesta.hardware.HardwareSensorDriver
import sa.factory.sakanlidarrealesta.ui.viewmodel.SakanLidarViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: SakanLidarRepository = SakanLidarRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): SakanLidarViewModel = SakanLidarViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}
