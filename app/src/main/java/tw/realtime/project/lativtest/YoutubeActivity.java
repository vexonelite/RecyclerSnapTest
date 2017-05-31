package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import tw.realtime.project.baseframework.widgets.LogWrapper;

/**
 * Put two RtYoutubePlayerFragments together and test if two player views can play simultaneously.
 * The result matches what the references pointed out: YouTube Android Player SDK enforces
 * a one-video-at-a-time limitation. i.e., Only one player can provide video playback,
 * the rest of players will keep silent.
 * @see <a href="http://stackoverflow.com/questions/17117164/android-multiple-youtube-videos-in-activity">Stackoverflow 1</a>
 * @see <a href="http://stackoverflow.com/questions/22189802/android-youtube-api-play-two-or-more-youtube-players-in-one-main-activity">Stackoverflow 2</a>
 * @see <a href="http://stackoverflow.com/questions/36983353/multiple-youtube-player-in-same-activity">Stackoverflow 3</a>
 */
public class YoutubeActivity extends AppCompatActivity {

    public enum NavigationMode {
        REPLACE,
        PUSH,
        SHROUD
    }

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

//        RtYouTubePlayerFragment fragment = new RtYouTubePlayerFragment();
//        fragment.setVideoUrl("https://youtu.be/QMQwz3uTaHQ");

//        RtYouTubePlayerFragment fragment = RtYouTubePlayerFragment.newInstance("QMQwz3uTaHQ");
//        replacePushOrShroudFragment(fragment, NavigationMode.REPLACE, R.id.youtubeContainer);

        RtYouTubePlayerFragment fragment1 = new RtYouTubePlayerFragment();
        fragment1.initYoutubePlayer(new YoutubePlayerCallback("15rYBbcy4Qc"));
        replacePushOrShroudFragment(fragment1, NavigationMode.REPLACE, R.id.youtubeContainer1);

        RtYouTubePlayerFragment fragment2 = new RtYouTubePlayerFragment();
        fragment2.initYoutubePlayer(new YoutubePlayerCallback("WvDrVUtRHng"));
        replacePushOrShroudFragment(fragment2, NavigationMode.REPLACE, R.id.youtubeContainer2);
    }


    private class YoutubePlayerCallback implements
            RtYouTubePlayerFragment.YoutubePlayerListener,
            YouTubePlayer.OnInitializedListener {

        private String mVideoId;

        YoutubePlayerCallback (String videoId) {
            mVideoId = videoId;
        }

        @Override
        public void onYouTubePlayerAvailable(YouTubePlayer youTubePlayer, boolean wasRestored) {
            if (wasRestored) {
                youTubePlayer.play();
            }
            else {
                youTubePlayer.loadVideo(mVideoId);
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                            YouTubePlayer youTubePlayer,
                                            boolean wasRestored) {
            onYouTubePlayerAvailable(youTubePlayer, wasRestored);
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                            YouTubeInitializationResult youTubeInitializationResult) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onInitializationFailure: " + youTubeInitializationResult);
        }
    }

    public void replacePushOrShroudFragment (final Fragment nextFragment,
                                             final NavigationMode mode,
                                             final int containerResId) {

        if (null == nextFragment) {
            return;
        }
        FragmentManager fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.setTransition(FragmentTransaction.TRANSIT_NONE);

        if (mode == NavigationMode.SHROUD) {
            fragTransaction.add(containerResId, nextFragment);
        }
        else {
            fragTransaction.replace(containerResId, nextFragment);
        }

        if (mode != NavigationMode.REPLACE) {
            fragTransaction.addToBackStack(null);
        }
        else {
            try {
                int len = fragManager.getBackStackEntryCount();
                if (len > 0) {
                    for(int i = 0; i < len; ++i) {
                        //fragManager.popBackStack();
                        fragManager.popBackStackImmediate();
                    }
                }
            }
            catch (Exception e) {
                LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentManager.popBackStackImmediate()", e);
                return;
            }
        }

        try {
            fragTransaction.commit();
        }
        catch (Exception e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on FragmentTransaction.commit()", e);
        }
    }
}
