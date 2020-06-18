package com.example.tracker.ui.news

import com.example.tracker.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import java.io.IOException

class NewsRepository {

    fun getNews(query: String, onResult: (ArrayList<News>) -> Unit, onFailure: (String) -> Unit) {
        val list = ArrayList<News>()

        GlobalScope.launch {
            try {
                val document =
                    Jsoup.connect("https://news.google.com/search?q=$query%20COVID-19&hl=en-GB&gl=GB&ceid=GB%3Aen")
                        .get()
                val elements = document.select("div.NiLAwe.y6IFtc.R7GTQ.keNKEd.j7vNaf.nID9nc")

                elements.forEachIndexed { index, it ->
                    val title = it.select("div.xrnccd")
                        .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                        .select("h3.ipQwMb.ekueJc.gEATFF.RD0gLb")
                        .select("a")
                        .text()

                    val link = it.select("div.xrnccd")
                        .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                        .select("h3.ipQwMb.ekueJc.gEATFF.RD0gLb > a")
                        .attr("href")

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

                    val image = it.select("a")
                        .select("figure.AZtY5d.fvuwob.d7hoq > img")
                        .attr("src")


                    list.add(
                        News(
                            title,
                            resource,
                            time,
                            "https://news.google.com/$link",
                            image.replace("h100", "h800").replace("w100", "w800")
                        )
                    )

                }

                GlobalScope.launch(Dispatchers.Main) {
                    onResult(list)
                }
            } catch (e: IOException) {
                onFailure(e.message.toString())
            }

        }


    }

    fun getTrueLink(s: String, onResult: (String) -> Unit, onFailure: (String) -> Unit) {
        var link: String = ""
        GlobalScope.launch {
            try {
                val document = Jsoup.connect(s).get()
                link = document.select("div.m2L3rb > a")
                    .attr("href")

                GlobalScope.launch(Dispatchers.Main) {
                    onResult(link)
                }
            } catch (e: IOException) {
                onFailure(e.message.toString())
            }
        }
    }

}
