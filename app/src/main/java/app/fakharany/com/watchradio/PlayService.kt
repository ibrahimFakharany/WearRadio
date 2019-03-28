package app.fakharany.com.watchradio

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

public const val URL_KEY: String = "music_url";

class PlayService : Service() {
    lateinit var exoplayer: SimpleExoPlayer;
    var mBinder: Binder = object : Binder() {
        fun getCurrentService(): PlayService = this@PlayService
        fun getExoplayer(): ExoPlayer = exoplayer
    }

    override fun onCreate() {
        super.onCreate();
        Log.e("playService", "onCreate");
        var trackSelector = DefaultTrackSelector()
        val loadControl = DefaultLoadControl()
        exoplayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("playService", "onStartCommand");
        return START_STICKY;
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("playService", "onDestroy");
        exoplayer?.release()
        exoplayer?.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("playService", "onBind");
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), null)
        val extractorsFactory = DefaultExtractorsFactory()
        var url = intent?.getStringExtra(URL_KEY)
        val audioSource = ExtractorMediaSource(Uri.parse(url), dataSourceFactory, extractorsFactory, null, null)
        exoplayer.let { it?.prepare(audioSource) }
        exoplayer.let {
            it?.playWhenReady = true
        }
        return mBinder;
    }

}