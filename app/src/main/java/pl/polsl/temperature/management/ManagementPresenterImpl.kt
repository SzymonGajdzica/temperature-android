package pl.polsl.temperature.management

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.dateToString
import pl.polsl.temperature.application.getMessageString
import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.StationReduced
import pl.polsl.temperature.models.User
import pl.polsl.temperature.services.StationService
import pl.polsl.temperature.services.UserService
import pl.polsl.temperature.utils.OneToast
import pl.polsl.temperature.utils.SettingsTools
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import java.util.*

class ManagementPresenterImpl(private val managementActivity: ManagementActivity): ManagementPresenter {

    private val userService: UserService = ApplicationContext.getRetrofit().create()
    private val stationService: StationService = ApplicationContext.getRetrofit().create()
    private var currentUser: User? = null
    private var currentStation: StationReduced? = null
    private var startDate = Date(System.currentTimeMillis() - (60*60*1000))
    private var endDate = Date()

    override fun getUserData() {
        if(checkTokenExpired())
            return
        userService.getUserData().enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>, e: Throwable) {
                OneToast.show(e.message)
                managementActivity.loadingUserDataFailed()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val user = response.body()
                if(response.isSuccessful && user != null){
                    currentUser = user
                    managementActivity.updateUserData(user)
                    managementActivity.updateEndDate(endDate)
                    managementActivity.updateStartDate(startDate)
                } else{
                    OneToast.show(response.getMessageString())
                    managementActivity.loadingUserDataFailed()
                }
            }

        })
    }

    override fun stationSelected(stationReduced: StationReduced?) {
        this.currentStation = stationReduced
        val stationId = stationReduced?.id ?: return
        stationService.getStation(stationId, startDate.dateToString(), endDate.dateToString()).enqueue(object: Callback<Station>{
            override fun onFailure(call: Call<Station>, e: Throwable) {
                OneToast.show(e.message)
                managementActivity.loadingUserDataFailed()
            }

            override fun onResponse(call: Call<Station>, response: Response<Station>) {
                val station = response.body()
                if(response.isSuccessful && station != null){
                    managementActivity.updateStationData(station)
                } else{
                    OneToast.show(response.getMessageString())
                    managementActivity.loadingStationFailed()
                }
            }
        })
    }

    override fun updateEndDate() {
        managementActivity.showDateTimePicker(endDate) {
            endDate = it
            managementActivity.updateEndDate(endDate)
            stationSelected(currentStation)
        }
    }

    override fun updateStartDate() {
        managementActivity.showDateTimePicker(startDate) {
            startDate = it
            managementActivity.updateStartDate(startDate)
            stationSelected(currentStation)
        }
    }

    private fun checkTokenExpired(): Boolean{
        if(SettingsTools.getToken() != null)
            return false
        managementActivity.tokenExpired()
        return true
    }

}