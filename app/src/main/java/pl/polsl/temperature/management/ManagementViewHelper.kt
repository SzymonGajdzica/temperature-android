package pl.polsl.temperature.management

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.androidplot.xy.*
import kotlinx.android.synthetic.main.activity_management.*
import org.joda.time.DateTime
import pl.polsl.temperature.R
import pl.polsl.temperature.application.*
import pl.polsl.temperature.models.Measurement
import java.lang.StringBuilder
import java.util.*

class ManagementViewHelper(
    private val activity: BaseActivity,
    private val managementPresenter: ManagementPresenter) {

    private val colorPrimary: Int by lazy { ContextCompat.getColor(activity, R.color.colorPrimary) }

    fun updateMeasurements(measurements: List<Measurement>) {
        measurements.forEach {
            ApplicationContext.log(DateTime(it.date).dateToString())
        }
        val measurementTypeIds = measurements.map { it.measurementTypeId }.distinct()
        measurementTypeIds.forEach { measurementTypeId ->
            val currentMeasurements = measurements.filter { it.measurementTypeId == measurementTypeId }
            val rawValues = currentMeasurements.map { it.value }
            if(rawValues.isNotEmpty()){
                activity.plotsContainer.addView(getPlot(measurementTypeId, rawValues))
                val text = StringBuilder()
                    .append("\n")
                    .append(activity.getString(R.string.firstMeasurement))
                    .append(": ")
                    .append(currentMeasurements.first().date.toDateTime().dateToShortString())
                    .append("\n")
                    .append(activity.getString(R.string.lastMeasurement))
                    .append(": ")
                    .append(currentMeasurements.last().date.toDateTime().dateToShortString())
                    .append("\n")
                    .append(activity.getString(R.string.average))
                    .append(": ")
                    .append(rawValues.average().round())
                    .append("\n")
                    .append(activity.getString(R.string.max))
                    .append(": ")
                    .append(rawValues.max()?.round())
                    .append("\n")
                    .append(activity.getString(R.string.min))
                    .append(": ")
                    .append(rawValues.min()?.round())
                    .append("\n")
                    .append(activity.getString(R.string.current))
                    .append(": ")
                    .append(rawValues.last().round())
                    .append("\n")
                    .append("\n")
                    .toString()
                activity.plotsContainer.addView(getTextView(text))
            }
        }
    }

    private fun getTextView(text: String): View {
        return TextView(activity).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            this.text = text
            setTextColor(colorPrimary)
        }
    }

    private fun getPlot(measurementTypeId: Long, rawValues: List<Double>): View {
        val max = rawValues.max() ?: 0.0
        val min = rawValues.min() ?: 0.0
        return (activity.layoutInflater.inflate(R.layout.plot, (null)) as XYPlot).apply {
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
            rawValues.forEach {
                simpleXYSeries.addLast(null, it)
            }
            addSeries(simpleXYSeries, LineAndPointFormatter(colorPrimary, null, null, null))
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

    fun showDateTimePicker(initialDate: DateTime, callback: (date: DateTime) -> Unit) {
        val startDate = Calendar.getInstance().apply { time = initialDate.toDate() }
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
                        callback(DateTime(date))
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