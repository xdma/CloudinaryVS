package com.shostak.cloudinary_vs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.shostak.cloudinary_vs.CloudinaryVSApp
import com.shostak.cloudinary_vs.model.SubTitle


class SubTitleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SubTitleRepository(getApplication<CloudinaryVSApp>())

    val generatedUrl
        get() = repository.generatedUrl

    val publicId
        get() = repository.publicId

    fun setPublicId(publicId: String) {
        repository.publicId = publicId
    }

    fun setCloudName(cloudName: String) {
        repository.cloudName = cloudName
    }

    fun deleteItem(id: Int) {
        repository.deleteItem(id)
    }

    fun changeTitle(id: Int, text: String, start: String, end: String) {
        repository.changeText(id, text, start, end)
    }

    fun addItem(item: SubTitle) {
        repository.addItem(item)
    }

    fun createCloudinaryUrl(
        subtitlesList: List<SubTitle>?,
        textColor: Int,
        bgColor: Int,
        textSize: Int = 20
    ) {

        repository.createCloudinaryUrl(subtitlesList, textColor, bgColor, textSize)
    }

}