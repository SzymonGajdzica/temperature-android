package pl.polsl.temperature.management

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setPadding
import com.androidplot.xy.*
import kotlinx.android.synthetic.main.activity_management.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.polsl.temperature.R
import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.BaseActivity
import pl.polsl.temperature.application.dateToShortString
import pl.polsl.temperature.application.toPx
import pl.polsl.temperature.models.Gateway
import pl.polsl.temperature.models.Measurement
import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.User
import pl.polsl.temperature.utils.OneToast
import pl.polsl.temperature.utils.SettingsTools
import java.util.*


class ManagementActivityImpl:
    BaseActivity(),
    ManagementActivity,
    View.OnClickListener{

    private val managementPresenter: ManagementPresenter = ManagementPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)
        setSupportActionBar(toolbar)
        showLoader()
        managementPresenter.getUserData()
    }

    override fun onClick(view: View?) {
        if(isLoading())
            return
        when(view?.id){
            R.id.startDateButton -> startDateButtonClick()
            R.id.endDateButton -> endDateButtonClick()
        }
    }

    override fun showDateTimePicker(initialDate: Date, callback: (date: Date) -> Unit) {
        val startDate = Calendar.getInstance().apply { time = initialDate }
        val date: Calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    this,
                    OnTimeSetListener { _, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        showLoader()
                        callback(date.time)
                    },
                    startDate[Calendar.HOUR_OF_DAY],
                    startDate[Calendar.MINUTE],
                    false
                ).show()
            },
            startDate[Calendar.YEAR],
            startDate[Calendar.MONTH],
            startDate[Calendar.DATE]
        ).show()
    }

    private fun startDateButtonClick(){
        managementPresenter.updateStartDate()
    }

    private fun endDateButtonClick(){
        managementPresenter.updateEndDate()
    }

    override fun updateUserData(user: User) {
        hideLoader()
        welcomeText.text = (getString(R.string.welcome) + " " + user.username)
        setupGatewaySpinner(user)
    }

    private fun setupGatewaySpinner(user: User){
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, user.gateways.map { it.name }
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gatewaySpinner.adapter = dataAdapter
        gatewaySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setupStationSpinner(user.gateways[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        setupStationSpinner(user.gateways.firstOrNull())
    }

    override fun updateStartDate(date: Date){
        startDateText.text = (getString(R.string.startDate) + " - " + date.dateToShortString())
    }

    override fun updateEndDate(date: Date){
        endDateText.text = (getString(R.string.endDate) + " - " + date.dateToShortString())
    }

    private fun setupStationSpinner(gateway: Gateway?){
        val dataAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, gateway?.stations?.map { it.name } ?: arrayListOf()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stationSpinner.adapter = dataAdapter
        stationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                showLoader()
                managementPresenter.stationSelected(gateway?.stations?.get(position))
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        managementPresenter.stationSelected(gateway?.stations?.firstOrNull())
    }

    override fun updateStationData(station: Station) {
        hideLoader()
        if(station.measurements.isEmpty())
            OneToast.show(R.string.noMeasurements)
        else
            updateMeasurements(station.measurements)
    }

    private fun updateMeasurements(measurements: List<Measurement>) {
        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary)
        plotsContainer.removeAllViews()
        val measurementTypeIds = measurements.map { it.measurementTypeId }.distinct()
        measurementTypeIds.forEach { measurementTypeId ->
            ApplicationContext.log(measurementTypeId)
            val currentMeasurements = measurements.filter { it.measurementTypeId == measurementTypeId }
            val maxMin = currentMeasurements.map { it.value }
            val max = maxMin.max() ?: 0.0
            val min = maxMin.min() ?: 0.0
            val plot = layoutInflater.inflate(R.layout.plot, (null)) as XYPlot
            plot.setTitle(measurementTypeId.toString())
            plot.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                300.toPx()
            ).apply {
                setMargins(0, 10.toPx(), 0, 10.toPx())
            }
            plot.setPadding(10.toPx())
            plot.setRangeBoundaries(min, max, BoundaryMode.AUTO)
            if(max > min)
                plot.rangeStepValue = (max - min) / 5.0
            plot.rangeStepMode = StepMode.INCREMENT_BY_VAL
            plot.setDomainLabel(getString(R.string.timeline))
            plot.domainTitle.pack()
            plot.setRangeLabel(getString(R.string.values))
            plot.rangeTitle.pack()
            val simpleXYSeries = SimpleXYSeries(" ")
            simpleXYSeries.useImplicitXVals()
            currentMeasurements.forEach {
                simpleXYSeries.addLast(null, it.value)
            }
            plot.addSeries(simpleXYSeries, LineAndPointFormatter(colorPrimary, null, null, null))
            plotsContainer.addView(plot)
        }
    }

    override fun loadingStationFailed() {
        hideLoader()
    }

    override fun loadingUserDataFailed() {
        hideLoader()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.baseline_arrow_back_white_24)
        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    override fun tokenExpired() {
        hideLoader()
        OneToast.show(R.string.sessionExpired)
        SettingsTools.setToken(null)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideLoader()
        SettingsTools.setToken(null)
        OneToast.show(R.string.loggingOut)
    }

}