package com.example.dishmanager.models

data class Dish(

    val dishId: Int,
    val dishName: String,
    val category: String,
    val imagePath: String,
    val finalPriceForClients: Double,
    var like: Boolean = false

)