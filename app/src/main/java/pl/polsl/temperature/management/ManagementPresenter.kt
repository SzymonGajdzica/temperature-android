package pl.polsl.temperature.management

import pl.polsl.temperature.models.Gateway
import pl.polsl.temperature.models.MeasurementType
import pl.polsl.temperature.models.StationReduced

interface ManagementPresenter {

    fun getUserData()

    fun updateEndDate()

    fun updateStartDate()

    fun addStation(name: String)

    fun addGateway(name: String)

    fun addMeasurementType(name: String)

    fun gatewaySelected(gateway: Gateway?)

    fun stationSelected(stationReduced: StationReduced?)

    fun getMeasurementType(id: Long): MeasurementType?

}