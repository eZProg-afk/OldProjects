package com.example.minicoder.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.minicoder.models.File

@Dao
interface FileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFile(file: File)

    @Delete
    fun deleteFile(file: File)

    @Update
    fun updateFile(file: File)

    @Query("SELECT * FROM files_table WHERE folderId == :folderId")
    fun getAllFilesInFolder(folderId: Int): LiveData<List<File>>
}