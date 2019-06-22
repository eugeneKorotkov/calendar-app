package ru.korotkov.womencalendar.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import ru.korotkov.womencalendar.*
import ru.korotkov.womencalendar.app.CalendarApplication
import ru.korotkov.womencalendar.ui.main.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginTitle.show()

        login_next.setOnClickListener {

            email_input.validate({
                s -> s.isValidEmail()
            }, getString(R.string.error_email))

            pass_input.validate({
                s -> s.isValidPass()
            }, getString(R.string.error_pass))

            if (email_input.text.toString().isValidEmail() &&
                pass_input.text.toString().isValidPass()) {
                sendPostRequest(login = email_input.text.toString(), pass = pass_input.text.toString())
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

        var reqParam = URLEncoder.encode("login", "UTF-8") + "=" + URLEncoder.encode(login, "UTF-8")
        reqParam += "&" + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8")
        reqParam += "&" + URLEncoder.encode(
            "data",
            "UTF-8"
        ) + "=" + URLEncoder.encode("{\"month\":\"08\",\"year\":\"2014\",\"count\":\"2\"}", "UTF-8")
        val mURL = URL("http://134.209.23.52:48656/api/v1/sing-up")

        with(mURL.openConnection() as HttpURLConnection) {
            // optional default is GET
            requestMethod = "POST"

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(reqParam);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")
                val substring = response.substring(response.indexOf("token") + 8)
                val token = substring.substring(0, substring.indexOf('"'))
                println("Response : $token")
                CalendarApplication.prefs.edit().putString("token", token).apply()
                goToMainActivity()
            }
        }
    }
    fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
