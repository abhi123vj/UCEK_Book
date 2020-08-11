package com.abhi.ucekbook

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.ucekbook.models.RowModel
import com.abhi.ucekbook.models.Semester
import com.abhi.ucekbook.models.Subject
import com.abhi.ucekbook.models.Year
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView : RecyclerView

    lateinit var  layoutManager: RecyclerView.LayoutManager

    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>

    lateinit var progressLayout: RelativeLayout

    lateinit var progressBar: ProgressBar

    val restaurantinfolist = arrayListOf<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recylerHome)
        rows = mutableListOf()
        layoutManager = LinearLayoutManager(this)
        rowAdapter = RowAdapter(this, rows)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )


        recyclerView.adapter = rowAdapter
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
                    var stateList1 : MutableList<Subject> = mutableListOf()
                    for (document1 in document.data){
                        Log.d(TAG, "${document.id} => ${document1.key}")
                        val field = Model(document1.key,document1.value.toString())
                        stateList1.add(Subject(field, null))
                    }
                    rows.add(RowModel(RowModel.COUNTRY, Semester(document.id, stateList1)))

                }
                rowAdapter.notifyDataSetChanged()
                progressLayout.visibility = GONE
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
        Log.d(TAG, "Error getting documents: ")
    }
}