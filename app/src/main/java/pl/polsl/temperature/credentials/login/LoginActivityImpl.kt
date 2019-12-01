package pl.polsl.temperature.credentials.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.toolbar.*
import pl.polsl.temperature.R
import pl.polsl.temperature.application.BaseActivity
import pl.polsl.temperature.credentials.register.RegisterActivityImpl
import pl.polsl.temperature.management.ManagementActivityImpl
import pl.polsl.temperature.models.AuthenticationPost
import pl.polsl.temperature.utils.OneToast
import pl.polsl.temperature.utils.SettingsTools

class LoginActivityImpl :
    BaseActivity(),
    LoginActivity,
    View.OnClickListener {

    private val loginPresenter: LoginPresenter = LoginPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        if(SettingsTools.getToken() != null)
            loginSucceed()
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
        loginPresenter.login(AuthenticationPost(
            username = usernameEditText.text.toString(),
            password = passwordEditText.text.toString()
        ))
    }

    override fun loginSucceed() {
        setOf<EditText>(usernameEditText, passwordEditText).forEach {
            it.text = null
        }
        startActivity(Intent(this, ManagementActivityImpl::class.java))
    }

    override fun getContext(): BaseActivity {
        return this
    }

}
