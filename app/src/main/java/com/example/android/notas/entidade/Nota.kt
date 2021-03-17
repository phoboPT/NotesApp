package com.example.android.notas.entidade

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nota_table")

class Nota(
        // Int? = null so when creating instance id has not to be passed as argument
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        @ColumnInfo(name = "nota") val nota: String,
)
