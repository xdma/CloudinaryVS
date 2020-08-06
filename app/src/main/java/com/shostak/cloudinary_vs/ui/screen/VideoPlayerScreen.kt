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
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.shostak.cloudinary_vs.AppDatabase
import com.shostak.cloudinary_vs.R
import com.shostak.cloudinary_vs.model.SubTitle
import com.shostak.cloudinary_vs.utils.*
import com.shostak.cloudinary_vs.viewmodel.SubTitleViewModel
import kotlinx.android.synthetic.main.fragment_video_player_screen.*


class VideoPlayerScreen : Fragment(), View.OnClickListener, TextWatcher {

    private lateinit var viewModel: SubTitleViewModel
    private lateinit var smoothScroller: RecyclerView.SmoothScroller
    private val subtitlesAdapter = SubtitlesAdapter()
    private lateinit var player: SimpleExoPlayer
    private var videoReload = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_player_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        player = SimpleExoPlayer.Builder(requireContext()).build()
        videoView.player = player

        initRecyclerview()
        initUi()
        loadButton.setOnClickListener(this)
        initViewModel()
    }


    override fun onClick(v: View?) {
        when (v) {
            loadButton ->
                showSubtitlesList()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUi() {
        VideoViewCollapseExpandAnimation.collapse(videoView)

        publicIdValue.addTextChangedListener(this)
        val addButtonSpring = createSpringAnimation(
            addButton,
            DynamicAnimation.SCALE_X,
            600F,
            0.5f
        ).apply {
            addUpdateListener { _, value, _ ->
                addButton.scaleY = value
                addButton.rotation = (1 - value) * 360
            }
        }

        addButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    addButtonSpring.setStartValue(addButton.scaleX).animateToFinalPosition(.8F)
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    addButtonSpring.setStartValue(addButton.scaleX).animateToFinalPosition(1F)
                    val timingStrings = videoView.player?.contentPosition?.convertPositionToTimingString()?: arrayOf()

                    viewModel.addItem(
                        SubTitle(
                            text = "",
                            start_timing = timingStrings[0],
                            end_timing = timingStrings[1],
                            public_id = viewModel.publicId
                        )
                    )

                    smoothScroller.targetPosition = subtitlesAdapter.itemCount + 1
                    recyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
            }
            return@setOnTouchListener true
        }


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

        subtitlesAdapter.onDeleteClicked = { subtitle ->
            viewModel.deleteItem(subtitle.id)
        }

        subtitlesAdapter.onTitleChanged = { id, text, start, end ->
            viewModel.changeTitle(id, text, start, end)
        }

        subtitlesAdapter.setHasStableIds(true)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = subtitlesAdapter

        val anim: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(
                requireContext(),
                R.anim.inputs_list_animation
            )

        recyclerView.layoutAnimation = anim
    }

    private fun hideSubtitlesList() {
        recyclerView.post {
            recyclerView.translationY = -recyclerView.height.toFloat()
            recyclerView.visibility = View.GONE
            recyclerView.tag = 0
        }
        addButton.visibility = View.GONE
        VideoViewCollapseExpandAnimation.collapse(videoView)
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(
            SubTitleViewModel::
            class.java
        )

        viewModel.generatedUrl.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotBlank())
                initializePlayer(it)
        })
    }

    private fun showSubtitlesList() {
        hideKeyboard(loadButton)
        videoReload = true
        viewModel.setPublicId(publicIdValue.text.toString())
        viewModel.setCloudName(cloudNameValue.text.toString())


        val db = AppDatabase.getInstance(requireContext())
        db.subTitleDao().getAll(viewModel.publicId).observe(viewLifecycleOwner, Observer {

            if (viewModel.publicId.isEmpty() || it.isNotEmpty() && it[0].public_id != viewModel.publicId)
                return@Observer

            recyclerView.visibility = View.VISIBLE
            addButton.visibility = View.VISIBLE

            val avd = (addButton.drawable as AnimatedVectorDrawable)

            val addButtonSpring = createSpringAnimation(
                addButton,
                DynamicAnimation.ROTATION,
                33F,
                0.3f
            ).apply {
                addUpdateListener { _, value, _ ->
                    addButton.translationY = -(value * 2)
                }
            }

            val recyclerViewSpring = createSpringAnimation(
                recyclerView,
                DynamicAnimation.TRANSLATION_Y,
                33F,
                0.8f
            )

            if (recyclerView.tag == 0) {
                recyclerView.tag = 1
                recyclerView.scheduleLayoutAnimation()
                recyclerViewSpring.setStartValue(-recyclerView.height.toFloat())
                    .animateToFinalPosition(0F)
                addButtonSpring.setStartValue(-90F).animateToFinalPosition(0F)
                avd.start()
            }

            subtitlesAdapter.submitList(it.sortedWith(compareBy({ it.id })))
            viewModel.createCloudinaryUrl(it)
        })

        VideoViewCollapseExpandAnimation.expand(videoView)
    }

    private fun initializePlayer(videoUrl: String) {
        if (!videoReload)
            return

        videoReload = false
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(requireContext(), "cvs")
        )
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(videoUrl))
        player.prepare(videoSource)
    }


    override fun afterTextChanged(s: Editable?) {
        if (viewModel.publicId != s.toString()) {
            hideSubtitlesList()
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoPlayerScreen()
    }
}