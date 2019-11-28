package pl.polsl.temperature.management

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_management.*
import kotlinx.android.synthetic.main.toolbar.*
import org.joda.time.DateTime
import pl.polsl.temperature.R
import pl.polsl.temperature.application.BaseActivity
import pl.polsl.temperature.application.dateToShortString
import pl.polsl.temperature.models.Gateway
import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.User
import pl.polsl.temperature.utils.OneToast
import pl.polsl.temperature.utils.SettingsTools


class ManagementActivityImpl:
    BaseActivity(),
    ManagementActivity,
    View.OnClickListener{

    private val managementPresenter: ManagementPresenter = ManagementPresenterImpl(this)
    private val managementViewHelper = ManagementViewHelper(this, managementPresenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)
        setSupportActionBar(toolbar)
        managementPresenter.getUserData()
    }

    override fun onClick(view: View?) {
        handleClick(view?.id)
    }

    private fun handleClick(id: Int?) {
        when(id){
            R.id.startDateButton -> startDateButtonClick()
            R.id.endDateButton -> endDateButtonClick()
            R.id.actionAddGateway -> managementViewHelper.askForName { managementPresenter.addGateway(it) }
            R.id.actionAddStation -> managementViewHelper.askForName { managementPresenter.addStation(it) }
            R.id.actionAddMeasurementType -> managementViewHelper.askForName { managementPresenter.addMeasurementType(it) }
            R.id.actionGetSecretId -> managementPresenter.copyStationSecretId()
        }
    }

    override fun showDateTimePicker(initialDate: DateTime, callback: (date: DateTime) -> Unit) {
        managementViewHelper.showDateTimePicker(initialDate, callback)
    }

    private fun startDateButtonClick(){
        managementPresenter.updateStartDate()
    }

    private fun endDateButtonClick(){
        managementPresenter.updateEndDate()
    }

    override fun updateUserData(user: User) {
        welcomeText.text = (getString(R.string.welcome) + " " + user.username)
        setupGatewaySpinner(user)
    }

    private fun setupGatewaySpinner(user: User){
        val names = arrayListOf(getString(R.string.selectGateway))
        names.addAll(user.gateways.map { it.name })
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, names
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gatewaySpinner.adapter = dataAdapter
        gatewaySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                managementPresenter.gatewaySelected(user.gateways.getOrNull(position - 1))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun updateStartDate(date: DateTime){
        startDateText.text = (getString(R.string.startDate) + " - " + date.dateToShortString())
    }

    override fun updateEndDate(date: DateTime){
        endDateText.text = (getString(R.string.endDate) + " - " + date.dateToShortString())
    }

    override fun setupStationSpinner(gateway: Gateway?){
        if(gateway == null) {
            stationSpinner.adapter = null
            return
        }
        val names = arrayListOf(getString(R.string.selectStation))
        names.addAll(gateway.stations.map { it.name })
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, names
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stationSpinner.adapter = dataAdapter
        stationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                managementPresenter.stationSelected(gateway.stations.getOrNull(position - 1))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun updateStationData(station: Station) {
        plotsContainer.removeAllViews()
        if(station.measurements.isNotEmpty())
            managementViewHelper.updateMeasurements(station.measurements)
    }

    override fun getContext(): BaseActivity {
        return this
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.baseline_arrow_back_white_24)
        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    override fun tokenExpired() {
        OneToast.show(R.string.sessionExpired)
        SettingsTools.setToken(null)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        SettingsTools.setToken(null)
        OneToast.show(R.string.loggingOut)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        handleClick(item.itemId)
        return true
    }

}