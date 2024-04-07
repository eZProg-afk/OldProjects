package com.example.minicoder.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files_table")
data class File(@PrimaryKey(autoGenerate = true) val id: Int, val folderId: Int, val name: String, val path: String)