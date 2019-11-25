package pl.polsl.temperature.credentials.register

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.getMessageString
import pl.polsl.temperature.models.Message
import pl.polsl.temperature.models.RegisterUser
import pl.polsl.temperature.services.CredentialsService
import pl.polsl.temperature.utils.OneToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class RegisterPresenterImpl(private val loginActivity: RegisterActivity): RegisterPresenter {

    private val credentialsService: CredentialsService = ApplicationContext.getRetrofit().create()

    override fun register(registerUser: RegisterUser) {
        credentialsService.register(registerUser).enqueue(object: Callback<Message> {
            override fun onFailure(call: Call<Message>, e: Throwable) {
                OneToast.show(e.message)
                loginActivity.registerFailed()
            }

            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if(response.isSuccessful)
                    loginActivity.registerSucceed()
                else{
                    OneToast.show(response.getMessageString())
                    loginActivity.registerFailed()
                }
            }
        })
    }

}