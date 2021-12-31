package ie.wit.assignment1.views.hikelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.assignment1.databinding.CardHikeBinding
import ie.wit.assignment1.models.HikeModel


interface HikeListener {
    fun onHikeClick(hike: HikeModel)
}

class HikeAdapter constructor(
    private var hikes: List<HikeModel>,
    private val listener: HikeListener
):

    RecyclerView.Adapter<HikeAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardHikeBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        if (presenter.edit){
            deleteMenu.setVisible(true)
        }
        else{
            deleteMenu.setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hike = hikes[holder.adapterPosition]
        holder.bind(hike, listener)
    }

    override fun getItemCount(): Int = hikes.size

    class MainHolder(private val binding : CardHikeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hike: HikeModel, listener: HikeListener) {
            binding.hikeTitle.text = hike.title
            binding.description.text = hike.description
            Picasso.get().load(hike.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onHikeClick(hike) }
        }
    }
}

