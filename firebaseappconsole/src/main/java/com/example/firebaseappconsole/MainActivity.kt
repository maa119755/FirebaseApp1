package com.example.firebaseappconsole

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ValueEventListener {
    val database = Firebase.database
    val myRef = database.getReference("1")

    private var i = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { myRef.setValue(i++) }
        database.getReference("").addValueEventListener(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.hasChildren()) {
            val sb = StringBuilder()
            for (s in snapshot.children) {
                sb.appendLine(s.toString())
            }
            textViewSnapshot.text = sb.toString()
        } else {
            textViewSnapshot.text = snapshot.toString()
        }
    }

    override fun onCancelled(error: DatabaseError) {
    }
}