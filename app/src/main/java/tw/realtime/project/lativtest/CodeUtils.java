package tw.realtime.project.lativtest;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by vexonelite on 2016/10/20.
 */
public class CodeUtils {

    private static String getLogTag () {
        return CodeUtils.class.getSimpleName();
    }

    public static int getColorFromResourceId (Context context, int resID) {
        return (Build.VERSION.SDK_INT < 23)
                ? context.getResources().getColor(resID)
                : context.getResources().getColor(resID, null);
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
}
