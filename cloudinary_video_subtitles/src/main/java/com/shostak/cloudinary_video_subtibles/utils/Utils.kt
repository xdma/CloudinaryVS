package com.shostak.cloudinary_video_subtibles.utils

import android.content.Context
import android.util.Log
import com.shostak.cloudinary_video_subtibles.CloudinaryVS
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.regex.Pattern

val TAG = "CVD"

/**
 *
 * @property time - timing string with pattern: #:##.#
 * converted to number of seconds
 */
internal fun CloudinaryVS.timePosStrToSec(time: String): Float? {
    try {
        val units = time.split(":")
        val minutes = units[0].toInt()
        val seconds = units[1].toFloat()
        return (60 * minutes + seconds)
    } catch (e: Exception) {
        Log.e(TAG, "Wrong timing field format, the correct pattern is: `#:##.#`, current subtitle wil be skipped")
    }

    return null
}

/**
 * Validating timing fields format in the json subtitle instance
 * @property item - json object instance from subtitles array
 * @property timing - string key of timing field
 *
 */
private fun CloudinaryVS.validateTimingField(item: JSONObject, timing: String): Boolean {
    val p = Pattern.compile(".*([01]?[0-9]|2[0-3]):[0-5][0-9].*")

    if (item.has(timing)) {
        val m = p.matcher(item.getString(timing))
        if (!m.matches()) {
            Log.e(TAG, "Wrong $timing field format, the correct pattern is: `#:##.#`")
        }
    } else {
        throw IllegalArgumentException("$timing field is missing")
    }

    return true
}

/**
 * Validating the entire json array fields before parsing it
 */
internal fun CloudinaryVS.jsonIsValid(): Boolean {

    if (json == null || json!!.optJSONArray("subtitles") != null && json!!.getJSONArray("subtitles")
            .length() == 0
    )
        return false

    if (json!!.optJSONArray("subtitles") == null) {
        Log.e(TAG, "Subtitles array is missing")
        return false
    }

    val subtitles = json!!.getJSONArray("subtitles")

    for (i in 0 until subtitles.length()) {
        val item = subtitles.getJSONObject(i)

        validateTimingField(item, "start-timing")
        validateTimingField(item, "end-timing")

        if (!item.has("text")) {
            Log.e(TAG, "text field is missing or empty")
            return false
        }
    }

    return true
}

/**
 * For testing purposes,
 * returns a json array of subtitles
 * stored in the assets folder.
 * {@link '/assets/test_data.json'
 */
fun testData(context: Context): JSONObject? {
    var json: String? = null
    json = try {
        val `is`: InputStream = context.getAssets().open("test_data.json")
        val size: Int = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return null
    }
    return JSONObject(json)
}


