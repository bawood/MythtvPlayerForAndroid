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

package org.mythtv.android.presentation.internal.di.modules;

import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.interactor.GetVideo;
import org.mythtv.android.domain.interactor.UseCase;
import org.mythtv.android.domain.repository.VideoRepository;
import org.mythtv.android.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 11/9/15.
 */
@Module
public class VideoModule {

    private int id = -1;

    public VideoModule() { }

    public VideoModule( int id ) {

        this.id = id;

    }

    @Provides
    @PerActivity
    @Named( "videoDetails" )
    UseCase provideVideoUseCase( VideoRepository videoRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {

        return new GetVideo( id, videoRepository, threadExecutor, postExecutionThread );
    }

}
