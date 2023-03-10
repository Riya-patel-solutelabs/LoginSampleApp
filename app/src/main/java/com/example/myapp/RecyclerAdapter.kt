package com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        var itemTitle: TextView
        var itemSubtitle: TextView

        init {
            itemImage=itemView.findViewById(R.id.image_view)
            itemTitle=itemView.findViewById(R.id.textView_title)
            itemSubtitle=itemView.findViewById(R.id.textView_subtitle)
        }
    }

    private val title= arrayOf("UI Component- Text","UI Component- Button", "UI Component- Widgets","UI Component- Widgets","UI Component- Layouts",
    "UI Component-Layouts","UI Component- Containers","UI Component- Containers","UI Component- Others","UI Component- Others")

    private val subTitle= arrayOf("TextView","Radiobutton","Checkbox","Rating Bar","SearchView",
        "Constaint Layout","Linear Layout","Spinner","RecyclerView","DatePicker","Snackbar")

    private val image= intArrayOf(R.drawable.android,R.drawable.android,R.drawable.android,
        R.drawable.android,R.drawable.android,R.drawable.android,R.drawable.android,
        R.drawable.android,R.drawable.android,R.drawable.android)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v= LayoutInflater.from(parent.context).inflate(R.layout.layout_cardview,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text= title[position]
        holder.itemSubtitle.text= subTitle[position]
        holder.itemImage.setImageResource(image[position])
    }

    override fun getItemCount(): Int {
       return  title.size
    }




}