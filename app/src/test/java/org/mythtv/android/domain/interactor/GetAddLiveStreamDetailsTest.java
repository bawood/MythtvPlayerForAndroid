package org.mythtv.android.domain.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mythtv.android.domain.executor.PostExecutionThread;
import org.mythtv.android.domain.executor.ThreadExecutor;
import org.mythtv.android.domain.repository.ContentRepository;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Created by dmfrey on 8/26/15.
 */
public class GetAddLiveStreamDetailsTest {

    private static final String FAKE_STORAGE_GROUP = "fake storage group";
    private static final String FAKE_FILENAME = "fake filename";
    private static final String FAKE_HOSTNAME = "fake hostname";

    private GetAddLiveStreamDetails getAddLiveStreamDetails;

    @Mock private ContentRepository mockContentRepository;
    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks( this );
        getAddLiveStreamDetails = new GetAddLiveStreamDetails( mockContentRepository, mockThreadExecutor, mockPostExecutionThread );

    }

    @Test
    public void testGetAddLiveStreamDetailsUseCaseObservableHappyCase() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put( "STORAGE_GROUP", FAKE_STORAGE_GROUP );
        parameters.put( "FILE_NAME", FAKE_FILENAME );
        parameters.put( "HOST_NAME", FAKE_HOSTNAME );
        getAddLiveStreamDetails.buildUseCaseObservable( parameters );

        verify( mockContentRepository ).addliveStream( FAKE_STORAGE_GROUP, FAKE_FILENAME, FAKE_HOSTNAME );
        verifyZeroInteractions( mockPostExecutionThread );
        verifyZeroInteractions( mockThreadExecutor );

    }

}
