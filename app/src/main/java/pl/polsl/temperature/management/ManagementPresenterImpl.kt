package pl.polsl.temperature.management

import org.joda.time.DateTime
import pl.polsl.temperature.R
import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.copyToClipboard
import pl.polsl.temperature.application.dateToString
import pl.polsl.temperature.application.reduce
import pl.polsl.temperature.models.*
import pl.polsl.temperature.services.GatewayService
import pl.polsl.temperature.services.MeasurementTypeService
import pl.polsl.temperature.services.StationService
import pl.polsl.temperature.services.UserService
import pl.polsl.temperature.utils.OneToast
import pl.polsl.temperature.utils.SettingsTools
import retrofit2.create

class ManagementPresenterImpl(private val managementActivity: ManagementActivity): ManagementPresenter {

    private val userService: UserService = ApplicationContext.getRetrofit().create()
    private val stationService: StationService = ApplicationContext.getRetrofit().create()
    private val gatewayService: GatewayService = ApplicationContext.getRetrofit().create()
    private val measurementTypeService: MeasurementTypeService = ApplicationContext.getRetrofit().create()
    private var currentUser: User? = null
    private var currentStation: StationReduced? = null
    private var currentGateway: Gateway? = null
    private var startDate = DateTime(System.currentTimeMillis() - (60*60*1000))
    private var endDate = DateTime(System.currentTimeMillis() + (60*1000))
    private val measurementTypes: HashMap<Long, MeasurementType> = hashMapOf()
    private var measurementTypesLoaded = false

    override fun getUserData() {
        if(checkTokenExpired())
            return
        userService.getUserData().enqueue(managementActivity.getContext().createCallback {
            OneToast.show(R.string.loggedSuccessfully)
            currentUser = it
            managementActivity.updateUserData(it)
            managementActivity.updateEndDate(endDate)
            managementActivity.updateStartDate(startDate)
        })
    }

    override fun addGateway(name: String) {
        if(checkTokenExpired())
            return
        val user = currentUser ?: return
        gatewayService.addGateway(GatewayPost(name, user.id)).enqueue(managementActivity.getContext().createCallback { gateway ->
            OneToast.show(managementActivity.getContext().getString(R.string.gatewayAdded) + ", id = " + gateway.id)
            currentUser?.gateways?.add(gateway)
            managementActivity.updateUserData(user)
        })
    }

    override fun addMeasurementType(name: String) {
        if(checkTokenExpired())
            return
        val user = currentUser ?: return
        measurementTypeService.addMeasurementType(MeasurementTypePost(name, user.id)).enqueue(managementActivity.getContext().createCallback { measurementType ->
            OneToast.show(managementActivity.getContext().getString(R.string.measurementTypeAdded) + ", id = " + measurementType.id)
            measurementTypes[measurementType.id] = measurementType
            managementActivity.updateUserData(user)
        })
    }

    override fun copyStationSecretId() {
        val uuid = currentStation?.secretId  ?: return OneToast.show(R.string.firstSelectStation)
        ApplicationContext.getAppContext()?.copyToClipboard(uuid.toString())
        OneToast.show(R.string.secretIdCopied)
    }

    override fun getMeasurementType(id: Long): MeasurementType? {
        return measurementTypes[id]
    }

    override fun addStation(name: String) {
        if(checkTokenExpired())
            return
        val gateway = currentGateway ?: return OneToast.show(R.string.firstAddGateway)
        stationService.addStation(StationPost(name, gateway.id)).enqueue(managementActivity.getContext().createCallback { station ->
            OneToast.show(managementActivity.getContext().getString(R.string.stationAdded) + ", id = " + station.id)
            gateway.stations.add(station.reduce())
            currentUser?.let { managementActivity.updateUserData(it) }
        })
    }

    override fun gatewaySelected(gateway: Gateway?) {
        if(checkTokenExpired())
            return
        currentGateway = gateway
        currentStation = null
        managementActivity.setupStationSpinner(gateway)
    }

    override fun stationSelected(stationReduced: StationReduced?) {
        if(checkTokenExpired())
            return
        this.currentStation = stationReduced
        val stationId = stationReduced?.id ?: return
        ApplicationContext.log("start end")
        ApplicationContext.log(startDate.dateToString())
        ApplicationContext.log(endDate.dateToString())
        stationService.getStation(stationId, startDate.dateToString(), endDate.dateToString()).enqueue(managementActivity.getContext().createCallback {
            managementActivity.updateStationData(it)
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
        if(SettingsTools.getToken() != null){
            loadMeasurementTypes()
            return false
        }
        managementActivity.tokenExpired()
        return true
    }

    private fun loadMeasurementTypes(){
        if(measurementTypesLoaded)
            return
        measurementTypeService.getMeasurementTypes().enqueue(managementActivity.getContext().createCallback { list ->
            list.forEach {
                measurementTypes[it.id] = it
            }
            measurementTypesLoaded = true
        })
    }

}