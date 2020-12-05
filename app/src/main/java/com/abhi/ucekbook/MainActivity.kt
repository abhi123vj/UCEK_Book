package com.abhi.ucekbook

import android.app.ActivityOptions
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhi.ucekboo.`interface`.OnItemClick
import com.abhi.ucekbook.models.RowModel
import com.abhi.ucekbook.models.Semester
import com.abhi.ucekbook.models.Subject
import com.abhi.ucekbook.models.Year
import androidx.transition.Transition
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Field
import java.util.*

class MainActivity : AppCompatActivity(), OnItemClick {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var rowAdapter: RowAdapter
    lateinit var rows: MutableList<RowModel>

    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recylerHome)

        rows = mutableListOf()
        layoutManager = LinearLayoutManager(this)
        rowAdapter = RowAdapter(this, rows, this)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )


        recyclerView.adapter = rowAdapter
        progressLayout = findViewById(R.id.progressLayout)

        progressBar = findViewById(R.id.progressBar)
        display("Sem")

        floating_action_button.setOnClickListener {
            val intent = Intent(this, DownloadActivity::class.java)
            val startView= floating_action_button
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                startView,
                "shared_element_container" // The transition name to be matched in Activity B.
            )
            startActivity(intent, options.toBundle())

        }
    }

    private fun display(path: String?) {
        progressLayout.visibility = VISIBLE
        val db = Firebase.firestore
        val TAG = "abhi123"
        if(!path.isNullOrEmpty())
        db.collection(path)
            .get()
            .addOnSuccessListener { result ->

                rows.clear()

                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val stateList1: MutableList<Subject> = mutableListOf()
                    for (document1 in document.data) {
                        Log.d(TAG, "${document.id} => ${document1.key}")
                        val field = Model(document1.key, document1.value.toString())
                        stateList1.add(Subject(field, null))
                    }
                    rows.add(RowModel(RowModel.COUNTRY, Semester(document.id, stateList1)))
                }
                rowAdapter.notifyDataSetChanged()
                progressLayout.visibility = GONE
            }
            .addOnFailureListener { exception ->
                progressLayout.visibility = GONE
                Toast.makeText(this@MainActivity, "Error 404!! Try again later", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    var stack = ArrayDeque<String>()
    var crnt = "Sem"
    var flag = 0
    override fun onClick(value: Model) {
        if (flag == 0) {
            if (crnt == "Sem")
                stack.push(crnt)
            crnt = value.FieldValue + "/" + value.FieldKey
            stack.push(crnt)
            display(stack.peek())
            Log.d("TAG", "onlick $value ")
            if(value.FieldKey=="Question Paper"|| value.FieldKey=="Notes"||value.FieldKey=="Syllabus")
                flag=1

        }
        else{
            Toast.makeText(this@MainActivity, value.FieldKey, Toast.LENGTH_SHORT).show()

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(value.FieldValue), "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            val newIntent = Intent.createChooser(intent, "Open File")
            try {
                startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, " this is errorcatched ", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    override fun onBackPressed() {
        if(flag==1)
            flag=0
        stack.pop()
        if (stack.isNotEmpty())
            display(stack.peek())
        else
            super.onBackPressed()

        Log.d("TAG", "onback $stack ")

    }

}