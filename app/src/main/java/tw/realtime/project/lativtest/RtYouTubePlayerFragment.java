package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by vexonelite on 2017/5/9.
 */

public class RtYouTubePlayerFragment extends YouTubePlayerSupportFragment {


    private String currentVideoID = "video_id";
    private YouTubePlayer mYouTubePlayer;

    private String mVideoUrl;


    private String getLogTag () {
        return this.getClass().getSimpleName();
    }

    protected void setVideoUrl (String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public static RtYouTubePlayerFragment newInstance(String videoUrl) {
        RtYouTubePlayerFragment fragment = new RtYouTubePlayerFragment();
        fragment.setVideoUrl(videoUrl);
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

    private void pauseYouTubePlayerIfNeeded() {
        if (null != mYouTubePlayer) {
            mYouTubePlayer.pause();
        }
    }

    private void initYoutubePlayer() {
        initialize(DeveloperKey.DEVELOPER_KEY, new YoutubeInitCallback());
    }

    private class YoutubeInitCallback implements YouTubePlayer.OnInitializedListener {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                            YouTubePlayer youTubePlayer,
                                            boolean wasRestored) {
            mYouTubePlayer = youTubePlayer;
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            if (!wasRestored) {
                youTubePlayer.loadVideo(mVideoUrl, 0);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                            YouTubeInitializationResult youTubeInitializationResult) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "onInitializationFailure: " + youTubeInitializationResult);
        }
    }
}
