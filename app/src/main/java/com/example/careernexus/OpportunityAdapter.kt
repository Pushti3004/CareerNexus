package com.example.careernexus

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OpportunityAdapter(
    private var items: List<Opportunity>,
    private val onItemClick: (Opportunity) -> Unit
) : RecyclerView.Adapter<OpportunityAdapter.ViewHolder>() {

    private var fullList: List<Opportunity> = items

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvType: TextView = view.findViewById(R.id.tvType)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDeadline: TextView = view.findViewById(R.id.tvDeadline)
        val tvCountdown: TextView = view.findViewById(R.id.tvCountdown)
    }

    // This line is what "fetches" single_item.xml as each card's layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]

        holder.tvType.text = item.type
        holder.tvTitle.text = item.title

        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.tvDeadline.text = "Deadline: ${sdf.format(Date(item.deadline))}"

        val diff = item.deadline - System.currentTimeMillis()
        val daysLeft = TimeUnit.MILLISECONDS.toDays(diff)

        holder.tvCountdown.text = when {
            diff < 0 -> "Overdue"
            daysLeft == 0L -> "Today!"
            daysLeft == 1L -> "1 day left"
            else -> "$daysLeft days left"
        }

        holder.itemView.setOnClickListener { onItemClick(item) }
    }


    override fun getItemCount() = items.size

    fun updateList(newItems: List<Opportunity>) {
        items = newItems
        fullList = newItems
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        items = if (query.isEmpty()) fullList else fullList.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.type.contains(query, ignoreCase = true)
        }
        notifyDataSetChanged()
    }
}