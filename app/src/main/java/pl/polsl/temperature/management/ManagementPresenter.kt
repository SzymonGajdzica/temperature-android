package pl.polsl.temperature.management

import pl.polsl.temperature.models.StationReduced

interface ManagementPresenter {

    fun getUserData()

    fun updateEndDate()

    fun updateStartDate()

    fun stationSelected(stationReduced: StationReduced?)

}