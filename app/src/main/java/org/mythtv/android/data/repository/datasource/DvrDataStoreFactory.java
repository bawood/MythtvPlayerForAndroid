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

package org.mythtv.android.data.repository.datasource;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.mythtv.android.data.cache.ProgramCache;
import org.mythtv.android.data.entity.mapper.BooleanJsonMapper;
import org.mythtv.android.data.entity.mapper.EncoderEntityJsonMapper;
import org.mythtv.android.data.entity.mapper.ProgramEntityJsonMapper;
import org.mythtv.android.data.entity.mapper.TitleInfoEntityJsonMapper;
import org.mythtv.android.data.net.DvrApi;
import org.mythtv.android.data.net.DvrApiImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;

/**
 * Created by dmfrey on 8/27/15.
 */
@Singleton
public class DvrDataStoreFactory {

    private static final String TAG = DvrDataStoreFactory.class.getSimpleName();

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final OkHttpClient okHttpClient;
    private final Gson gson;
    private final ProgramCache recordedProgramCache;
    private final SearchDataStoreFactory searchDataStoreFactory;

    @Inject
    public DvrDataStoreFactory( Context context, SharedPreferences sharedPreferences, OkHttpClient okHttpClient, Gson gson, ProgramCache recordedProgramCache, SearchDataStoreFactory searchDataStoreFactory ) {
        Log.d( TAG, "initialize : enter" );

        if( null == context || null == sharedPreferences || null == okHttpClient || null == gson || null == recordedProgramCache || null == searchDataStoreFactory ) {

            throw new IllegalArgumentException( "Constructor parameters cannot be null!!!" );
        }

        this.context = context.getApplicationContext();
        this.sharedPreferences = sharedPreferences;
        this.okHttpClient = okHttpClient;
        this.gson = gson;
        this.recordedProgramCache = recordedProgramCache;
        this.searchDataStoreFactory = searchDataStoreFactory;

        Log.d( TAG, "initialize : exit" );
    }

    public DvrDataStore create( int chanId, DateTime startTime ) {
        Log.d( TAG, "create : enter" );

        DvrDataStore dvrDataStore;

        if( !this.recordedProgramCache.isExpired() && this.recordedProgramCache.isCached( chanId, startTime ) ) {
            Log.d( TAG, "create : cache is not expired and recordedProgram exists in cache" );

            dvrDataStore = new DiskDvrDataStore( this.recordedProgramCache );

        } else {
            Log.d( TAG, "create : query backend for data" );

            dvrDataStore = createMasterBackendDataStore();

        }

        Log.d( TAG, "create : exit" );
        return dvrDataStore;
    }

    public DvrDataStore createMasterBackendDataStore() {
        Log.d( TAG, "createMasterBackendDataStore : enter" );

        TitleInfoEntityJsonMapper titleInfoEntityJsonMapper = new TitleInfoEntityJsonMapper( this.gson );
        ProgramEntityJsonMapper programEntityJsonMapper = new ProgramEntityJsonMapper( this.gson );
        EncoderEntityJsonMapper encoderEntityJsonMapper = new EncoderEntityJsonMapper( this.gson );
        BooleanJsonMapper booleanJsonMapper = new BooleanJsonMapper();
        DvrApi api = new DvrApiImpl( this.context, this.sharedPreferences, this.okHttpClient, titleInfoEntityJsonMapper, programEntityJsonMapper, encoderEntityJsonMapper, booleanJsonMapper );

        Log.d( TAG, "createMasterBackendDataStore : exit" );
        return new MasterBackendDvrDataStore( api, this.recordedProgramCache, this.searchDataStoreFactory );
    }

}