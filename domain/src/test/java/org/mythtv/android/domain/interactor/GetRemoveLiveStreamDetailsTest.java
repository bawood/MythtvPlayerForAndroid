package org.mythtv.android.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.repository.ContentRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by dmfrey on 8/26/15.
 */
public class GetRemoveLiveStreamDetailsTest {

    private static final int FAKE_LIVE_STREAM_ID = -1;

    private GetRemoveLiveStreamDetails getRemoveLiveStreamDetails;

    @Mock private ContentRepository mockContentRepository;
    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks( this );
        getRemoveLiveStreamDetails = new GetRemoveLiveStreamDetails( mockContentRepository, mockThreadExecutor, mockPostExecutionThread );

    }

    @Test
    public void testGetRemoveLiveStreamDetailsUseCaseObservableHappyCase() {

        Map<String, Integer> parameters = Collections.singletonMap( "LIVE_STREAM_ID", FAKE_LIVE_STREAM_ID );
        getRemoveLiveStreamDetails.buildUseCaseObservable( parameters );

        verify( mockContentRepository ).removeLiveStream( FAKE_LIVE_STREAM_ID );
        verifyZeroInteractions( mockPostExecutionThread );
        verifyZeroInteractions( mockThreadExecutor );

    }

}
