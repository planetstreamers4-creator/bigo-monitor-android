package com.example.bigostreammonitor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class StreamerAdapter(private val onClick: (Streamer) -> Unit) :
    ListAdapter<Streamer, StreamerAdapter.ViewHolder>(StreamerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_streamer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val streamer = getItem(position)
        holder.bind(streamer)
        holder.itemView.setOnClickListener { onClick(streamer) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName: TextView = view.findViewById(R.id.tvName)
        private val tvId: TextView = view.findViewById(R.id.tvId)
        private val tvStatus: TextView = view.findViewById(R.id.tvStatus)

        fun bind(streamer: Streamer) {
            tvName.text = streamer.name ?: "Sem nome"
            tvId.text = "@${streamer.bigoId}"
            tvStatus.text = if (streamer.isMonitoring) "Monitorando" else "Parado"
            tvStatus.setTextColor(if (streamer.isMonitoring) 0xFF4CAF50.toInt() else 0xFFFF5722.toInt())
        }
    }

    class StreamerDiffCallback : DiffUtil.ItemCallback<Streamer>() {
        override fun areItemsTheSame(oldItem: Streamer, newItem: Streamer) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Streamer, newItem: Streamer) = oldItem == newItem
    }
}
