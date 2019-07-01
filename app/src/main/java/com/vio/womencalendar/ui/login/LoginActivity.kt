package com.vio.womencalendar.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.vio.womencalendar.*
import com.vio.womencalendar.app.Injection
import com.vio.womencalendar.model.token.Token
import com.vio.womencalendar.model.user.User
import com.vio.womencalendar.ui.main.MainActivity


class LoginActivity : AppCompatActivity() {

    private val api = Injection.provideAuthAPI()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginTitle.show()

        login_next.setOnClickListener {

            emailInput.validate({
                s -> s.isValidEmail()
            }, getString(R.string.error_email))

            pass_input.validate({
                s -> s.isValidPass()
            }, getString(R.string.error_pass))

            if (emailInput.text.toString().isValidEmail() &&
                pass_input.text.toString().isValidPass()) {
                sendPostRequest(login = emailInput.text.toString(), pass = pass_input.text.toString())
            }
        }

        radioButtonLogin.setOnClickListener {
            loginTitle.animate().alpha(0f).setDuration(100).withEndAction{
                loginTitle.text = resources.getText(R.string.description_login)
                loginTitle.show()

                login_next.setOnClickListener{

                }
            }
        }



    }

    fun sendPostRequest(login:String, pass:String) {
        api.getAccessToken(User(login, pass))
            .enqueue(object : Callback<Token> {
                override fun onResponse(call: Call<Token>?, response: Response<Token>?) {
                    val accessToken = response?.body()?.token
                    Log.d("LoginActivity", "token $accessToken")
                }
                override fun onFailure(call: Call<Token>?, t: Throwable?) {
                    Log.e("MainViewModel", "Error getting token")
                }
            })

               // CalendarApplication.prefs.edit().putString("token", token).apply()
              //.  Toast.makeText(applicationContext, "token: $token, response: $response", Toast.LENGTH_LONG).show()
                //goToMainActivity()

    }
    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
