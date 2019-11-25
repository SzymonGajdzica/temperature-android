package pl.polsl.temperature.credentials.register

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.polsl.temperature.R
import pl.polsl.temperature.models.RegisterUser
import pl.polsl.temperature.utils.OneToast

class RegisterActivityImpl : AppCompatActivity(),
    RegisterActivity, View.OnClickListener {

    private val loginPresenter: RegisterPresenter =
        RegisterPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.registerButton -> handleRegisterClick()
        }
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        toolbar?.setNavigationIcon(R.drawable.baseline_arrow_back_white_24)
        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun handleRegisterClick(){
        if(setOf<EditText>(newEmailEditText, newUsernameEditText, newSurnameEditText,
                newPasswordEditText, newPasswordConfirmEditText, newNameEditText).any {
                it.text.isNullOrBlank()
        }){
            OneToast.show(R.string.fillAllData)
            return
        }
        if(newPasswordConfirmEditText.text.toString() != newPasswordEditText.text.toString()){
            OneToast.show(R.string.passwordsDoesNotMatch)
            return
        }
        showLoader()
        loginPresenter.register(RegisterUser(
            username = newUsernameEditText.text.toString(),
            password = newPasswordEditText.text.toString(),
            surname = newSurnameEditText.text.toString(),
            name = newNameEditText.text.toString(),
            email = newEmailEditText.text.toString()
        ))
    }

    private fun showLoader(){
        loader.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        loader.visibility = View.GONE
    }

    override fun registerSucceed() {
        hideLoader()
        OneToast.show(R.string.nowLogin)
        finish()
    }

    override fun registerFailed() {
        hideLoader()
    }


}
