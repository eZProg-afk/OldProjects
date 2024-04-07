package com.example.minicoder.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.minicoder.models.File
import com.example.minicoder.models.Folder

@Database(entities = [File::class, Folder::class], version = 1, exportSchema = false)
abstract class ViewDatabase : RoomDatabase() {
    abstract fun getFileDao(): FileDao
    abstract fun getFolderDao(): FolderDao
}