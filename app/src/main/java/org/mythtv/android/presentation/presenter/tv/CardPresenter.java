/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mythtv.android.presentation.presenter.tv;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.mythtv.android.R;
import org.mythtv.android.domain.SettingsKeys;
import org.mythtv.android.presentation.model.MediaItemModel;

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 *
 * @author dmfrey
 */
public class CardPresenter extends Presenter {

    private static final String TAG = CardPresenter.class.getSimpleName();

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;

    private static void updateCardBackgroundColor( ImageCardView view, boolean selected ) {

        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;

        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor( color );
        view.findViewById( R.id.info_field ).setBackgroundColor( color );

    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent ) {
        Log.d( TAG, "onCreateViewHolder" );

        sDefaultBackgroundColor = parent.getResources().getColor( R.color.default_background );
        sSelectedBackgroundColor = parent.getResources().getColor( R.color.primary_dark );
        mDefaultCardImage = parent.getResources().getDrawable( R.drawable.movie );

        ImageCardView cardView = new ImageCardView( parent.getContext() ) {
            @Override
            public void setSelected( boolean selected ) {

                updateCardBackgroundColor( this, selected );
                super.setSelected( selected );

            }

        };

        cardView.setFocusable( true);
        cardView.setFocusableInTouchMode( true );
        updateCardBackgroundColor( cardView, false );

        return new ViewHolder( cardView );
    }

    @Override
    public void onBindViewHolder( Presenter.ViewHolder viewHolder, Object item ) {

        if( item instanceof MediaItemModel) {
            Log.d( TAG, "onBindView : item=" + item.toString() );

            MediaItemModel mediaItemModel = (MediaItemModel) item;
            ImageCardView cardView = (ImageCardView) viewHolder.view;
            cardView.setTitleText( ( null == mediaItemModel.getSubTitle() || "".equals( mediaItemModel.getSubTitle() ) ) ? mediaItemModel.getTitle() : mediaItemModel.getSubTitle() );
            cardView.setContentText( mediaItemModel.getDescription() );
            cardView.setMainImageDimensions( CARD_WIDTH, CARD_HEIGHT );

            switch( mediaItemModel.getMedia() ) {

                case PROGRAM :

                    Glide.with( viewHolder.view.getContext() )
                            .load( getMasterBackendUrl( viewHolder.view.getContext() ) + mediaItemModel.getPreviewUrl() )
                            .error( mDefaultCardImage )
                            .into( cardView.getMainImageView() );
                    break;

                default :

                    Glide.with( viewHolder.view.getContext() )
                            .load( getMasterBackendUrl( viewHolder.view.getContext() ) + mediaItemModel.getFanartUrl() )
                            .error( mDefaultCardImage )
                            .into( cardView.getMainImageView() );
                    break;
            }

        }

    }

    @Override
    public void onUnbindViewHolder( Presenter.ViewHolder viewHolder ) {
        Log.d( TAG, "onUnbindViewHolder" );

        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage( null );
        cardView.setMainImage( null );

    }

    protected String getMasterBackendUrl( Context context ) {

        String host = getFromPreferences( context, SettingsKeys.KEY_PREF_BACKEND_URL );
        String port = getFromPreferences( context, SettingsKeys.KEY_PREF_BACKEND_PORT );

        return "http://" + host + ":" + port;
    }

    protected String getFromPreferences( Context context, String key ) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );

        return sharedPreferences.getString( key, "" );
    }

}
