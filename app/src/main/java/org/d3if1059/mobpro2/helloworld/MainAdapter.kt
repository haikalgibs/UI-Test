package org.d3if1059.mobpro2.helloworld

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.d3if1059.mobpro2.helloworld.model.Harian
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    private val formatter = SimpleDateFormat("dd MMMM", Locale("ID", "id"))
    private val data = ArrayList<Harian>()

    fun setData(newData: List<Harian>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun getDate(position: Int): Date {
        return Date(data[position].key)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_main, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tanggalTextView = itemView.findViewById<TextView>(R.id.tanggal)
        private val positifTextView =
            itemView.findViewById<TextView>(R.id.jumlahPositif)

        fun bind(harian: Harian) {
            tanggalTextView.text = formatter.format(Date(harian.key))
            positifTextView.text = itemView.context.getString(
                R.string.x_orang,
                harian.jumlahPositif.value
            )
        }
    }
}
