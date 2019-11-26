package pl.polsl.temperature.management

import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.User
import java.util.*

interface ManagementActivity {

    fun tokenExpired()

    fun updateUserData(user: User)

    fun loadingUserDataFailed()

    fun updateEndDate(date: Date)

    fun updateStartDate(date: Date)

    fun showDateTimePicker(initialDate: Date, callback: (date: Date) -> Unit)

    fun loadingStationFailed()

    fun updateStationData(station: Station)

}