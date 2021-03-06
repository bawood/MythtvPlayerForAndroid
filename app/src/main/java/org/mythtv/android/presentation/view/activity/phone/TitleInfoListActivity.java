/*
 * MythtvPlayerForAndroid. An application for Android users to play MythTV Recordings and Videos
 * Copyright (c) 2016. Daniel Frey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mythtv.android.presentation.view.activity.phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import org.mythtv.android.R;
import org.mythtv.android.domain.Media;
import org.mythtv.android.presentation.internal.di.HasComponent;
import org.mythtv.android.presentation.internal.di.components.DaggerMediaComponent;
import org.mythtv.android.presentation.internal.di.components.MediaComponent;
import org.mythtv.android.presentation.model.SeriesModel;
import org.mythtv.android.presentation.view.fragment.phone.SeriesListFragment;

/**
 * Activity that shows a list of programs.
 *
 * @author dmfrey
 *
 * Created on 9/1/15.
 */
public class TitleInfoListActivity extends AbstractBasePhoneActivity implements HasComponent<MediaComponent>, SeriesListFragment.SeriesListListener {

    private static final String TAG = TitleInfoListActivity.class.getSimpleName();

    public static Intent getCallingIntent( Context context ) {

        Intent callingIntent = new Intent( context, TitleInfoListActivity.class );
        callingIntent.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );

        return callingIntent;
    }

    private MediaComponent mediaComponent;

    @Override
    public int getLayoutResource() {

        return R.layout.activity_phone_title_info_list;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        Log.d( TAG, "onCreate : enter" );

        super.onCreate( savedInstanceState );

        this.initializeActivity();
        this.initializeInjector();

        Log.d( TAG, "onCreate : exit" );
    }

    @Override
    protected void onResume() {
        super.onResume();

        setNavigationMenuItemChecked( 1 );

    }

    @Override
    public void onBackPressed() {
        Log.d( TAG, "onBackPressed : enter" );
        super.onBackPressed();

        navigator.navigateToHome( this );

        Log.d( TAG, "onBackPressed : exit" );
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity() {
        Log.d( TAG, "initializeActivity : enter" );

        SeriesListFragment.Builder builder = new SeriesListFragment.Builder( Media.PROGRAM );
        SeriesListFragment seriesListFragment = SeriesListFragment.newInstance( builder.toBundle() );

        addFragment( R.id.fl_fragment, seriesListFragment );

        Log.d( TAG, "initializeActivity : exit" );
    }

    private void initializeInjector() {
        Log.d( TAG, "initializeInjector : enter" );

        this.mediaComponent = DaggerMediaComponent.builder()
                .applicationComponent( getApplicationComponent() )
                .build();

        Log.d( TAG, "initializeInjector : exit" );
    }

    @Override
    public MediaComponent getComponent() {
        Log.d( TAG, "getComponent : enter" );

        Log.d( TAG, "getComponent : exit" );
        return mediaComponent;
    }

    @Override
    public void onSeriesClicked( SeriesModel seriesModel ) {
        Log.d( TAG, "onSeriesClicked : enter" );

        Log.d( TAG, "onSeriesClicked : seriesModel=" + seriesModel );
        navigator.navigateToSeries( this, Media.PROGRAM, true, -1, -1, seriesModel.getTitle(), null, null, seriesModel.getInetref() );

        Log.d( TAG, "onSeriesClicked : exit" );
    }

}
