package com.yapps.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var nAdapter : NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchData()

        nAdapter = NewsListAdapter(this)

        recyclerView.adapter = nAdapter

    }

    private fun fetchData(){
        val url = "https://newsapi.org/v2/everything?q=keyword&apiKey=3f929106cbcb4ff89128562a72924dca"

        val jsonObjectRequest = object  : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newJSONObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newJSONObject.getString("title"),
                        newJSONObject.getString("author"),
                        newJSONObject.getString("url"),
                        newJSONObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                nAdapter.updateNews(newsArray)
            },
            {

            }
        ){
             override fun getHeaders():MutableMap<String, String>{
                val headers = HashMap<String, String>()
                headers["User-Agent"] = "Mozilla/5.0"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}