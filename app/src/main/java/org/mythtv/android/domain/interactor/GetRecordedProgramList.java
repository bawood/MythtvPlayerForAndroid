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

import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.repository.DvrRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 *
 *
 *
 * @author dmfrey
 *
 * Created on 8/26/15.
 */
public class GetRecordedProgramList extends UseCase {

    private final boolean descending;
    private final int startIndex;
    private final int count;
    private final String titleRegEx;
    private final String recGroup;
    private final String storageGroup;
    private final DvrRepository dvrRepository;

    @Inject
    public GetRecordedProgramList( final boolean descending, final int startIndex, final int count, final String titleRegEx, final String recGroup, final String storageGroup, DvrRepository dvrRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread ) {
        super( threadExecutor, postExecutionThread );

        this.descending = descending;
        this.startIndex = startIndex;
        this.count = count;
        this.titleRegEx = titleRegEx;
        this.recGroup = recGroup;
        this.storageGroup = storageGroup;
        this.dvrRepository = dvrRepository;

    }

    @Override
    public Observable buildUseCaseObservable() {

        return this.dvrRepository.recordedPrograms( this.descending, this.startIndex, this.count, this.titleRegEx, this.recGroup, this.storageGroup );
    }

}
