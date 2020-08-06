package com.shostak.cloudinary_vs.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shostak.cloudinary_video_subtibles.CloudinaryVS
import com.shostak.cloudinary_vs.AppDatabase
import com.shostak.cloudinary_vs.CloudinaryVSApp
import com.shostak.cloudinary_vs.model.SubTitle
import com.shostak.cloudinary_vs.model.SubTitleDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class SubTitleRepository(context: Context) {

    private var subtitleDao: SubTitleDao
    var publicId: String = ""
    var cloudName: String = ""
    val generatedUrl: MutableLiveData<String> = MutableLiveData()

    init {
        val db: AppDatabase = AppDatabase.getInstance(context)
        subtitleDao = db.subTitleDao()
    }


    fun loadSubtitles() {
        GlobalScope.launch {

//            subtitlesList.postValue(list)
        }
    }

    fun deleteItem(id: Int) {
        GlobalScope.launch {
            subtitleDao.delete(id)
        }
    }

    fun changeText(id: Int, text: String, start: String, end: String) {
        GlobalScope.launch {
            subtitleDao.updateItem(id, text, start, end)
        }
    }


    fun addItem(context: Context, item: SubTitle) {
        GlobalScope.launch {
            subtitleDao.insert(item)
        }
    }

    fun createCloudinaryUrl(subtitlesList: List<SubTitle>?) {
        val subtitlesJsonArray = JSONArray()
        subtitlesList?.forEach {
            subtitlesJsonArray.put(JSONObject().apply {
                put("text", it.text)
                put("start-timing", it.start_timing)
                put("end-timing", it.end_timing)
            })
        }

        val json = JSONObject().apply {
            put("subtitles", subtitlesJsonArray)
        }

        val resultUrl = CloudinaryVS
            .get(publicId)
            .cloudName(cloudName)
            .addSubtitles(json)
            .build()

//        Log.i("Shostak", resultUrl)

//        if (generatedUrl.value != resultUrl)
            generatedUrl.postValue(resultUrl)
    }


}