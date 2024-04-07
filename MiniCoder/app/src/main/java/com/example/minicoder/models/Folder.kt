package com.example.minicoder.models

import androidx.room.Entity

@Entity(tableName = "folders_table")
data class Folder(val id: Int, val name: String, val path: String)