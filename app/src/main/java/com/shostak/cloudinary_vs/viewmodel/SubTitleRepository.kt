package com.shostak.cloudinary_vs.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.shostak.cloudinary_video_subtibles.CloudinaryVS
import com.shostak.cloudinary_vs.AppDatabase
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


    fun addItem(item: SubTitle) {
        GlobalScope.launch {
            subtitleDao.insert(item)
        }
    }

    fun createCloudinaryUrl(
        subtitlesList: List<SubTitle>?,
        textColor: Int,
        bgColor: Int,
        textSize: Int
    ) {
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
            .textColor(textColor)
            .backgroundColor(bgColor)
            .setTextSize(textSize)
            .build()

        generatedUrl.postValue(resultUrl)
    }


}