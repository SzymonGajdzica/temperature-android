package pl.polsl.temperature.credentials.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.polsl.temperature.R
import pl.polsl.temperature.credentials.register.RegisterActivityImpl
import pl.polsl.temperature.management.ManagementActivityImpl
import pl.polsl.temperature.models.Credentials
import pl.polsl.temperature.utils.OneToast

class LoginActivityImpl : AppCompatActivity(),
    LoginActivity, View.OnClickListener {

    private val loginPresenter: LoginPresenter = LoginPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.noAccountButton -> openRegisterActivity()
            R.id.loginButton -> handleLoginClick()
        }
    }

    private fun openRegisterActivity(){
        startActivity(Intent(this, RegisterActivityImpl::class.java))
    }

    private fun handleLoginClick(){
        if(setOf<EditText>(usernameEditText, passwordEditText).any {
                it.text.isNullOrBlank()
            }){
            OneToast.show(R.string.fillAllData)
            return
        }
        showLoader()
        loginPresenter.login(Credentials(
            username = usernameEditText.text.toString(),
            password = passwordEditText.text.toString()
        ))
    }

    private fun showLoader(){
        loader.visibility = View.VISIBLE
    }

    private fun hideLoader(){
        loader.visibility = View.GONE
    }

    override fun loginSucceed() {
        hideLoader()
        OneToast.show(R.string.loggedSuccessfully)
        startActivity(Intent(this, ManagementActivityImpl::class.java))
    }

    override fun loginFailed() {
        hideLoader()
    }

}