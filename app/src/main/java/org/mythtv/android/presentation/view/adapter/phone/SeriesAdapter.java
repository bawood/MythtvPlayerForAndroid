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

package org.mythtv.android.presentation.view.adapter.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.mythtv.android.R;
import org.mythtv.android.domain.SettingsKeys;
import org.mythtv.android.presentation.model.SeriesModel;
import org.mythtv.android.presentation.view.component.AutoLoadImageView;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Adapter that manages a collection of {@link SeriesModel}.
 *
 * @author dmfrey
 *
 * Created on 8/31/15.
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private static final String TAG = SeriesAdapter.class.getSimpleName();

    public interface OnItemClickListener {

        void onSeriesItemClicked( SeriesModel seriesModel );

    }

    private Context context;
    private List<SeriesModel> seriesCollection;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public SeriesAdapter( Context context, Collection<SeriesModel> seriesCollection ) {
        Log.d( TAG, "initialize : enter" );

        this.context = context;
        this.validateSeriesCollection( seriesCollection );
        this.layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.seriesCollection = (List<SeriesModel>) seriesCollection;

        Log.d( TAG, "initialize : exit" );
    }

    @Override
    public int getItemCount() {

        return ( null != this.seriesCollection ) ? this.seriesCollection.size() : 0;
    }

    @Override
    public SeriesViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        Log.d( TAG, "onCreateViewHolder : enter" );

        View view = this.layoutInflater.inflate( R.layout.series_list_item, parent, false );
        SeriesViewHolder titleInfoViewHolder = new SeriesViewHolder( view );

        Log.d( TAG, "onCreateViewHolder : exit" );
        return titleInfoViewHolder;
    }

    @Override
    public void onBindViewHolder( SeriesViewHolder holder, final int position ) {
        Log.d( TAG, "onBindViewHolder : enter" );

        final SeriesModel seriesModel = this.seriesCollection.get( position );
        if( null != seriesModel.getArtworkUrl() && !"".equals( seriesModel.getArtworkUrl() ) ) {

            holder.imageViewArtwork.setImageUrl( getMasterBackendUrl() + seriesModel.getArtworkUrl() );

        } else {

            holder.imageViewArtwork.setImageDrawable( ContextCompat.getDrawable( context, R.drawable.ffffff ) );

        }
        holder.textViewTitle.setText( seriesModel.getTitle() );

        int titleCount = seriesModel.getCount();
        if( titleCount > 0 ) {

            holder.textViewCount.setText( String.valueOf( titleCount ) );

        }

        holder.itemView.setOnClickListener( v -> {
            if( null != SeriesAdapter.this.onItemClickListener ) {

                SeriesAdapter.this.onItemClickListener.onSeriesItemClicked( seriesModel );

            }
        });

        Log.d( TAG, "onBindViewHolder : exit" );
    }

    @Override
    public long getItemId( int position ) {
        Log.d( TAG, "getItemId : enter" );

        Log.d( TAG, "getItemId : exit" );
        return position;
    }

    public void setSeriesCollection( Collection<SeriesModel> seriesCollection ) {
        Log.d( TAG, "setSeriesCollection : enter" );

        this.validateSeriesCollection( seriesCollection );
        this.seriesCollection = (List<SeriesModel>) seriesCollection;
        this.notifyDataSetChanged();

        Log.d( TAG, "setSeriesCollection : exit");
    }

    public void setOnItemClickListener( OnItemClickListener onItemClickListener ) {
        Log.d( TAG, "setOnItemClickListener : enter" );

        this.onItemClickListener = onItemClickListener;

        Log.d( TAG, "setOnItemClickListener : exit" );
    }

    private void validateSeriesCollection( Collection<SeriesModel> seriesCollection ) {
        Log.d(TAG, "validateSeriesCollection : enter");

        if( null == seriesCollection ) {
            Log.w( TAG, "validateSeriesCollection : seriesCollection is null" );

            throw new IllegalArgumentException( "The list cannot be null" );
        }

        Log.d( TAG, "validateSeriesCollection : exit" );
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {

        @BindView( R.id.series_item_artwork )
        AutoLoadImageView imageViewArtwork;

        @BindView( R.id.series_item_title )
        TextView textViewTitle;

        @BindView( R.id.series_item_count )
        TextView textViewCount;

        public SeriesViewHolder( View itemView ) {
            super( itemView );

            ButterKnife.bind( this, itemView );

        }

    }

    private String getMasterBackendUrl() {

        String host = getFromPreferences( this.context, SettingsKeys.KEY_PREF_BACKEND_URL );
        String port = getFromPreferences( this.context, SettingsKeys.KEY_PREF_BACKEND_PORT );

        return "http://" + host + ":" + port;
    }

    public String getFromPreferences( Context context, String key ) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );

        return sharedPreferences.getString( key, "" );
    }

}
