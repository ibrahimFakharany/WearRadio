package app.fakharany.com.watchradio

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray


class MainActivity : WearableActivity() {
    var exoplayer: SimpleExoPlayer? = null;
    var TAG = "mainActivity"
    var eventListener = object : ExoPlayer.EventListener {
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            Log.i(TAG, "on play back parametere");
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            Log.i(TAG, "on player error");
        }

        override fun onLoadingChanged(isLoading: Boolean) {
            Log.i(TAG, "on loading changed");
        }

        override fun onPositionDiscontinuity() {
            Log.i(TAG, "on position disconnected");
        }

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
            Log.i(TAG, "on time line changed");
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            Log.i(TAG, "on tracks changed");

        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_ENDED -> {
                    Toast.makeText(this@MainActivity, "state ended", Toast.LENGTH_SHORT).show()
                }
                ExoPlayer.STATE_READY -> {
                    Toast.makeText(this@MainActivity, "state ready", Toast.LENGTH_SHORT).show()
                }
                ExoPlayer.STATE_BUFFERING -> {
                    Toast.makeText(this@MainActivity, "state buffered", Toast.LENGTH_SHORT).show()
                }
                ExoPlayer.STATE_IDLE -> {
                    Toast.makeText(this@MainActivity, "state idle", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    var connectionListener = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("service", "in mainActivity onServiceDisConnected");
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("service", "in mainActivity onServiceConnected");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()
        bindService(
            Intent(this, PlayService::class.java).apply {

                putExtra(URL_KEY, "http://streaming.radio.rtl.fr/rtl-1-48-192")
            },
            connectionListener,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onPause() {
        super.onPause();

    }
}
