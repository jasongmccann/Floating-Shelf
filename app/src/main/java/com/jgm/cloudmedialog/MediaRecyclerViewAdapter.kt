package com.jgm.cloudmedialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MediaRecyclerViewAdapter(private val values: MutableList<UserData.Media>?) :
    RecyclerView.Adapter<MediaRecyclerViewAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.content_media, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val item = values?.get(position)
        holder.nameView.text = item?.name
        holder.platformView.text = item?.platform
        holder.yearView.text = item?.year
        holder.formatView.text = item?.format
        holder.ratingView.text = item?.rating
        holder.playstatusView.text = item?.playstatus
        if (item?.image != null)
        {
            holder.imageView.setImageBitmap(item.image)
        }
    }
    override fun getItemCount() = values?.size ?: 0
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        val imageView: ImageView = view.findViewById(R.id.image)
        val nameView: TextView = view.findViewById(R.id.name)
        val platformView: TextView = view.findViewById(R.id.platform)
        val yearView: TextView = view.findViewById(R.id.year)
        val formatView: TextView = view.findViewById(R.id.format)
        val ratingView: TextView = view.findViewById(R.id.rating)
        val playstatusView: TextView = view.findViewById(R.id.playstatus)
    }
}