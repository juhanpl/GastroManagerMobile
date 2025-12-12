package com.example.dishmanager

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dishmanager.adapter.DishAdapter
import com.example.dishmanager.models.Category
import com.example.dishmanager.models.Dish
import com.example.dishmanager.repository.CategoryRepository
import com.example.dishmanager.repository.DishRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dishes: List<Dish> = emptyList()
        val dishRepository = DishRepository()
        val categoryRepository = CategoryRepository()

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = DishAdapter(this, emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val txtSearch = findViewById<EditText>(R.id.txtSearch)
        val spinner = findViewById<Spinner>(R.id.cbCategory)


        lifecycleScope.launch {

            val categories = categoryRepository.getCategories()
            val spinnerAdapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                categories
                )
            spinner.adapter = spinnerAdapter

            val newDishes = dishRepository.getDishes()
            dishes = newDishes
            adapter.updateData(newDishes)

        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parent.getItemAtPosition(position) as String

                if (selectedCategory == "Select...") {

                    adapter.updateData(dishes)

                } else if (selectedCategory == "Favorite") {

                    adapter.getLikedDishes()


                } else {

                    val searchedByCategoryDishes = dishes.filter { dish -> dish.category == selectedCategory }
                    adapter.updateData(searchedByCategoryDishes)

                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        txtSearch.doOnTextChanged { text, start, before, count ->

            val searchedByNameDishes = dishes.filter { dish -> dish.dishName.contains(text.toString(), ignoreCase = true) }
            adapter.updateData(searchedByNameDishes)


        }

    }
}