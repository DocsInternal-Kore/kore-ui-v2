package kore.botssdk.viewmodels.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import kore.botssdk.listener.BotContentFragmentUpdate;
import kore.botssdk.models.BaseBotMessage;
import kore.botssdk.models.BotResponse;
import kore.botssdk.repository.history.HistoryRepository;
import kore.botssdk.retroresponse.ServerBotMsgResponse;

@RunWith(RobolectricTestRunner.class)
public class BotContentViewModelTest {

    private BotContentViewModel viewModel;
    private HistoryRepository repository;
    private BotContentFragmentUpdate chatView;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        repository = mock(HistoryRepository.class);
        chatView = mock(BotContentFragmentUpdate.class);
        viewModel = new BotContentViewModel(context, chatView, repository);

        // Setup RxJava for testing
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxAndroidPlugins.setMainThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @Test
    public void testViewModelInitialization() {
        assertNotNull(viewModel);
    }

    @Test
    public void testLoadChatHistory_Success() {
        String jwt = "test_jwt";
        ServerBotMsgResponse response = new ServerBotMsgResponse();
        ArrayList<BaseBotMessage> messages = new ArrayList<>();
        messages.add(new BotResponse());
        response.setBotMessages(messages);
        response.setOriginalSize(1);

        when(repository.getHistoryRequest(anyInt(), anyInt(), anyString()))
                .thenReturn(Observable.just(response));

        viewModel.loadChatHistory(0, 10, jwt);

        // Verify that the view was updated
        verify(chatView).onChatHistory(messages, 1, true);
    }

    @Test
    public void testOffsetCalculation() {
        String jwt = "jwt";
        // Initial offset 0, receives 5 messages
        ServerBotMsgResponse response1 = new ServerBotMsgResponse();
        response1.setOriginalSize(5);
        ArrayList<BaseBotMessage> messages1 = new ArrayList<>();
        messages1.add(new BotResponse());
        response1.setBotMessages(messages1);
        
        when(repository.getHistoryRequest(0, 10, jwt)).thenReturn(Observable.just(response1));
        viewModel.loadChatHistory(0, 10, jwt);
        assertEquals(5, viewModel.offset);

        // Next load with offset 5, receives 5 more
        ServerBotMsgResponse response2 = new ServerBotMsgResponse();
        response2.setOriginalSize(5);
        ArrayList<BaseBotMessage> messages2 = new ArrayList<>();
        messages2.add(new BotResponse());
        response2.setBotMessages(messages2);

        when(repository.getHistoryRequest(5, 10, jwt)).thenReturn(Observable.just(response2));
        viewModel.loadChatHistory(5, 10, jwt);
        assertEquals(10, viewModel.offset);
    }
}
