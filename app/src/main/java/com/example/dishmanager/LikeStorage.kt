package com.example.dishmanager

import android.content.Context
import android.content.SharedPreferences
import com.example.dishmanager.models.Dish
import org.json.JSONArray
import org.json.JSONObject

class LikeStorage(private val context: Context) {


    private val sharedPref = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    fun addLikedItem(dish: Dish) {

        val json = sharedPref.getString("favorite", "[]")
        val jsonArray = JSONArray(json)
        val jsonObj = JSONObject().apply {

            put("dishId", dish.dishId)
            put("dishName", dish.dishName)
            put("category", dish.category)
            put("imagePath", dish.imagePath)
            put("finalPriceForClients", dish.finalPriceForClients)
            put("like", dish.like)
        }

        jsonArray.put(jsonObj)

        sharedPref.edit().putString("favorite", jsonArray.toString()).apply()

    }

    fun removeLikedItem(dish: Dish) {

        val json = sharedPref.getString("favorite", "[]")
        val jsonArray = JSONArray(json)

        for (position in 0 until jsonArray.length()) {

            val jsonObj = jsonArray.getJSONObject(position)
            val id = jsonObj.getInt("dishId")

            if (id == dish.dishId) {

                jsonArray.remove(position)
                break

            }

        }

        sharedPref.edit().putString("favorite", jsonArray.toString()).apply()

    }

    fun getLikedItems(): List<Dish> {

        val dishes = mutableListOf<Dish>()

        val json = sharedPref.getString("favorite", "[]")
        val jsonArray = JSONArray(json)

        for (position in 0 until jsonArray.length()) {

            val jsonObj = jsonArray.getJSONObject(position)

            val dish = Dish(
                dishId = jsonObj.getInt("dishId"),
                dishName = jsonObj.getString("dishName"),
                category = jsonObj.getString("category"),
                imagePath = jsonObj.getString("imagePath"),
                finalPriceForClients = jsonObj.getDouble("finalPriceForClients"),
                like = jsonObj.getBoolean("like")
            )

            dishes.add(dish)

        }

        return dishes

    }

}