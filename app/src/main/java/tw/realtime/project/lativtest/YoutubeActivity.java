package tw.realtime.project.lativtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayerFragment;

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

        RtYouTubePlayerFragment fragment = RtYouTubePlayerFragment.newInstance("QMQwz3uTaHQ");
//        RtYouTubePlayerFragment fragment = new RtYouTubePlayerFragment();
//        fragment.setVideoUrl("https://youtu.be/QMQwz3uTaHQ");
        replacePushOrShroudFragment(fragment, NavigationMode.REPLACE, R.id.youtubeContainer);
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
