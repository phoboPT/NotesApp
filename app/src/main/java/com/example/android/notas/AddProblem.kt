package com.example.android.notas

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.aplicacao.android.notas.R

class AddProblem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)

        val title = findViewById<EditText>(R.id.titleProblemEditText)
        val problem = findViewById<EditText>(R.id.problemEditText)
        val button = findViewById<Button>(R.id.saveProblem)

        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(title.text) || TextUtils.isEmpty(problem.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_TITLE, title.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_PROBLEM, problem.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.android.title"
        const val EXTRA_REPLY_PROBLEM = "com.example.android.problem"
    }
}