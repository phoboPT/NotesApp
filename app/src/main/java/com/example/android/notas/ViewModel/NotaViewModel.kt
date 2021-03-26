package com.example.android.notas.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.android.notas.Repositorio.NotaRepository
import com.example.android.notas.entidade.Nota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotaViewModel(private val repository: NotaRepository) : ViewModel() {

    val allNotas: LiveData<List<Nota>> = repository.allNotas.asLiveData()


    fun insert(nota: Nota) = viewModelScope.launch {
        repository.insert(nota)
    }

    fun deleteByNota(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("IDNota", id.toString())
        repository.deleteNota(id)
    }

    fun updateNota(id: Int, nota: String,title:String) = viewModelScope.launch {
        repository.updateNota(nota,id,title)
    }
}

class NotaViewModelFactory(private val repository: NotaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
