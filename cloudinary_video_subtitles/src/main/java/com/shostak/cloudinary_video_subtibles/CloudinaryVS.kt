package com.shostak.cloudinary_video_subtibles

import android.graphics.Color
import com.shostak.cloudinary_video_subtibles.utils.jsonIsValid
import com.shostak.cloudinary_video_subtibles.utils.timePosStrToSec
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

class CloudinaryVS(val publicId: String) {

    private var cloudName: String = ""
    internal var json: JSONObject? = null

    private val baseUrl =
        "https://res.cloudinary.com/%s/video/upload/%s%s"
    private val iteration = "l_text:arial_%s:%s,g_south,co_rgb:%s,y_50,so_%s,eo_%s,b_%s"
    private var defaultTextColor = "ffffff"
    private var defaultBgColor = "black"
    private var defaultTextSize = 20

    private fun generateVideoUrl(): String {
        var resultUrl = ""

        if (jsonIsValid()) {
            val subtitles = json!!.getJSONArray("subtitles")
            val sb = StringBuilder("")
            for (i in 0 until subtitles.length()) {
                val item = subtitles.getJSONObject(i)

                // escaping cloudinary special characters
                val text = if ((item.getString("text")).isBlank()) "-subtitle-" else item.getString(
                    "text"
                )
                    .replace("%", "%25")
                    .replace(",", "%2C")
                val start = timePosStrToSec(item.getString("start-timing"))
                val end = timePosStrToSec(item.getString("end-timing"))
                if (start == null || end == null)
                    continue

                sb.append(
                    URLEncoder.encode(
                        "l_text:arial_$defaultTextSize:$text,g_south,co_rgb:$defaultTextColor,y_50,so_$start,eo_$end,b_$defaultBgColor",
                        "UTF-8"
                    ).replace("+", "%20")
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

    fun textColor(color: Int): CloudinaryVS {
        defaultTextColor = Integer.toHexString(color).substring(2)
        return this
    }


    fun backgroundColor(color: Int): CloudinaryVS {
        defaultBgColor = "#${Integer.toHexString(color).substring(2)}"
        return this
    }

    fun setTextSize(size: Int): CloudinaryVS {
        defaultTextSize = size
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