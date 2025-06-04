package hr.algebra.sabitify.adapter

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.sabitify.R
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_ITEMS
import hr.algebra.sabitify.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class EventPagerAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<EventPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val ivRead = itemView.findViewById<ImageView>(R.id.ivRead)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvEventTitleText)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvExplanation = itemView.findViewById<TextView>(R.id.tvEventDescription)
        private val tvLocation = itemView.findViewById<TextView>(R.id.tvEventVenue)
        @SuppressLint("SetTextI18n")
        fun bind(item: Item) {
            tvItem.text = item.title
            tvDate.text = item.date?.start_date ?: "no date"
            tvExplanation.text = item.description
            ivRead.setImageResource(
                if (item.liked) R.drawable.liked else R.drawable.disliked
            )
            if ( item.address?.city == "" && item.address?.street == ""){
                tvLocation.text ="unknown"
            }
            else{
                tvLocation.text = item.address?.city + ", " + item.address?.street
            }


            Picasso.get()
                .load(File(item.image))
                .error(R.drawable.sabitify_logo)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.event_pager, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivRead.setOnClickListener {
            updateItem(position)
        }
        holder.bind(items[position])
    }

    private fun updateItem(position: Int) {
        val item = items[position]
        item.liked = !item.liked
        context.contentResolver.update(
            ContentUris.withAppendedId(SABITIFY_PROVIDER_CONTENT_URI_ITEMS, item._id!!),
            ContentValues().apply {
                put(Item::liked.name, item.liked)
            },
            null,
            null
        )
        notifyItemChanged(position)
    }
}

