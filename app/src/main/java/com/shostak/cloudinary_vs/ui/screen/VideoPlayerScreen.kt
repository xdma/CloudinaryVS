package com.shostak.cloudinary_vs.ui.screen

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.shostak.cloudinary_vs.AppDatabase
import com.shostak.cloudinary_vs.R
import com.shostak.cloudinary_vs.model.SubTitle
import com.shostak.cloudinary_vs.ui.ColorPicker
import com.shostak.cloudinary_vs.utils.*
import com.shostak.cloudinary_vs.viewmodel.SubTitleViewModel
import kotlinx.android.synthetic.main.fragment_video_player_screen.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class VideoPlayerScreen : Fragment(), View.OnClickListener, View.OnTouchListener, TextWatcher {

    private lateinit var viewModel: SubTitleViewModel
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private lateinit var addButtonSpring: SpringAnimation
    private lateinit var addButtonRotationSpring: SpringAnimation
    private lateinit var recyclerViewSpring: SpringAnimation
    private val subtitlesAdapter = SubtitlesAdapter()
    private var player: SimpleExoPlayer? = null
    private var videoReload = false
    private var textColor: Int = 0
    private var bgColor: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_player_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initUiAndListeners()
        initRecyclerview()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(
            SubTitleViewModel::
            class.java
        )

        viewModel.generatedUrl.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotBlank())
                reInitializePlayer(it)
        })
    }

    private fun initUiAndListeners() {
        VideoViewCollapseExpandAnimation.collapse(videoView)
        textColor = ContextCompat.getColor(requireContext(), R.color.colorAccent)
        bgColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)

        publicIdValue.addTextChangedListener(this)
        loadButton.setOnClickListener(this)
        txtColorButton.setOnClickListener(this)
        bgColorButton.setOnClickListener(this)
        addButton.setOnTouchListener(this)

        addButtonSpring = createSpringAnimation(
            addButton,
            DynamicAnimation.SCALE_X,
            600F,
            0.5f
        ).apply {
            addUpdateListener { _, value, _ ->
                addButton?.scaleY = value
                addButton?.rotation = (1 - value) * 360
            }
        }

        addButtonRotationSpring = createSpringAnimation(
            addButton,
            DynamicAnimation.ROTATION,
            33F,
            0.3f
        ).apply {
            addUpdateListener { _, value, _ ->
                addButton?.translationY = -(value * 2)
            }
        }

        recyclerViewSpring = createSpringAnimation(
            recyclerView,
            DynamicAnimation.TRANSLATION_Y,
            33F,
            0.8f
        )
    }

    private fun initRecyclerview() {
        hideSubtitlesList()
        smoothScroller = object : LinearSmoothScroller(requireContext()) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

        val llm = LinearLayoutManager(context)
        llm.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager = llm
        subtitlesAdapter.setHasStableIds(true)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = subtitlesAdapter

        subtitlesAdapter.onTitleChanged = { id, text, start, end ->
            viewModel.changeTitle(id, text, start, end)
        }

        subtitlesAdapter.onDeleteClicked = { subtitle ->
            viewModel.deleteItem(subtitle.id)
        }

        val anim: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.inputs_list_animation
            )

        recyclerView.layoutAnimation = anim
    }

    private fun reInitializePlayer(videoUrl: String) {
        if (!videoReload)
            return

        videoReload = false
        player = SimpleExoPlayer.Builder(requireContext()).build()
        videoView.player = player
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(requireContext(), "cvs")
        )
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videoUrl))
        player?.prepare(videoSource)
    }

    private fun showSubtitlesList() {
        hideKeyboard(loadButton)
        videoReload = true
        recyclerView.tag = 0
        player?.release()
        viewModel.setPublicId(publicIdValue.text.toString())
        viewModel.setCloudName(cloudNameValue.text.toString())

        val db = AppDatabase.getInstance(requireContext())
        db.subTitleDao().getAll(viewModel.publicId).observe(viewLifecycleOwner, Observer {

            if (viewModel.publicId.isEmpty() || it.isNotEmpty() && it[0].public_id != viewModel.publicId)
                return@Observer

            /** recyclerView.tag used here for storing the state of the view,
            if tag is 0 layoutanimation, animation drawable of action button
            and recyclerView spring will play the animations */

            if (recyclerView.tag == 0) {
                recyclerView.visibility = View.VISIBLE
                addButton.visibility = View.VISIBLE
                recyclerView.tag = 1
                recyclerView.scheduleLayoutAnimation()
                recyclerViewSpring.setStartValue(-recyclerView.height.toFloat())
                    .animateToFinalPosition(0F)

                (addButton.drawable as AnimatedVectorDrawable).start()
                addButtonRotationSpring.setStartValue(-90F).animateToFinalPosition(0F)
            }

            // submitting the list of subtitle objects to the recyclerView ListAdapter, sorted dy id
            subtitlesAdapter.submitList(it.sortedWith(compareBy({ it.id })))

            /** generating the Cloudinary video url
             * on every items list change,
            but not reloading the video */

            viewModel.createCloudinaryUrl(
                subtitlesList = it,
                textColor = textColor,
                bgColor = bgColor,
                textSize = 80
            )
        })

        VideoViewCollapseExpandAnimation.expand(videoView)
    }

    // hiding the recyclerView list and actionButton
    private fun hideSubtitlesList() {
        if (player?.isPlaying == true)
            player?.stop()

        recyclerView.post {
            recyclerView.translationY = -recyclerView.height.toFloat()
            recyclerView.tag = 0
            recyclerView.visibility = View.GONE
        }

        addButton.visibility = View.GONE
        VideoViewCollapseExpandAnimation.collapse(videoView)
    }

    override fun onPause() {
        super.onPause()

        //releasing the video player to free up limited resources such as video decoders.
        player?.release()
    }

    // ui listeners
    override fun onClick(v: View?) {
        when (v) {
            loadButton -> {
                showSubtitlesList()
            }

            //Color picker fragment dialog
            txtColorButton, bgColorButton -> {
                val cp = ColorPicker()
                cp.show(childFragmentManager, "cp")
                cp.onSetColorListner = object : ColorPicker.OnColorSetListener {
                    override fun onColorSet(color: Int) {
                        when (v) {
                            txtColorButton -> textColor = color
                            bgColorButton -> bgColor = color
                        }

                        txtColorButton.setBackgroundColor(bgColor)
                        bgColorButton.setBackgroundColor(bgColor)
                        txtColorButton.setTextColor(textColor)
                        bgColorButton.setTextColor(textColor)
                    }
                }
            }
        }
    }

    //actionButton onTouch listener
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                addButtonSpring.setStartValue(addButton.scaleX).animateToFinalPosition(.8F)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                addButtonSpring.setStartValue(addButton.scaleX).animateToFinalPosition(1F)
                val timingStrings =
                    (videoView.player?.contentPosition ?: 0).convertPositionToTimingString()

                //add new empty subtitle item with current video position
                viewModel.addItem(
                    SubTitle(
                        text = "",
                        start_timing = timingStrings[0],
                        end_timing = timingStrings[1],
                        public_id = viewModel.publicId
                    )
                )

                subtitlesAdapter.requestFocusOnLastItem = true
                smoothScroller.targetPosition = subtitlesAdapter.itemCount + 1

                GlobalScope.launch(Dispatchers.Main) {
                    delay(500)
                    recyclerView?.layoutManager?.startSmoothScroll(smoothScroller)
                }

            }
        }
        return true
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable?) {
        if (viewModel.publicId != s.toString()) {
            hideSubtitlesList()
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            VideoPlayerScreen()
    }
}