package tw.realtime.project.lativtest;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * Created by vexonelite on 2016/10/20.
 */

public class CodeUtils {

    private static String getLogTag () {
        return CodeUtils.class.getSimpleName();
    }

    /**
     * @param random It must be a SecureRandom instance.
     * @param length The desired length of result.
     * @return Return a randomly generated String with length is exactly 32.
     */
    public static String generateRandomStringWithLength (SecureRandom random, int length) throws Exception {
//        return random.ints(48,122)
//                .filter(i-> (i<57 || i>65) && (i <90 || i>97))
//                .mapToObj(i -> (char) i)
//                .limit(length)
//                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
//                .toString();

        String randomString = "";
        while (randomString.length() < length) {
            randomString = randomString + generateRandomString(random);
            LogWrapper.showLog(Log.INFO, getLogTag(), "randomString: " + randomString + ", length: " + randomString.length());
        }

        String result = "";
        if (randomString.length() >= length) {
            result = randomString.substring(0, length);
        }
        LogWrapper.showLog(Log.INFO, getLogTag(), "result: " + result + ", length: " + result.length());

        return result;
    }

    /**
     * This works by choosing 130 bits from a cryptographically secure random bit generator,
     * and encoding them in base-32.
     * 128 bits is considered to be cryptographically strong,
     * but each digit in a base 32 number can encode 5 bits,
     * so 128 is rounded up to the next multiple of 5.
     * This encoding is compact and efficient, with 5 random bits per character.
     *
     * Compare this to a random UUID, which only has 3.4 bits per character in standard layout,
     * and only 122 random bits in total.
     *
     * If you allow session identifiers to be easily guessable
     * (too short, flawed random number generator, etc.), attackers can hijack other's sessions.
     *
     * Note that SecureRandom objects are expensive to initialize, so you'll want to keep one around and reuse it.
     *
     * @param random It must be a SecureRandom instance.
     * @return Return a randomly generated String.
     */
    private static String generateRandomString (SecureRandom random) throws Exception {
        return new BigInteger(130, random).toString(32);
    }

    /**
     * Use SHA-256 message digest algorithm to process the given byte array and
     * generate a fixed-length hashed byte array
     * @param data  The given byte array.
     * @return A fixed-length hashed byte array.
     */
    public static byte[] messageDigestSHA256 (byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            return md.digest();
        }
        catch (NoSuchAlgorithmException e) {
            LogWrapper.showLog(Log.ERROR, getLogTag(), "Exception on messageDigestSHA256", e);
            return null;
        }
    }

    /*
    public static boolean hasPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void openBrowser(Context context, String url) {
        Log.i("", "openBrowser - given url: " + url);
        if ( (!url.startsWith("http://")) && (!url.startsWith("https://")) ) {
            url = "http://" + url;
            Log.i("", "openBrowser - append 'http': " + url);
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static boolean hasInterNet () {
        Context context = MainApplication.getInstance().getApplicationContext();
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean hasInterNet = false;
        if ( (null != networkInfo) && (networkInfo.isConnected()) ) {
            try {
                //make a URL to a known source
                URL url = new URL("https://www.google.com");
                //open a connection to that source
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();
                //trying to retrieve data from the source. If there
                //is no connection, this line will fail
                Object objData = urlConnect.getContent();
                hasInterNet = true;
            }
            catch (Exception e) {
                e.printStackTrace();
                //return false;
            }
        }
        Log.w("CodeUtils", "hasInterNet: " + hasInterNet);
        return hasInterNet;
    }

    public static String getNetworkAccessType (Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        String ret = "u";
        if ( (null != networkInfo) && (networkInfo.isConnected()) ) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                ret = "wifi";
            }
            else if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
                ret = "cell";
            }
        }
        return ret;
    }
    */


    public static int getColorFromResourceId (Context context, int resID) {
        return (Build.VERSION.SDK_INT < 23)
                ? context.getResources().getColor(resID)
                : context.getResources().getColor(resID, null);
    }

    public static Point getRealScreenSize (Context context) {

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                manager.getDefaultDisplay().getRealSize(size);
            } catch (NoSuchMethodError e) {
                Log.i("error", "it can't work");
            }
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(metrics);
            size.x = metrics.widthPixels;
            size.y = metrics.heightPixels;
        }
        return size;
    }

    /**
     * Return the current height of Action Bar.
     */
    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
    }

    /**
     * Return the current height of Action Bar.
     */
    public static int getActionBarHeightAlter (Context context) {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{android.R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    /**
     * Return the current height of Status Bar.
     * <p></p>
     * Ref:
     * http://mrtn.me/blog/2012/03/17/get-the-height-of-the-status-bar-in-android/
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources()
                    .getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*
    public static DisplayImageOptions.Builder getDefaultDisplayImageOptionsBuilder () {
        return new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.nophoto)
//                .showImageForEmptyUri(R.drawable.nophoto)
//                .showImageOnFail(R.drawable.nophoto)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .resetViewBeforeLoading(true);
    }

    public static DisplayImageOptions getDefaultDisplayImageOptions () {
        return getDefaultDisplayImageOptionsBuilder().build();
    }
    */

    /**
     * tell system that we do not need to receive the Global Layout Event any more
     * <p></p>
     * @param target The View that you want to remove its GlobalLayoutListener.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutEvent(View target, ViewTreeObserver.OnGlobalLayoutListener listener){
        if (Build.VERSION.SDK_INT < 16) {
            target.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            target.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    /**
     * convert a number String into a decimal String, e.g., 1000000 --> 1,000,000
     */
    public static String getDecimalFormatString (String inputPrice) {
        DecimalFormat fmt = new DecimalFormat();
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        fmt.setGroupingSize(3);
        fmt.setGroupingUsed(true);
        fmt.setDecimalFormatSymbols(fmts);
        try {
            float fPrice = Float.parseFloat(inputPrice);
            return fmt.format(fPrice);
        }
        catch (Exception e) {
            //e.printStackTrace();
            return "";
        }
    }

    /**
     * arrange the given date String into the desired format.
     */
    public static Date convertStringToDate (String dateString, String dateFormat) {
        try {
            if ( (null == dateFormat) || (dateFormat.isEmpty()) ) {
                dateFormat = "yyyy-MM-dd hh:mm:ss";
            }
            Locale locale = Locale.getDefault();
            Log.i("CodeUtil", "convertStringToDate: " + locale);
            SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, locale);
            Date date = fmt.parse(dateString);
            return date;
        }
        catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    /**
     * convert the given date Object into a String with the desired format.
     */
    public static String convertDateToString (Date date, String dateFormat) {
        try {
            if ( (null == dateFormat) || (dateFormat.isEmpty()) ) {
                dateFormat = "yyyy-MM-dd hh:mm:ss";
            }
            Locale locale = Locale.getDefault();
            Log.i("CodeUtil", "convertStringToDate: " + locale);
            SimpleDateFormat fmt = new SimpleDateFormat(dateFormat, locale);
            String newDateString = fmt.format(date);
            return newDateString;
        }
        catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public static int hexStringToDecimalNumber (String hexString) throws RuntimeException {
        if ( (null != hexString) && (!hexString.isEmpty()) ) {
            return Integer.decode(hexString);
        }
        else {
            throw new IllegalArgumentException("hexString is either null or empty!");
        }
    }

    /**
     * convert the number of dp into number of pixel.
     */
    public static int dpToPixel (Context context, float dp) {
        final float density = context.getResources().getDisplayMetrics().density;
        return (int)(density * dp);
    }

    /**
     * apply color to the given String.
     * That is done by set a ForegroundColorSpan to the SpannableStringBuilder.
     */
    public static SpannableStringBuilder appendTextWithColor (SpannableStringBuilder builder,
                                                              String givenText,
                                                              final int color) {
        int spanStart = builder.length();
        int spanEnd;
        builder.append(givenText);
        spanEnd = spanStart + givenText.length();
        if (spanEnd > spanStart) {
            final ForegroundColorSpan fcs = new ForegroundColorSpan(color);
            builder.setSpan(fcs, spanStart, spanEnd, 0);
        }

        return builder;
    }

    /**
     * apply new text size to the given String.
     * That is done by set a RelativeSizeSpan to the SpannableStringBuilder.
     */
    public static SpannableStringBuilder appendTextWithSize (SpannableStringBuilder builder,
                                                             String givenText,
                                                             final float newTextSize,
                                                             final float oriTextSize) {
        int spanStart = builder.length();
        int spanEnd;
        builder.append(givenText);
        spanEnd = spanStart + givenText.length();
        if (spanEnd > spanStart) {
            final RelativeSizeSpan rss = new RelativeSizeSpan(newTextSize/oriTextSize);
            builder.setSpan(rss, spanStart, spanEnd, 0);
        }

        return builder;
    }

    /**
     * apply color and new text size to the given String.
     * That is done by set a ForegroundColorSpan and a RelativeSizeSpan to the SpannableStringBuilder.
     */
    public static SpannableStringBuilder appendTextWithSizeAndColor (
            SpannableStringBuilder builder,
            String givenText,
            final float newTextSize,
            final float oriTextSize,
            final int color) {

        int spanStart = builder.length();
        int spanEnd;
        builder.append(givenText);
        spanEnd = spanStart + givenText.length();
        if (spanEnd > spanStart) {
            final RelativeSizeSpan rss = new RelativeSizeSpan(newTextSize/oriTextSize);
            final ForegroundColorSpan fcs = new ForegroundColorSpan(color);
            builder.setSpan(rss, spanStart, spanEnd, 0);
            builder.setSpan(fcs, spanStart, spanEnd, 0);
        }

        return builder;
    }


}
