package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.aplicacao.android.notas.R


class NovaNotaActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_nota)
        val editnotaView = findViewById<EditText>(R.id.edit_nota)
        var ss: Int = intent.getIntExtra("ID", 0)
        val button = findViewById<Button>(R.id.guardar)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editnotaView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val nota = editnotaView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_Nota, nota)
                replyIntent.putExtra(EXTRA_REPLY_ID, ss)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_Nota = "com.example.android.nota"
        const val EXTRA_REPLY_ID = "com.example.android.id"
    }
}
