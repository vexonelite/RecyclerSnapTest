package tw.realtime.project.lativtest;

import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import tw.realtime.project.baseframework.widgets.LogWrapper;

/**
 * Created by vexonelite on 2017/5/9.
 */

public class RtYouTubePlayerFragment extends YouTubePlayerSupportFragment {


    private YouTubePlayer mYouTubePlayer;
    private String mVideoId;
    private YoutubeInitCallback mYoutubeInitCallback;
    private YoutubePlayerListener mCallback;


    public interface YoutubePlayerListener {
        void onYouTubePlayerAvailable (YouTubePlayer youTubePlayer, boolean wasRestored);
    }

    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    /**
     *
     * @param videoId e.g., "15rYBbcy4Qc"
     */
    protected void setVideoId (String videoId) {
        mVideoId = videoId;
    }


    public static RtYouTubePlayerFragment newInstance(String videoId) {
        RtYouTubePlayerFragment fragment = new RtYouTubePlayerFragment();
        fragment.setVideoId(videoId);
        fragment.initYoutubePlayer();
        return fragment;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    @Override
    public void onPause() {
        super.onPause();
        pauseYouTubePlayerIfNeeded();
    }

    public void setYoutubePlayerListener (YoutubePlayerListener callback) {
        mCallback = callback;
    }

    public void initYoutubePlayer() {
        if ( (null == mYouTubePlayer) && (null == mYoutubeInitCallback) ) {
            mYoutubeInitCallback = new YoutubeInitCallback();
            initialize(DeveloperKey.DEVELOPER_KEY, mYoutubeInitCallback);
        }
    }

    public void initYoutubePlayer(YouTubePlayer.OnInitializedListener callback) {
        if ( (null == mYouTubePlayer) && (null != callback) ) {
            initialize(DeveloperKey.DEVELOPER_KEY, callback);
        }
    }

    /**
     *
     * @param videoId e.g., "15rYBbcy4Qc"
     */
    public void cueVideoIfNeeded (String videoId) {
        if (null != mYouTubePlayer) {
            mYouTubePlayer.cueVideo(videoId);
        }
    }

    /**
     *
     * @param videoId e.g., "15rYBbcy4Qc"
     */
    public void loadVideoIfNeeded (String videoId) {
        if (null != mYouTubePlayer) {
            mYouTubePlayer.loadVideo(videoId);
        }
    }

    /**
     *
     */
    public void playVideoIfNeeded () {
        if (null != mYouTubePlayer) {
            mYouTubePlayer.play();
        }
    }

    public void pauseYouTubePlayerIfNeeded() {
        if (null != mYouTubePlayer) {
            mYouTubePlayer.pause();
        }
    }

    //Loads the specified video's thumbnail and prepares the player to play the video, but does not download any of the video stream until play() is called.

    private class YoutubeInitCallback implements YouTubePlayer.OnInitializedListener {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                            YouTubePlayer youTubePlayer,
                                            boolean wasRestored) {
            mYouTubePlayer = youTubePlayer;
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            if (null != mCallback) {
                mCallback.onYouTubePlayerAvailable(youTubePlayer, wasRestored);
            }
//            if (!wasRestored) {
//                youTubePlayer.loadVideo(mVideoId, 0);
//            }
            mYoutubeInitCallback = null;
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                            YouTubeInitializationResult youTubeInitializationResult) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onInitializationFailure: " + youTubeInitializationResult);
            mYoutubeInitCallback = null;
        }
    }


}
