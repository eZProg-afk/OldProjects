package com.example.minicoder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minicoder.models.Folder

interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFolder(folder: Folder)

    @Delete
    fun deleteFolder(folder: Folder)

    @Update
    fun updateFolder(folder: Folder)

    @Query("SELECT * FROM folders_table")
    fun getAllFolders(): LiveData<List<Folder>>
}