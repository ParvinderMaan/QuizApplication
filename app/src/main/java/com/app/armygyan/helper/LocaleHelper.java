package com.app.armygyan.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import com.app.armygyan.QuizApplication;
import com.app.armygyan.annotation.Language;

import java.util.Locale;

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 * You can also change the locale of your application on the fly by using the setLocale method.
 */
public class LocaleHelper {


    public static Context onAttach(Context context) {
        String lang = getPersistedData(context);
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context);
    }

    public static Context setLocale(Context context, String language) {
        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

     // read
    private static String getPersistedData(Context context) {
        SharedPrefHelper sharedPrefs = ((QuizApplication) context.getApplicationContext()).getSharedPrefInstance();
        return sharedPrefs.read(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH);
    }
   // write
    private static void persist(Context context, String language) {
        SharedPrefHelper sharedPrefs = ((QuizApplication) context.getApplicationContext()).getSharedPrefInstance();
        sharedPrefs.write(SharedPrefHelper.KEY_LANGUAGE, language);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
//        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }
}