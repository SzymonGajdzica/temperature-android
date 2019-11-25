package pl.polsl.temperature.management

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.models.User
import pl.polsl.temperature.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class ManagementPresenterImpl(private val managementActivity: ManagementActivity): ManagementPresenter {

    private val userService: UserService = ApplicationContext.getRetrofit().create()

    override fun getUserData() {
        userService.getUserData().enqueue(object: Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {

            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                ApplicationContext.log(response.body().toString())
            }

        })
    }

}