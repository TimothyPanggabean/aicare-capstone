package com.example.capstone.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.capstone.R
import com.example.capstone.data.remote.pojo.ListHistoryItem
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class ListHistoryAdapter(
    private val listHistory: ArrayList<ListHistoryItem>
) : RecyclerView.Adapter<ListHistoryAdapter.ListViewHolder>() {

    val pattern = "dd-MM-yyyy"
    val simpleDateFormat = SimpleDateFormat(pattern)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_row_history, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.tvLabel.text = listHistory[position].outputlabel

        val percentage = String.format("%.2f", (listHistory[position].outputpercentage?.toFloat()
            ?: 0.0f) * 100) + "%"
        holder.tvPercentage.text = percentage

        val expiry = Date((listHistory[position].date?.seconds?.toLong() ?: 0) * 1000)
        val date: String = simpleDateFormat.format(expiry)
        holder.tvDate.text = date
    }

    override fun getItemCount(): Int = listHistory.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvLabel: TextView = itemView.findViewById(R.id.tv_item_label)
        var tvPercentage: TextView = itemView.findViewById(R.id.tv_item_percentage)
        var tvDate: TextView = itemView.findViewById(R.id.tv_item_date)
    }
}