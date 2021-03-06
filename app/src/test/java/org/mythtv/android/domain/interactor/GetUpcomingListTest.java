package org.mythtv.android.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mythtv.android.domain.Media;
import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.repository.DvrRepository;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by dmfrey on 3/18/16.
 */
public class GetUpcomingListTest {

    private GetUpcomingProgramList getUpcomingList;

    @Mock
    private ThreadExecutor mockThreadExecutor;

    @Mock
    private PostExecutionThread mockPostExecutionThread;

    @Mock
    private DvrRepository mockDvrRepository;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks( this );
        getUpcomingList = new GetUpcomingProgramList( mockDvrRepository, mockThreadExecutor, mockPostExecutionThread );

    }

    @Test
    public void testGetUpcomingProgramListUseCaseObservableHappyCase() {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put( GetMediaItemList.MEDIA_KEY, Media.UPCOMING.name() );

        getUpcomingList.buildUseCaseObservable( parameters );

        verify( mockDvrRepository ).upcoming( -1, -1, false, -1, -1 );
        verifyNoMoreInteractions( mockDvrRepository );
        verifyZeroInteractions( mockThreadExecutor );
        verifyZeroInteractions( mockPostExecutionThread );

    }

}
