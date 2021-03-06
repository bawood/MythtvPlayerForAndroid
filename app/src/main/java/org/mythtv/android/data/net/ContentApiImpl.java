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

package org.mythtv.android.data.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mythtv.android.data.entity.LiveStreamInfoEntity;
import org.mythtv.android.data.entity.mapper.BooleanJsonMapper;
import org.mythtv.android.data.entity.mapper.LiveStreamInfoEntityJsonMapper;
import org.mythtv.android.data.exception.NetworkConnectionException;
import org.mythtv.android.domain.SettingsKeys;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Subscriber;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 10/17/15.
 */
public class ContentApiImpl implements ContentApi {

    private static final String TAG = ContentApiImpl.class.getSimpleName();

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern( "yyyy-MM-dd'T'HH:mm:ss'Z'" );

    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final OkHttpClient okHttpClient;
    private final LiveStreamInfoEntityJsonMapper liveStreamInfoEntityJsonMapper;
    private final BooleanJsonMapper booleanJsonMapper;

    public ContentApiImpl( Context context, SharedPreferences sharedPreferences, OkHttpClient okHttpClient, LiveStreamInfoEntityJsonMapper liveStreamInfoEntityJsonMapper, BooleanJsonMapper booleanJsonMapper ) {

        if( null == context || null == sharedPreferences || null == okHttpClient || null == liveStreamInfoEntityJsonMapper || null == booleanJsonMapper ) {

            throw new IllegalArgumentException( "The constructor parameters cannot be null!!!" );
        }

        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.okHttpClient = okHttpClient;
        this.liveStreamInfoEntityJsonMapper = liveStreamInfoEntityJsonMapper;
        this.booleanJsonMapper = booleanJsonMapper;

    }

    @Override
    public Observable<LiveStreamInfoEntity> addliveStream( final String storageGroup, final String filename, final String hostname ) {

        return Observable.create( new Observable.OnSubscribe<LiveStreamInfoEntity>() {

            @Override
            public void call( Subscriber<? super LiveStreamInfoEntity> subscriber ) {

                if( isThereInternetConnection() ) {

                    try {

                        Reader responseLiveStreamInfo = addLiveStreamFromApi( storageGroup, filename, hostname );
                        if( null != responseLiveStreamInfo ) {

                            subscriber.onNext( liveStreamInfoEntityJsonMapper.transformLiveStreamInfoEntity( responseLiveStreamInfo ) );
                            subscriber.onCompleted();

                        } else {

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {

                    subscriber.onError( new NetworkConnectionException() );

                }

            }

        });

    }

    @Override
    public Observable<LiveStreamInfoEntity> addRecordingliveStream( final int recordedId, final int chanId, final DateTime startTime ) {

        return Observable.create( new Observable.OnSubscribe<LiveStreamInfoEntity>() {

            @Override
            public void call( Subscriber<? super LiveStreamInfoEntity> subscriber ) {

                if( isThereInternetConnection() ) {

                    try {

                        Reader responseLiveStreamInfo = addRecordingLiveStreamFromApi( recordedId, chanId, startTime );
                        if( null != responseLiveStreamInfo ) {

                            subscriber.onNext( liveStreamInfoEntityJsonMapper.transformLiveStreamInfoEntity( responseLiveStreamInfo ) );
                            subscriber.onCompleted();

                        } else {

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {

                    subscriber.onError( new NetworkConnectionException() );

                }

            }

        });

    }

    @Override
    public Observable<LiveStreamInfoEntity> addVideoliveStream( final int id ) {

        return Observable.create( new Observable.OnSubscribe<LiveStreamInfoEntity>() {

            @Override
            public void call( Subscriber<? super LiveStreamInfoEntity> subscriber ) {

                if( isThereInternetConnection() ) {

                    try {

                        Reader responseLiveStreamInfo = addVideoLiveStreamFromApi( id );
                        if( null != responseLiveStreamInfo ) {

                            subscriber.onNext( liveStreamInfoEntityJsonMapper.transformLiveStreamInfoEntity( responseLiveStreamInfo ) );
                            subscriber.onCompleted();

                        } else {

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {

                    subscriber.onError( new NetworkConnectionException() );

                }

            }

        });

    }

    @Override
    public Observable<List<LiveStreamInfoEntity>> liveStreamInfoEntityList( final String filename ) {

        return Observable.create( new Observable.OnSubscribe<List<LiveStreamInfoEntity>>() {

            @Override
            public void call( Subscriber<? super List<LiveStreamInfoEntity>> subscriber ) {
                Log.d(TAG, "LiveStreamInfoEntityList.call : enter");

                if( isThereInternetConnection() ) {
                    Log.d(TAG, "LiveStreamInfoEntityList.call : network is connected");

                    try {

                        Reader responseLiveStreamInfoEntities = getLiveStreamInfoEntitiesFromApi( filename );
                        if( null != responseLiveStreamInfoEntities ) {
                            Log.d(TAG, "LiveStreamInfoEntityList.call : retrieved LiveStream info entities");

                            subscriber.onNext( liveStreamInfoEntityJsonMapper.transformLiveStreamInfoEntityCollection(responseLiveStreamInfoEntities) );
                            subscriber.onCompleted();

                        } else {
                            Log.d(TAG, "LiveStreamInfoEntityList.call : failed to retrieve LiveStream info entities");

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {
                        Log.e( TAG, "LiveStreamInfoEntityList.call : error", e );

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {
                    Log.d( TAG, "LiveStreamInfoEntityList.call : network is not connected" );

                    subscriber.onError( new NetworkConnectionException() );

                }

                Log.d( TAG, "LiveStreamInfoEntityList.call : exit" );
            }

        });

    }

    @Override
    public Observable<LiveStreamInfoEntity> liveStreamInfoById( final int id ) {

        return Observable.create( new Observable.OnSubscribe<LiveStreamInfoEntity>() {

            @Override
            public void call( Subscriber<? super LiveStreamInfoEntity> subscriber ) {

                if( isThereInternetConnection() ) {

                    try {

                        Reader responseLiveStreamInfo = getLiveStreamInfoFromApi( id );
                        if( null != responseLiveStreamInfo ) {

                            subscriber.onNext( liveStreamInfoEntityJsonMapper.transformLiveStreamInfoEntity( responseLiveStreamInfo ) );
                            subscriber.onCompleted();

                        } else {

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {

                    subscriber.onError( new NetworkConnectionException() );

                }

            }

        });

    }

    @Override
    public Observable<Boolean> removeLiveStream( final int id ) {

        return Observable.create( new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call( Subscriber<? super Boolean> subscriber ) {

                if( isThereInternetConnection() ) {

                    try {

                        Reader responseLiveStreamInfo = removeLiveStreamInfoFromApi( id );
                        if( null != responseLiveStreamInfo ) {

                            subscriber.onNext( booleanJsonMapper.transformBoolean(responseLiveStreamInfo) );
                            subscriber.onCompleted();

                        } else {

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {

                    subscriber.onError( new NetworkConnectionException() );

                }

            }

        });

    }

    @Override
    public Observable<Boolean> stopLiveStream( final int id ) {

        return Observable.create( new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call( Subscriber<? super Boolean> subscriber ) {

                if( isThereInternetConnection() ) {

                    try {

                        Reader responseLiveStreamInfo = stopLiveStreamInfoFromApi(id);
                        if( null != responseLiveStreamInfo ) {

                            subscriber.onNext( booleanJsonMapper.transformBoolean( responseLiveStreamInfo ) );
                            subscriber.onCompleted();

                        } else {

                            subscriber.onError( new NetworkConnectionException() );

                        }

                    } catch( Exception e ) {

                        subscriber.onError( new NetworkConnectionException( e.getCause() ) );

                    }

                } else {

                    subscriber.onError( new NetworkConnectionException() );

                }

            }

        });

    }

    private Reader addLiveStreamFromApi( final String storageGroup, final String filename, final String hostname ) throws MalformedURLException {

        List<String> params = new ArrayList<>();

        if( null != storageGroup && !"".equals( storageGroup ) ) {

            try {

                String encodedStorageGroup = URLEncoder.encode( storageGroup, "UTF-8" );
                encodedStorageGroup = encodedStorageGroup.replaceAll( "%2F", "/" );
                encodedStorageGroup = encodedStorageGroup.replaceAll( "\\+", "%20" );

                params.add( String.format( STORAGE_GROUP_QS, encodedStorageGroup ) );

            } catch( UnsupportedEncodingException e ) {

                Log.e( TAG, "addLiveStreamFromApi : error", e );

            }

        }

        if( null != filename && !"".equals( filename ) ) {

            try {

                String encodedFilename = URLEncoder.encode( filename, "UTF-8" );
                encodedFilename = encodedFilename.replaceAll( "%2F", "/" );
                encodedFilename = encodedFilename.replaceAll( "\\+", "%20" );

                params.add( String.format( FILENAME_QS, encodedFilename ) );

            } catch( UnsupportedEncodingException e ) {

                Log.e( TAG, "addLiveStreamFromApi : error", e );

            }

        }

        if( null != hostname && !"".equals( hostname ) ) {

           params.add( String.format( HOSTNAME_QS, hostname ) );
        }

        addParameters( params );

        StringBuilder sb = new StringBuilder();
        sb.append( getMasterBackendUrl() );
        sb.append(ADD_LIVE_STREAM_BASE_URL);

        if( !params.isEmpty() ) {

            String separator = "?";

            for( String param : params ) {

                sb.append( separator ).append( param );
                separator = "&";

            }

        }

        Log.d( TAG, "addLiveStreamFromApi : url=" + sb.toString() );
        return ApiConnection.create( okHttpClient, sb.toString() ).requestSyncCall();
    }

    private Reader addRecordingLiveStreamFromApi( final int recordedId, final int chanId, final DateTime startTime ) throws MalformedURLException {

        List<String> params = new ArrayList<>();

        if( -1 > recordedId ) {

            params.add( String.format( RECORDED_ID_QS, recordedId ) );

        }

        if( -1 > chanId ) {

            params.add( String.format( CHAN_ID_QS, chanId ) );

        }

        if( null != startTime ) {

            params.add( String.format( START_TIME_QS, fmt.print( startTime.withZone( DateTimeZone.UTC ) ) ) );

        }

        addParameters( params );

        StringBuilder sb = new StringBuilder();
        sb.append( getMasterBackendUrl() );
        sb.append(ADD_RECORDING_LIVE_STREAM_BASE_URL);

        if( !params.isEmpty() ) {

            String separator = "?";

            for( String param : params ) {

                sb.append( separator ).append( param );
                separator = "&";

            }

        }

        Log.d( TAG, "addRecordingLiveStreamFromApi : url=" + sb.toString() );
        return ApiConnection.create( okHttpClient, sb.toString() ).requestSyncCall();
    }

    private Reader addVideoLiveStreamFromApi( final int id ) throws MalformedURLException {

        List<String> params = new ArrayList<>();

        if( -1 > id ) {

            params.add( String.format( ID_QS, id ) );

        }

        addParameters( params );

        StringBuilder sb = new StringBuilder();
        sb.append( getMasterBackendUrl() );
        sb.append(ADD_VIDEO_LIVE_STREAM_BASE_URL);

        if( !params.isEmpty() ) {

            String separator = "?";

            for( String param : params ) {

                sb.append( separator ).append( param );
                separator = "&";

            }

        }

        Log.d( TAG, "addVideoLiveStreamFromApi : url=" + sb.toString() );
        return ApiConnection.create( okHttpClient, sb.toString() ).requestSyncCall();
    }

    private Reader getLiveStreamInfoEntitiesFromApi( String filename ) throws MalformedURLException {

        StringBuilder sb = new StringBuilder();
        sb.append( getMasterBackendUrl() );
        sb.append(LIVE_STREAM_INFO_LIST_BASE_URL);

        if( null != filename && !"".equals( filename ) ) {

            try {

                String encodedFilename = URLEncoder.encode( filename, "UTF-8" );
                encodedFilename = encodedFilename.replaceAll( "%2F", "/" );
                encodedFilename = encodedFilename.replaceAll( "\\+", "%20" );

                sb.append( "?" );
                sb.append( String.format( FILENAME_QS, encodedFilename ) );

            } catch( UnsupportedEncodingException e ) {

                Log.e( TAG, "addLiveStreamFromApi : error", e );

            }

        }

        Log.d( TAG, "getLiveStreamInfoEntitiesFromApi : url=" + sb.toString() );
        return ApiConnection.create( okHttpClient, sb.toString() ).requestSyncCall();
    }

    private Reader getLiveStreamInfoFromApi( int id ) throws MalformedURLException {

        String apiUrl = String.format(LIVE_STREAM_INFO_URL, id );
        Log.d( TAG, "getLiveStreamInfoFromApi : url=" + apiUrl );

        return ApiConnection.create( okHttpClient, getMasterBackendUrl() + apiUrl ).requestSyncCall();
    }

    private Reader removeLiveStreamInfoFromApi( int id ) throws MalformedURLException {

        String apiUrl = String.format(REMOVE_LIVE_STREAM_URL, id );
        Log.d( TAG, "removeLiveStreamInfoFromApi : url=" + apiUrl );

        return ApiConnection.create( okHttpClient, getMasterBackendUrl() + apiUrl ).requestSyncCall();
    }

    private Reader stopLiveStreamInfoFromApi( int id ) throws MalformedURLException {

        String apiUrl = String.format(STOP_LIVE_STREAM_URL, id );
        Log.d( TAG, "stopLiveStreamInfoFromApi : url=" + apiUrl );

        return ApiConnection.create( okHttpClient, getMasterBackendUrl() + apiUrl ).requestSyncCall();
    }

    private boolean isThereInternetConnection() {

        boolean isConnected;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = ( networkInfo != null && networkInfo.isConnectedOrConnecting() );

        return isConnected;
    }

    private String getMasterBackendUrl() {

        String host = getFromPreferences( SettingsKeys.KEY_PREF_BACKEND_URL );
        String port = getFromPreferences( SettingsKeys.KEY_PREF_BACKEND_PORT );

        String masterBackend = "http://" + host + ":" + port;
        Log.d( TAG, "getMasterBackendUrl : masterBackend=" + masterBackend );

        return masterBackend;
    }

    private void addParameters( List<String> params ) {

        params.add( String.format( WIDTH_QS, getFromPreferences( SettingsKeys.KEY_PREF_HLS_VIDEO_WIDTH ) ) );
        params.add( String.format( HEIGHT_QS, getFromPreferences( SettingsKeys.KEY_PREF_HLS_VIDEO_HEIGHT ) ) );
        params.add( String.format( BITRATE_QS, getFromPreferences( SettingsKeys.KEY_PREF_HLS_VIDEO_BITRATE ) ) );
        params.add( String.format( AUDIO_BITRATE_QS, getFromPreferences( SettingsKeys.KEY_PREF_HLS_AUDIO_BITRATE ) ) );

    }

    private String getFromPreferences( String key ) {

        return sharedPreferences.getString( key, "" );
    }

}
