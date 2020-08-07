package com.shostak.cloudinary_video_subtibles

import com.shostak.cloudinary_video_subtibles.utils.jsonIsValid
import com.shostak.cloudinary_video_subtibles.utils.timePosStrToSec
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder

/**
 * CloudinaryVS
 * Android library for adding subtitles to video files hosted in
 * Cloudinary media management platform using Cloudinary’s Text Layer capabilities.
 *
 * @author Dima Shostak
 */
/**
 * @constructor CloudinaryVS
 * @property publicId - the publicId value of video file hosted in Cloudinary
 */
class CloudinaryVS(private val publicId: String) {

    private var cloudName: String = ""
    internal var json: JSONObject? = null
    private val baseUrl = "https://res.cloudinary.com/%s/video/upload/%s%s"
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

                //empty string will be replaced to -subtitle-
                val text = if ((item.getString("text")).isBlank()) "-subtitle-" else item.getString(
                    "text"
                )
                    //escaping Cloudinary special characters
                    .replace("%", "%25")
                    .replace(",", "%2C")

                //converting timing strings to number of seconds: #.#
                val start = timePosStrToSec(item.getString("start-timing"))
                val end = timePosStrToSec(item.getString("end-timing"))

                //skip iteration if start or end empty or wrong formatted
                if (start == null || end == null)
                    continue

                //appending text overlay iteration
                sb.append(
                    URLEncoder.encode(
                        "l_text:arial_$defaultTextSize:$text,g_south,co_rgb:$defaultTextColor,y_50,so_$start,eo_$end,b_$defaultBgColor",
                        "UTF-8"
                    ).replace("+", "%20")
                )
                sb.append("/")
            }

            //formatting the string template of final result url
            resultUrl = baseUrl.format(
                this.cloudName,
                sb.toString(),
                this.publicId
            )

        } else {
            //returning the raw video without transformations
            return baseUrl.format(this.cloudName, publicId, "")
        }

        return URL(resultUrl).toString()
    }

    /**
     * Cloud Name
     *
     * @property cloudName the name of cloudinary account
     */
    fun cloudName(cloudName: String): CloudinaryVS {
        this.cloudName = cloudName
        return this
    }

    /**
     * addSubTitles
     *
     * @property json JSONObject with internal `subtitles` JSONArray'
     * @see com.shostak.cloudinary_video_subtibles.utils.testData method
     * which loads json file with default subtitle samples.
     */
    fun addSubtitles(json: JSONObject): CloudinaryVS {
        this.json = json
        return this
    }

    /**
     * Text Color (optional)
     *
     * @property color - Int color value
     * the default value if not specified:
     * @see defaultTextColor
     */
    fun textColor(color: Int): CloudinaryVS {
        defaultTextColor = Integer.toHexString(color).substring(2)
        return this
    }

    /**
     * Background Color (optional)
     *
     * @property color - Int color value
     * the default value if not specified:
     * @see backgroundColor
     */
    fun backgroundColor(color: Int): CloudinaryVS {
        defaultBgColor = "#${Integer.toHexString(color).substring(2)}"
        return this
    }

    /**
     * Text Size (optional)
     *
     * @property size - Int text size value
     * the default value if not specified:
     * @see defaultTextSize
     */
    fun setTextSize(size: Int): CloudinaryVS {
        defaultTextSize = size
        return this
    }

    fun build(): String {
        if (cloudName.isBlank())
            throw IllegalArgumentException("cloudName field cannot be empty")

        if (publicId.isBlank())
            throw IllegalArgumentException("publicId field cannot be empty")

        return generateVideoUrl()
    }


    companion object {

        @JvmStatic
        fun get(publicId: String) =
            CloudinaryVS(publicId)
    }

}