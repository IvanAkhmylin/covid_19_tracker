package com.example.tracker.ui.news

import android.util.Log
import com.example.tracker.Constants.Status
import com.example.tracker.model.News
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.io.IOException

class NewsRepository {
    var coroutine: Job = Job()

    fun getNews(query: String,locale: String, onResult: (ArrayList<News> , String) -> Unit, onFailure: (String) -> Unit) {
        val list = ArrayList<News>()

        if (coroutine.isActive){
            coroutine.cancel()
        }

        coroutine = GlobalScope.launch(Dispatchers.Default) {
            try {

                val document =
                    Jsoup.connect("https://news.google.com/search?q=$query%20COVID-19&hl=$locale-GB&gl=${locale.toUpperCase()}&ceid=${locale.toUpperCase()}%3A$locale")
                        .get()
                val elements = document.select("div.NiLAwe.y6IFtc.R7GTQ.keNKEd.j7vNaf.nID9nc")

                elements.forEachIndexed { index, it ->

                    val linkAsync = GlobalScope.async { // creates worker thread
                        var link = withContext(Dispatchers.Default) {
                            it.select("div.xrnccd")
                                .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                                .select("h3.ipQwMb.ekueJc.gEATFF.RD0gLb > a")
                                .attr("href")
                        }

                        link = withContext(Dispatchers.Default) {
                            getTrueLink("https://news.google.com/$link")
                        }

                        val title = it.select("div.xrnccd")
                            .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                            .select("h3.ipQwMb.ekueJc.gEATFF.RD0gLb")
                            .select("a")
                            .text()

                        val image = it.select("a")
                            .select("figure.AZtY5d.fvuwob.d7hoq > img")
                            .attr("src")

                        val resource = it.select("div.xrnccd")
                            .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                            .select("div.QmrVtf.RD0gLb")
                            .select("a.wEwyrc.AVN2gc.uQIVzc.Sksgp")
                            .text()

                        val time = it.select("div.xrnccd")
                            .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                            .select("div.QmrVtf.RD0gLb")
                            .select("time.WW6dff.uQIVzc.Sksgp")
                            .text()

                        list.add(
                            News(
                                title,
                                resource,
                                time,
                                link,
                                image.replace("h100", "h500").replace("w100", "w500")
                            )
                        )
                    }

                    linkAsync.await()

                    if (index == elements.size - 1){
                        onResult(list , Status.SUCCESS)
                    }else{
                        if (list.get(index).link.isNotEmpty()){
                            onResult(list, Status.LOADING)
                        }
                    }
                }

            } catch (e: IOException) {
                onFailure(e.message.toString())
            }
        }
    }

    fun getTrueLink(s: String): String {
        var link = ""
        try {
            val document = Jsoup.connect(s).get()
            link = document.select("div.m2L3rb > a")
                .attr("href")


        } catch (e: IOException) {
            Log.d("TAG", e.message.toString())
        }

        return link

    }


}
