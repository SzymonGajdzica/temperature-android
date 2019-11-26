package pl.polsl.temperature.utils

import androidx.core.content.edit
import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.dateToString
import pl.polsl.temperature.application.stringToDate
import pl.polsl.temperature.models.CredentialsResponse
import java.util.*

object SettingsTools {

    private const val tokenKey = "tokenKey"
    private const val tokenExpirationDateKey = "tokenExpirationDateKey"

    fun getToken(): String?{
        val sharedPreferences = ApplicationContext.getSharedPreferences() ?: return null
        val expirationDate = sharedPreferences.getString(tokenExpirationDateKey, null).stringToDate() ?: return null
        if(expirationDate > Date())
            return sharedPreferences.getString(tokenKey, null)
        return null
    }

    fun setToken(credentialsResponse: CredentialsResponse?){
        ApplicationContext.getSharedPreferences()?.edit(commit = true) {
            putString(tokenKey, credentialsResponse?.token)
        }
        ApplicationContext.getSharedPreferences()?.edit(commit = true) {
            putString(tokenExpirationDateKey, credentialsResponse?.expirationDate?.dateToString())
        }
    }


}