package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.aplicacao.android.notas.R


class AddProblem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)
        val spinner: Spinner = findViewById(R.id.spinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val problemET = findViewById<EditText>(R.id.problemEditText)
        val button = findViewById<Button>(R.id.saveProblem)

        val long = intent.getStringExtra("LONG")
        val lat = intent.getStringExtra("LAT")
        val userId = intent.getIntExtra("USERID", 0)
        val id = intent.getIntExtra("ID", 0)
        val problem = intent.getStringExtra("PROBLEM")

        problemET.setText(problem)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(problemET.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_LONG, long)
                replyIntent.putExtra(EXTRA_REPLY_PROBLEM, problemET.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_LAT, lat)
                replyIntent.putExtra(EXTRA_REPLY_ID, id)
                replyIntent.putExtra(EXTRA_REPLY_USERID, userId)
                replyIntent.putExtra(EXTRA_REPLY_TYPE, spinner.getSelectedItem().toString())

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_LONG = "com.example.android.long"
        const val EXTRA_REPLY_PROBLEM = "com.example.android.problem"
        const val EXTRA_REPLY_ID = "com.example.android.id"
        const val EXTRA_REPLY_USERID = "com.example.android.userId"
        const val EXTRA_REPLY_LAT = "com.example.android.lat"
        const val EXTRA_REPLY_TYPE="com.example.android.type"
    }
}
