package com.example.tracker.data.remote

import com.example.tracker.Constants.Status
import com.example.tracker.data.local.entity.*
import com.example.tracker.data.remote.retrofit.ApiService
import com.example.tracker.utils.Result
import com.example.tracker.utils.Utils
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject


class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    var newsCoroutine: Job = Job()

    override suspend fun getGlobalStatistic(): Result<Statistic?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val statistic = apiService.getOverallStatistic()
            if (statistic.isSuccessful) {
                Result.success(statistic.body())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.error(e.message.toString(), null)
        }
    }

    override suspend fun getGlobalHistoric(): Result<TimeLine?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val statistic = apiService.getOverallHistoric()
            if (statistic.isSuccessful) {
                Result.success(statistic.body())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.error(e.message.toString(), null)
        }
    }


    override suspend fun getCountries(): Result<List<Country>?> = withContext(Dispatchers.IO) {
        return@withContext try {
            val statistic = apiService.getCountriesStatistic()
            if (statistic.isSuccessful) {
                Result.success(statistic.body())
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.error(e.message.toString(), null)
        }
    }

    override suspend fun getHistoricForCountries(countries: String): Result<List<Historic>?> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val statistic = apiService.getCountriesHistoric(countries)
                if (statistic.isSuccessful) {
                    Result.success(statistic.body())
                } else {
                    Result.success(null)
                }
            } catch (e: Exception) {
                Result.error(e.message.toString(), null)
            }
        }

    override suspend fun getNews(
        query: String,
        locale: String,
        onResult: (Result<ArrayList<News>>) -> Unit
    ) {

        if (newsCoroutine.isActive) {
            newsCoroutine.cancel()
        }

        newsCoroutine = GlobalScope.launch(Dispatchers.Default) {
            try {
                val list = ArrayList<News>()
                val document =
                    Jsoup.connect("https://news.google.com/search?q=$query%20COVID-19&hl=$locale-GB&gl=${locale.toUpperCase()}&ceid=${locale.toUpperCase()}%3A$locale")
                        .get()
                val elements = document.select("div.NiLAwe.y6IFtc.R7GTQ.keNKEd.j7vNaf.nID9nc")

                elements.forEachIndexed { index, it ->
                    val linkAsync = GlobalScope.async { // creates worker thread
                        var link = withContext(Dispatchers.Default) {
                            it.select("div.xrnccd")
                                .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                                .select("h3.ipQwMb.ekueJc.RD0gLb > a")
                                .attr("href")
                        }

                        link = withContext(Dispatchers.Default) {
                            Utils.getTrueLink("https://news.google.com/$link")
                        }

                        val loadStatus = when (index == elements.size - 1) {
                            true -> {
                                Status.SUCCESS
                            }
                            else -> {
                                Status.LOADING
                            }
                        }

                        val title = it.select("div.xrnccd")
                            .select("article.MQsxIb.xTewfe.R7GTQ.keNKEd.j7vNaf.Cc0Z5d.EjqUne")
                            .select("h3.ipQwMb.ekueJc.RD0gLb")
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

                        if (link.trim().isNotEmpty()) {
                            list.add(
                                News(
                                    title,
                                    resource,
                                    time,
                                    link,
                                    image.replace("h100", "h500").replace("w100", "w500"),
                                    loadStatus
                                )
                            )
                        }
                    }

                    linkAsync.await()
                    onResult(Result.loading(list))
                }
                onResult(Result.success(list))
            } catch (e: IOException) {
                onResult(Result.error(e.message!!, null))
            }
        }
    }
}