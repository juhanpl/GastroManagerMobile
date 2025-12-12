package com.example.dishmanager.repository

import com.example.dishmanager.models.Dish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class DishRepository {

    private val urlString = "http://10.0.2.2:5211/api/dishes"

    suspend fun getDishes() : List<Dish>
    = withContext(Dispatchers.IO) {

        val dishes = mutableListOf<Dish>()

        val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {

            requestMethod = "GET"
            doInput = true
            connectTimeout = 5000
            readTimeout = 5000
            setRequestProperty("Content-Type", "application/json; charset=utf-8")

        }

        val json = connection.getInputStream().bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)

        for (position in 0 until jsonArray.length()) {

            val jsonObj = jsonArray.getJSONObject(position)

            val dish = Dish(

                dishId = jsonObj.getInt("dishId"),
                dishName = jsonObj.getString("dishName"),
                category = jsonObj.getString("category"),
                imagePath = jsonObj.getString("imagePath"),
                finalPriceForClients = jsonObj.getDouble("finalPriceForClients")

            )

            dishes.add(dish)

        }

        dishes


    }

}