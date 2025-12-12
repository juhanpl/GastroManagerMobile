package com.example.dishmanager.repository

import com.example.dishmanager.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class CategoryRepository {

    val urlString = "http://10.0.2.2:5211/api/categories"

    suspend fun getCategories() : List<String> = withContext(Dispatchers.IO) {

        val categories = mutableListOf<String>()

        val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {

            requestMethod = "GET"
            readTimeout = 5000
            connectTimeout = 5000
            doInput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")

        }

        val json = connection.getInputStream().bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)

        categories.add("Select...")

        for (position in 0 until jsonArray.length()) {

            val jsonObj = jsonArray.getJSONObject(position)

            val category = jsonObj.getString("categoryName")

            categories.add(category)

        }

        categories.add("Favorite")

        categories
    }

}