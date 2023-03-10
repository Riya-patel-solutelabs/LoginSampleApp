package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerActivity : AppCompatActivity() {
    private  var layoutManager: RecyclerView.LayoutManager?=null
    private  var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>?=null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        recyclerView= findViewById(R.id.recyclerView)

        layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager= layoutManager

        adapter=RecyclerAdapter()
        recyclerView.adapter= adapter
    }
}