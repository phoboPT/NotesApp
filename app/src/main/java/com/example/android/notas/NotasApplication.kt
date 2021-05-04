
package com.example.android.notas

import android.app.Application
import com.example.android.notas.Repositorio.NotaRepository
import com.example.android.notas.bd.NotaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NotasApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { NotaDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NotaRepository(database.NotaDao()) }


}
