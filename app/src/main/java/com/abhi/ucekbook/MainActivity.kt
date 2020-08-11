package com.abhi.ucekbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var recylerHome : RecyclerView

    lateinit var  layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter : Adapter

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    val restaurantinfolist = arrayListOf<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recylerHome = findViewById(R.id.recylerHome)

        layoutManager = LinearLayoutManager(this)

        progressLayout = findViewById(R.id.progressLayout)

        progressBar = findViewById(R.id.progressBar)

        progressLayout.visibility = VISIBLE
        val db = Firebase.firestore
        val TAG = "abhi123"
        db.collection("Sem")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")

                    val abc  = Model( document.id,document.data.toString())
                    restaurantinfolist.add(abc)
                }

                recyclerAdapter = Adapter(this, restaurantinfolist)

                recylerHome.adapter = recyclerAdapter

                recylerHome.layoutManager = layoutManager
                progressLayout.visibility = GONE
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        Log.d(TAG, "Error getting documents: ")
    }
}