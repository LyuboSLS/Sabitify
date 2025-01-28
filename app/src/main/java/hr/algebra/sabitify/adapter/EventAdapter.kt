package hr.algebra.sabitify.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.sabitify.EventPagerActivity
import hr.algebra.sabitify.ITEM_POSITION
import hr.algebra.sabitify.R
import hr.algebra.sabitify.SABITIFY_PROVIDER_CONTENT_URI_ITEMS
import hr.algebra.sabitify.framework.startActivity
import hr.algebra.sabitify.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class EventAdapter(
    private val context: Context,
    private val items: MutableList<Item>
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
        fun bind(item: Item) {
            tvItem.text = item.title
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
                .inflate(R.layout.event_item, parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnClickListener {
            context.startActivity<EventPagerActivity>(ITEM_POSITION, position)
        }
        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context).apply {
                setTitle(context.getString(R.string.delete))
                setMessage(context.getString(R.string.are_you_sure_you_want_to_delete))
                setIcon(R.drawable.delete)
                setCancelable(true)
                setPositiveButton("OK") { _, _ -> deleteItem(position) }
                setNegativeButton(R.string.cancel, null)
                show()
            }
            true
        }


        holder.bind(item)
    }

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(SABITIFY_PROVIDER_CONTENT_URI_ITEMS, item._id!!),
            null,
            null
        )
        items.removeAt(position)
        File(item.image).delete()
        notifyDataSetChanged()
    }
}