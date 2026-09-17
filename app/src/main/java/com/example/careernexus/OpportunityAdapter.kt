package com.example.careernexus

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OpportunityAdapter(){
    class OpportunityAdapter(
        private val opportunities: MutableList<Opportunity>) : RecyclerView.Adapter<OpportunityAdapter.OpportunityViewHolder>() {
        class OpportunityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvType: TextView = itemView.findViewById(R.id.tvType)
            val tvCountdown: TextView = itemView.findViewById(R.id.tvCountdown)
            val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
            val tvDeadline: TextView = itemView.findViewById(R.id.tvDeadline)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): OpportunityViewHolder {

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_item, parent, false)

            return OpportunityViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: OpportunityViewHolder,
            position: Int
        ) {
            val opportunity = opportunities[position]
            holder.tvType.text = opportunity.type
            holder.tvTitle.text = opportunity.title

            val formatter = SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            )

            holder.tvDeadline.text =
                "Deadline: ${formatter.format(Date(opportunity.deadlineMillis))}"

            holder.tvCountdown.text =
                getRemainingTime(opportunity.deadlineMillis)
        }

        override fun getItemCount(): Int {
            return opportunities.size
        }
        private fun getRemainingTime(deadlineMillis: Long): String {

            var difference = deadlineMillis - System.currentTimeMillis()

            if (difference <= 0) {
                return "Expired"
            }

            val days = TimeUnit.MILLISECONDS.toDays(difference)
            difference -= TimeUnit.DAYS.toMillis(days)

            val hours = TimeUnit.MILLISECONDS.toHours(difference)
            difference -= TimeUnit.HOURS.toMillis(hours)

            val minutes = TimeUnit.MILLISECONDS.toMinutes(difference)

            return when {
                days > 0 ->
                    "$days days left"

                hours > 0 ->
                    "$hours hours left"

                minutes > 0 ->
                    "$minutes minutes left"

                else ->
                    "Less than a minute"
            }
        }
        fun updateData(newList: List<Opportunity>) {
            opportunities.clear()
            opportunities.addAll(newList)
            notifyDataSetChanged()
        }
    }

}