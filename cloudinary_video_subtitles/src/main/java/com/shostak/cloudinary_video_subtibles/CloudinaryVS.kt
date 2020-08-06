package com.shostak.cloudinary_video_subtibles

import com.shostak.cloudinary_video_subtibles.utils.jsonIsValid
import com.shostak.cloudinary_video_subtibles.utils.timePosStrToSec
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

class CloudinaryVS(val publicId: String) {

    var cloudName: String = ""
    var json: JSONObject? = null

    private val baseUrl =
        "https://res.cloudinary.com/%s/video/upload/%s%s"

    //    val iteration = "l_text:arial_30:%s,g_south,y_50,so_%s,eo_%s/"
    val iteration = "l_text:arial_50:%s,g_south,co_rgb:ffffff,y_50,so_%s,eo_%s,b_black"

//    val temp =
//        "https://res.cloudinary.com/demo/video/upload/l_text:arial_30:Cool Video,g_south,co_rgb:ffffff,y_80,so_2,eo_5,b_black/dog.mp4"

    private fun generateVideoUrl(): String {
        var resultUrl = ""

        if (jsonIsValid()) {
            val subtitles = json!!.getJSONArray("subtitles")
            val sb = StringBuilder("")
            for (i in 0 until subtitles.length()) {
                val item = subtitles.getJSONObject(i)

                sb.append(
                    // escaping cloudinary special characters
                    URLEncoder.encode(
                        iteration.format(
                            if((item.getString("text")).isBlank()) "-subtitle-" else item.getString("text")
                                .replace("%", "%25")
                                .replace(",", "%2C"),

                            timePosStrToSec(item.getString("start-timing")),
                            timePosStrToSec(item.getString("end-timing"))
                        ), "UTF-8"
                    )
                        .replace("+", "%20")

                )
                sb.append("/")
            }

            resultUrl = baseUrl.format(
                this.cloudName,
                sb.toString(),

                this.publicId
            )

        } else {
            return baseUrl.format(this.cloudName, publicId, "")
        }

        return URL(resultUrl).toString()
    }

    fun cloudName(cloudName: String): CloudinaryVS {
        this.cloudName = cloudName
        return this
    }

    fun addSubtitles(json: JSONObject): CloudinaryVS {
        this.json = json
        return this
    }

    fun build(): String {
        if (cloudName.isBlank())
            throw IllegalArgumentException("cloudName field cannot be empty")

        if (publicId.isBlank())
            throw IllegalArgumentException("publicId field cannot be empty")

//        if (json == null)
//            throw IllegalArgumentException("No subtitles json provided, please use the addSubtitles method")


        return generateVideoUrl()
    }


    companion object {

        @JvmStatic
        fun get(publicId: String) =
            CloudinaryVS(publicId)
    }

}