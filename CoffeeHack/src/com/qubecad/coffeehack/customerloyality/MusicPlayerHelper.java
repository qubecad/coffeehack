package com.qubecad.coffeehack.customerloyality;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.List;

/**
 * Created by carl on 04/12/13.
 */
public class MusicPlayerHelper {
    final static String TAG = "com.qubecad.thisismyjam.MusicPlayerHelper";

    public static void SearchDeezerAndPlay(String query, Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setAction(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.setComponent(new ComponentName("deezer.android.app", "com.deezer.android.ui.activity.LauncherActivity"));
        intent.putExtra(SearchManager.QUERY, query);

        context.startActivity(intent);

    }

    public static void PlayInDeezer(String deezerID, Context context) {

        String uri = "http://www.deezer.com/track/" + deezerID;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName("deezer.android.app", "com.deezer.android.ui.activity.LauncherActivity"));
        intent.setData(Uri.parse(uri));
        if (isCallable(intent, context)) {
            context.startActivity(intent);
        }

    }

    private static boolean isCallable(Intent intent, Context context) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {


            return true;
        } else
            return false;

    }

    public static void SearchSpotifyAndPlay(String query, Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setAction(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
        intent.setComponent(new ComponentName("com.spotify.mobile.android.ui", "com.spotify.mobile.android.ui.Launcher"));

        intent.putExtra(SearchManager.QUERY, query);

        context.startActivity(intent);
    }

    public static String getSearchableTrackName(String trackName) {

        String searchableTrackName = "";
        int bracketStart = trackName.indexOf("(");

        if (bracketStart != -1) {

            searchableTrackName = trackName.substring(0, bracketStart);
        } else {
            searchableTrackName = trackName;
        }

        Log.d(TAG, "returning " + searchableTrackName);

        return searchableTrackName;
    }

    public static void SearchYouTube(String query, Context context) {

        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", query);
        context.startActivity(intent);
    }

}
