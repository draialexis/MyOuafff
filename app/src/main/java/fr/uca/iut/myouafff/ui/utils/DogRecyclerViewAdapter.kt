package fr.uca.iut.myouafff.ui.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import fr.uca.iut.myouafff.R
import fr.uca.iut.myouafff.data.Dog

class DogRecyclerViewAdapter(private var dogList: List<Dog>, private val listener: Callbacks) :
    RecyclerView.Adapter<DogRecyclerViewAdapter.DogViewHolder>() {
    override fun getItemCount() = dogList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DogViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list_dog,
                parent,
                false
            ), listener
        )

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) =
        holder.bind(dogList[position])

    class DogViewHolder(itemView: View, listener: Callbacks) :
        RecyclerView.ViewHolder(itemView) {

        private val viewName = itemView.findViewById<TextView>(R.id.view_name)
        private val viewBreed = itemView.findViewById<TextView>(R.id.view_breed)
        private val dogCardview = itemView.findViewById<CardView>(R.id.dog_cardview)

        var dog: Dog? = null
            private set

        init {
            itemView.setOnClickListener { dog?.let { listener.onDogSelected(it.id) } }
        }

        fun bind(dog: Dog) {
            this.dog = dog
            viewName.text = dog.name
            val context = itemView.context
            val breed = dog.breed
            viewBreed.text = breed.ifEmpty { context.getString(R.string.unknown_breed) }
            val color =
                context.resources.getIntArray(R.array.aggressiveness_color)[dog.aggressiveness]
            dogCardview.setCardBackgroundColor(color)
        }
    }

    fun updateList(dogList: List<Dog>) {
        this.dogList = dogList
        notifyDataSetChanged()
    }

    interface Callbacks {
        fun onDogSelected(dogId: Long)
    }

}