package ru.korotkov.womencalendar.ui.articles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_articles.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.korotkov.womencalendar.R
import ru.korotkov.womencalendar.app.Injection
import ru.korotkov.womencalendar.model.arcticle.Article
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ArticlesFragment: Fragment() {

    private val api = Injection.provideServerAPI()
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesRecyclerView.layoutManager = LinearLayoutManager(context)


        api.getArticles().enqueue(object : Callback<List<Article>> {
            override fun onResponse(call: Call<List<Article>>?, response: Response<List<Article>>?) {
                if (response != null && response.isSuccessful) {
                    adapter = ArticleAdapter(response.body()!!)
                    articlesRecyclerView.adapter = adapter
                }
            }
            override fun onFailure(call: Call<List<Article>>?, t: Throwable?) {
            }
        })

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
                Log.d("TAGGGGG", "token " + token)
            }
        }
    }
}
