package ru.korotkov.womencalendar

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import ru.korotkov.womencalendar.ui.steps.slider.OnSnapPostionChangeListener
import ru.korotkov.womencalendar.ui.steps.slider.SnapOnScrollListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun EditText.validate(validator: (String) -> Boolean, message: String)  {
    this.afterTextChanged {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.text.toString())) null else message
}

fun View.show() {
    this.animate().alpha(1f).setDuration(100)
}
fun View.hide() {
    this.animate().alpha(0f).duration = 100
}

fun String.isValidPass(): Boolean = this.length >= 6 && this.matches("^[a-zA-Z0-9]+$".toRegex())

fun sendPostRequest(login:String, pass:String): String {

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
            return token
        }
    }
}

fun String.isValidEmail(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}


fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    var snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}
fun RecyclerView.attachSnapHelperWithListener(

    snapHelper: SnapHelper,
    behavior: SnapOnScrollListener.Behavior = SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,

    onSnapPositionChangeListener: OnSnapPostionChangeListener
) {
    snapHelper.attachToRecyclerView(this)

    val snapOnScrollListener =
        SnapOnScrollListener(
            snapHelper,
            behavior,
            onSnapPositionChangeListener
        )
    addOnScrollListener(snapOnScrollListener)
}

fun Activity.setTransparentStatusBar() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = Color.TRANSPARENT
    }
}
