package pl.polsl.temperature.management

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.androidplot.xy.*
import kotlinx.android.synthetic.main.activity_management.*
import pl.polsl.temperature.R
import pl.polsl.temperature.application.*
import pl.polsl.temperature.models.Measurement
import java.util.*

class ManagementViewHelper(
    private val activity: BaseActivity,
    private val managementPresenter: ManagementPresenter) {

    fun updateMeasurements(measurements: List<Measurement>) {
        measurements.forEach {
            ApplicationContext.log(it.date.dateToString())
        }
        val colorPrimary = ContextCompat.getColor(activity, R.color.colorPrimary)
        val measurementTypeIds = measurements.map { it.measurementTypeId }.distinct()
        measurementTypeIds.forEach { measurementTypeId ->
            ApplicationContext.log(measurementTypeId)
            val currentMeasurements = measurements.filter { it.measurementTypeId == measurementTypeId }
            val maxMin = currentMeasurements.map { it.value }
            val max = maxMin.max() ?: 0.0
            val min = maxMin.min() ?: 0.0
            (activity.layoutInflater.inflate(R.layout.plot, (null)) as XYPlot).apply {
                setTitle(managementPresenter.getMeasurementType(measurementTypeId)?.name ?: " ")
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    300.toPx()
                ).apply {
                    setMargins(0, 10.toPx(), 0, 10.toPx())
                }
                setPadding(10.toPx())
                setRangeBoundaries(min, max, BoundaryMode.AUTO)
                if(max > min)
                    rangeStepValue = (max - min) / 5.0
                rangeStepMode = StepMode.INCREMENT_BY_VAL
                setDomainLabel(activity.getString(R.string.timeline))
                domainTitle.pack()
                setRangeLabel(activity.getString(R.string.values))
                rangeTitle.pack()
                val simpleXYSeries = SimpleXYSeries(" ")
                simpleXYSeries.useImplicitXVals()
                currentMeasurements.forEach {
                    simpleXYSeries.addFirst(null, it.value)
                }
                addSeries(simpleXYSeries, LineAndPointFormatter(colorPrimary, null, null, null))
            }.also {
                activity.plotsContainer.addView(it)
            }
        }
    }

    fun askForName(callback: (name: String) -> Unit) {
        val editText = EditText(activity)
        editText.isSingleLine = true
        val container = FrameLayout(activity)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.leftMargin = 20.toPx()
        params.rightMargin = 20.toPx()
        editText.layoutParams = params
        container.addView(editText)
        AlertDialog.Builder(activity)
            .setTitle(R.string.enterName)
            .setView(container)
            .setPositiveButton(R.string.ok) { _, _ ->
                callback(editText.text.toString())
            }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .create()
            .mShow(activity)
    }

    fun showDateTimePicker(initialDate: Date, callback: (date: Date) -> Unit) {
        val startDate = Calendar.getInstance().apply { time = initialDate }
        val date: Calendar = Calendar.getInstance()
        DatePickerDialog(
            activity,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    activity,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        callback(date.time)
                    },
                    startDate[Calendar.HOUR_OF_DAY],
                    startDate[Calendar.MINUTE],
                    false
                ).mShow(activity)
            },
            startDate[Calendar.YEAR],
            startDate[Calendar.MONTH],
            startDate[Calendar.DATE]
        ).mShow(activity)
    }


}