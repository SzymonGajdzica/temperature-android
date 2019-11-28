package pl.polsl.temperature.management

import org.joda.time.DateTime
import pl.polsl.temperature.application.BaseActivity
import pl.polsl.temperature.models.Gateway
import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.User
import java.util.*

interface ManagementActivity {

    fun tokenExpired()

    fun updateEndDate(date: DateTime)

    fun updateStartDate(date: DateTime)

    fun showDateTimePicker(initialDate: DateTime, callback: (date: DateTime) -> Unit)

    fun updateUserData(user: User)

    fun updateStationData(station: Station)

    fun setupStationSpinner(gateway: Gateway?)

    fun getContext(): BaseActivity

}