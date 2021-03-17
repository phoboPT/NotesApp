package com.example.android.notas.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.android.notas.entidade.Nota
import com.example.android.notas.Dao.NotaDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
@Database(entities = [Nota::class], version = 8)
abstract class NotaDatabase : RoomDatabase() {

    abstract fun NotaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: NotaDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): NotaDatabase {

            return INSTANCE
                    ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotaDatabase::class.java,
                    "nota_database"
                )
                    //.fallbackToDestructiveMigration()
                    //.addCallback(NotaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class NotaDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.NotaDao())
                    }
                }
            }
        }
        suspend fun populateDatabase(notaDao: NotaDao) {

            notaDao.deleteAll()
            /*
            var nota = Nota("Hello")
            notaDao.insert(nota)
            nota = Nota("World!")
            notaDao.insert(nota)*/
        }
    }
}
