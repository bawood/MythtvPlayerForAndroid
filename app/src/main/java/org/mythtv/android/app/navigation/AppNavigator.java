package org.mythtv.android.app.navigation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.joda.time.DateTime;
import org.mythtv.android.presentation.model.ProgramModel;
import org.mythtv.android.app.view.activity.CastPlayerActivity;
import org.mythtv.android.app.view.activity.MainActivity;
import org.mythtv.android.app.view.activity.PlayerActivity;
import org.mythtv.android.app.view.activity.ProgramDetailsActivity;
import org.mythtv.android.app.view.activity.ProgramDetailsSettingsActivity;
import org.mythtv.android.app.view.activity.ProgramListActivity;
import org.mythtv.android.app.view.activity.SettingsActivity;
import org.mythtv.android.app.view.activity.TitleInfoListActivity;
import org.mythtv.android.app.view.activity.VideoDetailsActivity;
import org.mythtv.android.app.view.activity.VideoDetailsSettingsActivity;
import org.mythtv.android.app.view.activity.VideoListActivity;
import org.mythtv.android.app.view.activity.VideoSeriesListActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 *
 * Created by dmfrey on 8/30/15.
 */
@Singleton
public class AppNavigator {

    private static final String TAG = AppNavigator.class.getSimpleName();

    @Inject
    public AppNavigator() {
        //empty
    }

    public void navigateToHome( Context context ) {
        Log.d( TAG, "navigateToHome : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToHome : context != null" );

            Intent intentToLaunch = MainActivity.getCallingIntent( context );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToHome : exit" );
    }

    public void navigateToTitleInfos( Context context ) {
        Log.d( TAG, "navigateToTitleInfos : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToTitleInfos : context != null" );

            Intent intentToLaunch = TitleInfoListActivity.getCallingIntent(context);
            intentToLaunch.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToTitleInfos : exit" );
    }

    public void navigateToPrograms( Context context, boolean descending, int startIndex, int count, String titleRegEx, String recGroup, String storageGroup ) {
        Log.d( TAG, "navigateToPrograms : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToPrograms : context != null" );

            Intent intentToLaunch = ProgramListActivity.getCallingIntent( context, descending, startIndex, count, titleRegEx, recGroup, storageGroup );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToPrograms : exit" );
    }

    public void navigateToProgram( Context context, int chanId, DateTime startTime ) {
        Log.d( TAG, "navigateToProgram : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToProgram : context != null" );

            Intent intentToLaunch = ProgramDetailsActivity.getCallingIntent( context, chanId, startTime );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToProgram : exit" );
    }

    public void navigateToProgramSettings( Context context ) {
        Log.d( TAG, "navigateToProgramSettings : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToProgramSettings : context != null" );

            Intent intentToLaunch = ProgramDetailsSettingsActivity.getCallingIntent( context );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToProgramSettings : exit" );
    }

    public void navigateToVideos( Context context ) {
        Log.d( TAG, "navigateToVideos : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToVideos : context != null" );

            Intent intentToLaunch = VideoListActivity.getCallingIntent( context );
            intentToLaunch.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToVideos : exit" );
    }

    public void navigateToVideoSeries( Context context, String series ) {
        Log.d( TAG, "navigateToVideoSeries : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToVideoSeries : context != null" );

            Intent intentToLaunch = VideoSeriesListActivity.getCallingIntent( context, series );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToVideoSeries : exit" );
    }

    public void navigateToVideo( Context context, int id, String storeageGroup, String filename, String hostname ) {
        Log.d( TAG, "navigateToVideo : enter" );

        if( null != context ) {
            Log.d(TAG, "navigateToVideo : context != null");

            Intent intentToLaunch = VideoDetailsActivity.getCallingIntent( context, id );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToVideo : exit" );
    }

    public void navigateToVideoSettings( Context context ) {
        Log.d( TAG, "navigateToVideoSettings : enter" );

        if( null != context ) {
            Log.d(TAG, "navigateToVideoSettings : context != null");

            Intent intentToLaunch = VideoDetailsSettingsActivity.getCallingIntent( context );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToVideoSettings : exit" );
    }

    public void navigateToCastPlayer( Context context, ProgramModel programModel ) {
        Log.d( TAG, "navigateToCastPlayer : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToCastPlayer : context != null" );

            Intent intentToLaunch = CastPlayerActivity.getCallingIntent( context, programModel );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToCastPlayer : exit" );
    }

    public void navigateToExternalPlayer( Context context, String uri ) {
        Log.d( TAG, "navigateToExternalPlayer : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToExternalPlayer : context != null" );
            Log.d( TAG, "navigateToExternalPlayer : uri=" + uri );

            Intent intentToLaunch = new Intent( Intent.ACTION_VIEW );
            intentToLaunch.setDataAndType( Uri.parse( uri ), "video/*" );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToInternalPlayer : exit" );
    }

    public void navigateToVideoPlayer( Context context, String uri ) {
        Log.d( TAG, "navigateToVideoPlayer : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToVideoPlayer : context != null" );
            Log.d( TAG, "navigateToVideoPlayer : uri=" + uri );

            Intent intentToLaunch = PlayerActivity.getCallingIntent( context, uri );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToVideoPlayer : exit" );
    }

    public void navigateToSettings( Context context ) {
        Log.d( TAG, "navigateToSettings : enter" );

        if( null != context ) {
            Log.d( TAG, "navigateToSettings : context != null" );

            Intent intentToLaunch = SettingsActivity.getCallingIntent( context );
            intentToLaunch.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity( intentToLaunch );

        }

        Log.d( TAG, "navigateToSettings : exit" );
    }

}
