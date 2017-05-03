package tw.realtime.project.lativtest;

import android.content.Context;
import android.os.Build;

/**
 * Created by vexonelite on 2016/10/20.
 */
public class CodeUtils {


    public static int getColorFromResourceId (Context context, int resID) {
        return (Build.VERSION.SDK_INT < 23)
                ? context.getResources().getColor(resID)
                : context.getResources().getColor(resID, null);
    }
}
