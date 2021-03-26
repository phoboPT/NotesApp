package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aplicacao.android.notas.R
import com.example.android.notas.ViewModel.NotaViewModel


class NovaNotaActivity : AppCompatActivity() {
    private lateinit var notasViewModel: NotaViewModel
    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_nota)
        val editnotaView = findViewById<EditText>(R.id.edit_nota)
        val titleView = findViewById<EditText>(R.id.titleTB)


        var editNota = intent.getStringExtra("NOTE")
        var editTitle = intent.getStringExtra("TITLE")
        editnotaView.setText(editNota)
        titleView.setText(editTitle)
        var ss: Int = intent.getIntExtra("ID", 0)
        val button = findViewById<Button>(R.id.guardar)
       // notasViewModel = ViewModelProvider(this).get(NotaViewModel::class.java)

     //   Log.d("ITEM",nota.toString())
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editnotaView.text) || TextUtils.isEmpty(titleView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val nota = editnotaView.text.toString()
                val titleView = titleView.text.toString()

                replyIntent.putExtra(EXTRA_REPLY_Nota, nota)
                replyIntent.putExtra(EXTRA_REPLY_TITLE, titleView)

                replyIntent.putExtra(EXTRA_REPLY_ID, ss)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_Nota = "com.example.android.nota"
        const val EXTRA_REPLY_ID = "com.example.android.id"
        const val EXTRA_REPLY_TITLE="com.example.android.title"
    }
}
