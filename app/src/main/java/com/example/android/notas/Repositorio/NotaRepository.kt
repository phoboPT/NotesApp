package com.example.android.notas.Repositorio


import com.example.android.notas.entidade.Nota
import com.example.android.notas.Dao.NotaDao
import kotlinx.coroutines.flow.Flow


class NotaRepository(private val notaDao: NotaDao) {


    val allNotas: Flow<List<Nota>> = notaDao.getAlphabetizedNotas()




    @Suppress("RedundantSuspendModifier")

    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

    suspend fun deleteNota(id: Int){
        notaDao.deleteByNota(id)
    }

    suspend fun updateNota(nota: String, id: Int,title:String){
        notaDao.updateNota(nota, id,title)
    }

}
