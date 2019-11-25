package pl.polsl.temperature.credentials.login

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.getMessageString
import pl.polsl.temperature.models.Credentials
import pl.polsl.temperature.models.CredentialsResponse
import pl.polsl.temperature.services.CredentialsService
import pl.polsl.temperature.utils.OneToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class LoginPresenterImpl(private val loginActivity: LoginActivity): LoginPresenter {

    private val credentialsService: CredentialsService = ApplicationContext.getRetrofit().create()

    override fun login(credentials: Credentials) {
        credentialsService.login(credentials).enqueue(object: Callback<CredentialsResponse>{
            override fun onFailure(call: Call<CredentialsResponse>, e: Throwable) {
                OneToast.show(e.message)
                loginActivity.loginFailed()
            }

            override fun onResponse(call: Call<CredentialsResponse>, response: Response<CredentialsResponse>) {
                if(response.isSuccessful){
                    ApplicationContext.token = response.body()?.token.toString()
                    loginActivity.loginSucceed()
                } else{
                    OneToast.show(response.getMessageString())
                    loginActivity.loginFailed()
                }
            }

        })
    }

}