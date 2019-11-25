package pl.polsl.temperature.management

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*
import pl.polsl.temperature.R

class ManagementActivityImpl: AppCompatActivity(), ManagementActivity {

    private val managementPresenter: ManagementPresenter = ManagementPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_management)
        setSupportActionBar(toolbar)
        managementPresenter.getUserData()
    }

}