package com.example.dishmanager.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dishmanager.LikeStorage
import com.example.dishmanager.R
import com.example.dishmanager.models.Dish

data class Item(val like: Boolean)

class DishAdapter(private val context: Context, private var dishes: List<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    private val likeStorage = LikeStorage(context = context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DishViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.dish_item, parent, false)
        return DishViewHolder(inflate)
    }

    override fun onBindViewHolder(
        holder: DishViewHolder,
        position: Int
    ) {

        holder.bind(dishes[position])
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun updateData(newDishes: List<Dish>) {

        dishes = newDishes
        notifyDataSetChanged()

    }

    fun getLikedDishes() {

        dishes = likeStorage.getLikedItems()
        notifyDataSetChanged()

    }

    inner class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitleDish = view.findViewById<TextView>(R.id.txtTitleDish)
        private val imgDish = view.findViewById<ImageView>(R.id.imgDish)
        private val txtFinalPrice = view.findViewById<TextView>(R.id.txtFinalPrice)
        private val btnFavorite = view.findViewById<ImageButton>(R.id.btnFavorite)

        fun bind(dish: Dish) {

            dish.like = likeStorage.getLikedItems().any { likedDish -> likedDish.dishId == dish.dishId }

            txtTitleDish.text = dish.dishName
            txtFinalPrice.text = "Final Price: " + dish.finalPriceForClients + " $"

            //Load Image
            val assetManager = itemView.context.assets
            val inputStream = assetManager.open(dish.imagePath)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imgDish.setImageBitmap(bitmap)


            fun updateIcon() {

                btnFavorite.setImageResource(
                    if (dish.like) {
                        R.drawable.heart_filled
                    } else {
                        R.drawable.heart_outline
                    }
                )

            }

            val btnHeart = btnFavorite.setOnClickListener {

                dish.like = !dish.like

                if (dish.like) {
                    likeStorage.addLikedItem(dish)
                } else {
                    likeStorage.removeLikedItem(dish)
                }
                updateIcon()

            }

            updateIcon()


        }

    }
}