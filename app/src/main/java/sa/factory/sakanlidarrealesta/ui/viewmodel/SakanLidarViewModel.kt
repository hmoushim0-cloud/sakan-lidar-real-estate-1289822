package sa.factory.sakanlidarrealesta.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.sakanlidarrealesta.domain.usecase.*
import sa.factory.sakanlidarrealesta.model.SakanLidarItem
import sa.factory.sakanlidarrealesta.model.SakanLidarOrder
import sa.factory.sakanlidarrealesta.model.SakanLidarTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SakanLidarUiState(
    val catalog: List<SakanLidarItem> = emptyList(),
    val filteredCatalog: List<SakanLidarItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<SakanLidarItem> = emptyList(),
    val telemetry: SakanLidarTelemetry? = null,
    val lastOrder: SakanLidarOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class SakanLidarViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SakanLidarUiState())
    val uiState: StateFlow<SakanLidarUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: SakanLidarItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: SakanLidarItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
