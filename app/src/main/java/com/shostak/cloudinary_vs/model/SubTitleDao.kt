package com.shostak.cloudinary_vs.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubTitleDao {

    @Query("SELECT * FROM SubTitle WHERE publicid = :id")
    fun getItem(id: Int): SubTitle

    @Query("SELECT * FROM SubTitle WHERE publicid = :publicId")
    fun getAll(publicId: String): LiveData<List<SubTitle>>

    @Insert
    fun insertAll(innApps: List<SubTitle>)

    @Insert
    fun insert(item: SubTitle)

    @Update
    fun updateAll(vararg innApps: SubTitle)

    @Query("UPDATE SubTitle SET text = :text, start_timing = :start, end_timing=:end WHERE id == :id")
    fun updateItem(id: Int, text: String, start: String, end: String): Int


    @Query("DELETE FROM SubTitle WHERE id == :id")
    fun delete(id: Int)

}