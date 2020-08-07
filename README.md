# CloudinaryVS

Android library for adding subtitles to video files hosted in [Cloudinary media management platform](https://cloudinary.com/) using Cloudinary’s Text Layer capabilities (read more [here](https://cloudinary.com/documentation/video_transformation_reference)).

## Installation
Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
   repositories {
    ...
    maven { url 'https://jitpack.io' }
  
   }
}
```

 Add the dependency:
 
 ```gradle
dependencies {
    ...
    implementation 'com.github.xdma:CloudinaryVS:0.1.8'
}
  ```
  
## How do I use it?
The library takes three major values, ```publicid```,  ```cloudname```, ```jsonObject```- representing the subtitles
* **The Subtitles JSONObject structure example:** 
```kotlin
{
  "subtitles": [
    {
      "start-timing": "0:24.8",
      "end-timing": "0:27.2",
      "text": "Hey Sweetie! Sorry I got home so late..."
    },
    {
      "start-timing": "0:27.2",
      "end-timing": "0:30.6",
      "text": "but I had to pick something up after work."
    },
    {
      "start-timing": "0:30.6",
      "end-timing": "0:34.4",
      "text": "It's such a beautiful day outside, you should let some sun inside."
    },
   ...
  ]
}
```
* **Usage Example:**
```Kotlin
 val resultUrl = CloudinaryVS
            .get(publicId)
            .cloudName(cloudName)
            .addSubtitles(json)
            .textColor(textColor)
            .backgroundColor(bgColor)
            .setTextSize(textSize)
            .build()
```


_sample of susbtitles json is available at:_ ``` .addSubtitles(testData(context)) ```
