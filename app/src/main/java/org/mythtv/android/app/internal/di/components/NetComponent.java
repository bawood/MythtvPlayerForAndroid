package org.mythtv.android.app.internal.di.components;

import com.squareup.picasso.Picasso;

import org.mythtv.android.app.internal.di.modules.ApplicationModule;
import org.mythtv.android.app.view.activity.AbstractBaseActivity;
import org.mythtv.android.presentation.internal.di.modules.NetModule;
import org.mythtv.android.presentation.internal.di.modules.SharedPreferencesModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by dmfrey on 5/9/16.
 */
@Singleton
@Component( modules = { ApplicationModule.class, SharedPreferencesModule.class, NetModule.class } )
public interface NetComponent {

    void inject( AbstractBaseActivity baseActivity );

    //Exposed to sub-graphs.
    OkHttpClient okHttpClient();
    Picasso picasso();

}
