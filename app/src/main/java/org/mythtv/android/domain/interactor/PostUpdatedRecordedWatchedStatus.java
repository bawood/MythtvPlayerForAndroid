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

package org.mythtv.android.domain.interactor;

import org.joda.time.DateTime;
import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.repository.DvrRepository;

import java.util.Map;

import rx.Observable;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 4/9/16.
 */
public class PostUpdatedRecordedWatchedStatus extends DynamicUseCase {

    private final DvrRepository dvrRepository;

    public PostUpdatedRecordedWatchedStatus( final DvrRepository dvrRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {
        super( threadExecutor, postExecutionThread );

        this.dvrRepository = dvrRepository;

    }

    @Override
    protected Observable buildUseCaseObservable( Map parameters ) {

        final int chanId = (Integer) parameters.get( "CHAN_ID" );
        final DateTime startTime = (DateTime) parameters.get( "START_TIME" );
        final boolean watched = (Boolean) parameters.get( "WATCHED" );

        return this.dvrRepository.updateWatchedStatus( chanId, startTime, watched );
    }

}
