package pl.polsl.temperature.management

import pl.polsl.temperature.application.BaseActivity
import pl.polsl.temperature.models.Gateway
import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.User
import java.util.*

interface ManagementActivity {

    fun tokenExpired()

    fun updateEndDate(date: Date)

    fun updateStartDate(date: Date)

    fun showDateTimePicker(initialDate: Date, callback: (date: Date) -> Unit)

    fun updateUserData(user: User)

    fun updateStationData(station: Station)

    fun setupStationSpinner(gateway: Gateway?)

    fun getContext(): BaseActivity

}