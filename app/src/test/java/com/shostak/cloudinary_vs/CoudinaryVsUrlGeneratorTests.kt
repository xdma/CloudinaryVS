package com.shostak.cloudinary_vs

import com.shostak.cloudinary_video_subtibles.CloudinaryVS
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests of all major CloudinatyVS library features
 *
 *
 */
class CoudinaryVsUrlGeneratorTests {

    val jsonSample =
        "{\"subtitles\":[{\"start-timing\":\"00:24.8\",\"end-timing\":\"00:27.2\",\"text\":\"Hey Sweetie! Sorry I got home so late...\"},{\"start-timing\":\"00:27.2\",\"end-timing\":\"00:30.6\",\"text\":\"but I had to pick something up after work.\"},{\"start-timing\":\"0:30.6\",\"end-timing\":\"0:34.4\",\"text\":\"It's such a beautiful day outside, you should let some sun inside.\"},{\"start-timing\":\"0:39.4\",\"end-timing\":\"0:42.5\",\"text\":\"Why don't you stop playing and open your present?\"},{\"start-timing\":\"0:43.1\",\"end-timing\":\"0:44.8\",\"text\":\"For me?\"},{\"start-timing\":\"0:46.2\",\"end-timing\":\"0:49.6\",\"text\":\"Yes Sir, I don't know why you didn't get the papers.\"},{\"start-timing\":\"0:55.7\",\"end-timing\":\"0:57.7\",\"text\":\"Woah! Cool!\"},{\"start-timing\":\"1:08.1\",\"end-timing\":\"1:10.4\",\"text\":\"She's gotta be kidding me!\"},{\"start-timing\":\"1:17.5\",\"end-timing\":\"1:19.3\",\"text\":\"GET LOST!?\"},{\"start-timing\":\"3:04.7\",\"end-timing\":\"3:07.3\",\"text\":\"MUM! WE'LL BE OUTSIDE!\"}]}"

    @Test
    fun testJsonToUrlConversion() {
        val expected =
            "https://res.cloudinary.com/candidate-evaluation/video/upload/l_text%3Aarial_20%3AHey%20Sweetie%21%20Sorry%20I%20got%20home%20so%20late...%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_24.8%2Ceo_27.2%2Cb_black/l_text%3Aarial_20%3Abut%20I%20had%20to%20pick%20something%20up%20after%20work.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_27.2%2Ceo_30.6%2Cb_black/l_text%3Aarial_20%3AIt%27s%20such%20a%20beautiful%20day%20outside%252C%20you%20should%20let%20some%20sun%20inside.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_30.6%2Ceo_34.4%2Cb_black/l_text%3Aarial_20%3AWhy%20don%27t%20you%20stop%20playing%20and%20open%20your%20present%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_39.4%2Ceo_42.5%2Cb_black/l_text%3Aarial_20%3AFor%20me%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_43.1%2Ceo_44.8%2Cb_black/l_text%3Aarial_20%3AYes%20Sir%252C%20I%20don%27t%20know%20why%20you%20didn%27t%20get%20the%20papers.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_46.2%2Ceo_49.6%2Cb_black/l_text%3Aarial_20%3AWoah%21%20Cool%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_55.7%2Ceo_57.7%2Cb_black/l_text%3Aarial_20%3AShe%27s%20gotta%20be%20kidding%20me%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_68.1%2Ceo_70.4%2Cb_black/l_text%3Aarial_20%3AGET%20LOST%21%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_77.5%2Ceo_79.3%2Cb_black/l_text%3Aarial_20%3AMUM%21%20WE%27LL%20BE%20OUTSIDE%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_184.7%2Ceo_187.3%2Cb_black/The_Present.mp4"

        val resultUrl = CloudinaryVS
            .get("The_Present.mp4")
            .cloudName("candidate-evaluation")
            .addSubtitles(JSONObject(jsonSample))
            .build()

        assertEquals(
            "Generation url from sample json failed",
            expected,
            resultUrl
        )
    }

    @Test
    fun testSetTextSize() {
        val expected =
            "https://res.cloudinary.com/candidate-evaluation/video/upload/l_text%3Aarial_130%3AHey%20Sweetie%21%20Sorry%20I%20got%20home%20so%20late...%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_24.8%2Ceo_27.2%2Cb_black/l_text%3Aarial_130%3Abut%20I%20had%20to%20pick%20something%20up%20after%20work.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_27.2%2Ceo_30.6%2Cb_black/l_text%3Aarial_130%3AIt%27s%20such%20a%20beautiful%20day%20outside%252C%20you%20should%20let%20some%20sun%20inside.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_30.6%2Ceo_34.4%2Cb_black/l_text%3Aarial_130%3AWhy%20don%27t%20you%20stop%20playing%20and%20open%20your%20present%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_39.4%2Ceo_42.5%2Cb_black/l_text%3Aarial_130%3AFor%20me%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_43.1%2Ceo_44.8%2Cb_black/l_text%3Aarial_130%3AYes%20Sir%252C%20I%20don%27t%20know%20why%20you%20didn%27t%20get%20the%20papers.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_46.2%2Ceo_49.6%2Cb_black/l_text%3Aarial_130%3AWoah%21%20Cool%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_55.7%2Ceo_57.7%2Cb_black/l_text%3Aarial_130%3AShe%27s%20gotta%20be%20kidding%20me%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_68.1%2Ceo_70.4%2Cb_black/l_text%3Aarial_130%3AGET%20LOST%21%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_77.5%2Ceo_79.3%2Cb_black/l_text%3Aarial_130%3AMUM%21%20WE%27LL%20BE%20OUTSIDE%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_184.7%2Ceo_187.3%2Cb_black/The_Present.mp4"

        val resultUrl = CloudinaryVS
            .get("The_Present.mp4")
            .cloudName("candidate-evaluation")
            .addSubtitles(JSONObject(jsonSample))
            .setTextSize(130)
            .build()

        assertEquals(
            "setTextSize() failed",
            expected,
            resultUrl
        )
    }


    @Test
    fun testSetBackgroundColor() {
        val expected =
            "https://res.cloudinary.com/candidate-evaluation/video/upload/l_text%3Aarial_20%3AHey%20Sweetie%21%20Sorry%20I%20got%20home%20so%20late...%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_24.8%2Ceo_27.2%2Cb_%23000000/l_text%3Aarial_20%3Abut%20I%20had%20to%20pick%20something%20up%20after%20work.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_27.2%2Ceo_30.6%2Cb_%23000000/l_text%3Aarial_20%3AIt%27s%20such%20a%20beautiful%20day%20outside%252C%20you%20should%20let%20some%20sun%20inside.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_30.6%2Ceo_34.4%2Cb_%23000000/l_text%3Aarial_20%3AWhy%20don%27t%20you%20stop%20playing%20and%20open%20your%20present%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_39.4%2Ceo_42.5%2Cb_%23000000/l_text%3Aarial_20%3AFor%20me%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_43.1%2Ceo_44.8%2Cb_%23000000/l_text%3Aarial_20%3AYes%20Sir%252C%20I%20don%27t%20know%20why%20you%20didn%27t%20get%20the%20papers.%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_46.2%2Ceo_49.6%2Cb_%23000000/l_text%3Aarial_20%3AWoah%21%20Cool%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_55.7%2Ceo_57.7%2Cb_%23000000/l_text%3Aarial_20%3AShe%27s%20gotta%20be%20kidding%20me%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_68.1%2Ceo_70.4%2Cb_%23000000/l_text%3Aarial_20%3AGET%20LOST%21%3F%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_77.5%2Ceo_79.3%2Cb_%23000000/l_text%3Aarial_20%3AMUM%21%20WE%27LL%20BE%20OUTSIDE%21%2Cg_south%2Cco_rgb%3Affffff%2Cy_50%2Cso_184.7%2Ceo_187.3%2Cb_%23000000/The_Present.mp4"

        val resultUrl = CloudinaryVS
            .get("The_Present.mp4")
            .cloudName("candidate-evaluation")
            .addSubtitles(JSONObject(jsonSample))
            .backgroundColor(-16777216)
            .build()

        assertEquals(
            "backgroundColor() failed",
            expected,
            resultUrl
        )
    }


    @Test
    fun testSetTestColor() {
        val expected =
            "https://res.cloudinary.com/candidate-evaluation/video/upload/l_text%3Aarial_20%3AHey%20Sweetie%21%20Sorry%20I%20got%20home%20so%20late...%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_24.8%2Ceo_27.2%2Cb_black/l_text%3Aarial_20%3Abut%20I%20had%20to%20pick%20something%20up%20after%20work.%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_27.2%2Ceo_30.6%2Cb_black/l_text%3Aarial_20%3AIt%27s%20such%20a%20beautiful%20day%20outside%252C%20you%20should%20let%20some%20sun%20inside.%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_30.6%2Ceo_34.4%2Cb_black/l_text%3Aarial_20%3AWhy%20don%27t%20you%20stop%20playing%20and%20open%20your%20present%3F%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_39.4%2Ceo_42.5%2Cb_black/l_text%3Aarial_20%3AFor%20me%3F%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_43.1%2Ceo_44.8%2Cb_black/l_text%3Aarial_20%3AYes%20Sir%252C%20I%20don%27t%20know%20why%20you%20didn%27t%20get%20the%20papers.%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_46.2%2Ceo_49.6%2Cb_black/l_text%3Aarial_20%3AWoah%21%20Cool%21%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_55.7%2Ceo_57.7%2Cb_black/l_text%3Aarial_20%3AShe%27s%20gotta%20be%20kidding%20me%21%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_68.1%2Ceo_70.4%2Cb_black/l_text%3Aarial_20%3AGET%20LOST%21%3F%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_77.5%2Ceo_79.3%2Cb_black/l_text%3Aarial_20%3AMUM%21%20WE%27LL%20BE%20OUTSIDE%21%2Cg_south%2Cco_rgb%3A000000%2Cy_50%2Cso_184.7%2Ceo_187.3%2Cb_black/The_Present.mp4"

        val resultUrl = CloudinaryVS
            .get("The_Present.mp4")
            .cloudName("candidate-evaluation")
            .addSubtitles(JSONObject(jsonSample))
            .textColor(-16777216)
            .build()

        assertEquals(
            "textColor() failed",
            expected,
            resultUrl
        )
    }


}