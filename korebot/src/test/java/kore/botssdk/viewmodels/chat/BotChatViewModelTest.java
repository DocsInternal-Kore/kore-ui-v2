package kore.botssdk.viewmodels.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import kore.botssdk.bot.BotClient;
import kore.botssdk.listener.BotChatViewListener;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.BotResponseMessage;
import kore.botssdk.models.ComponentModel;
import kore.botssdk.models.PayloadOuter;
import kore.botssdk.utils.BundleConstants;

@RunWith(RobolectricTestRunner.class)
public class BotChatViewModelTest {

    private BotChatViewModel viewModel;
    private Context context;
    private BotClient botClient;
    private BotChatViewListener chatViewListener;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        botClient = mock(BotClient.class);
        chatViewListener = mock(BotChatViewListener.class);
        viewModel = new BotChatViewModel(context, botClient, chatViewListener);
    }

    @Test
    public void testViewModelInitialization() {
        assertNotNull(viewModel);
    }

    @Test
    public void testGetUniqueDeviceId() {
        String deviceId = viewModel.getUniqueDeviceId(context);
        assertNotNull(deviceId);
        assertEquals(deviceId, viewModel.getUniqueID());
        
        // Ensure it's persistent
        String secondCall = viewModel.getUniqueDeviceId(context);
        assertEquals(deviceId, secondCall);
    }

    @Test
    public void testDisplayMessage_Text() {
        String testText = "Hello Bot";
        String messageId = "msg_123";
        
        viewModel.displayMessage(testText, BotResponse.COMPONENT_TYPE_TEXT, messageId);
        
        // displayMessage eventually calls processPayload which calls addMessageToAdapter
        ArgumentCaptor<BotResponse> captor = ArgumentCaptor.forClass(BotResponse.class);
        verify(chatViewListener, atLeastOnce()).addMessageToAdapter(captor.capture());
        
        BotResponse response = captor.getValue();
        assertEquals(messageId, response.getMessageId());
        assertNotNull(response.getMessage());
        assertEquals(1, response.getMessage().size());
        
        BotResponseMessage botResponseMessage = response.getMessage().get(0);
        ComponentModel componentModel = botResponseMessage.getComponent();
        assertNotNull(componentModel);
        assertEquals(BotResponse.COMPONENT_TYPE_TEXT, componentModel.getType());
        
        PayloadOuter payloadOuter = componentModel.getPayload();
        assertNotNull(payloadOuter);
        assertEquals(testText, payloadOuter.getText());
    }

    @Test
    public void testSetIsActivityResumed() {
        // Since isActivityResumed is private, we test its effect in processPayload
        viewModel.setIsActivityResumed(true);
        
        // If activity is resumed and agent is connected, it should send READ receipts
        // We need to simulate a bot response from an agent
        BotResponse agentResponse = new BotResponse();
        agentResponse.setFromAgent(true);
        agentResponse.setMessageId("agent_msg_1");
        agentResponse.setCreatedOn("2023-10-25T10:00:00.000Z");
        
        ArrayList<BotResponseMessage> messages = new ArrayList<>();
        BotResponseMessage msg = new BotResponseMessage();
        ComponentModel comp = new ComponentModel();
        comp.setType(BotResponse.COMPONENT_TYPE_TEXT);
        PayloadOuter payload = new PayloadOuter();
        payload.setText("Agent speaking");
        comp.setPayload(payload);
        msg.setComponent(comp);
        messages.add(msg);
        agentResponse.setMessage(messages);

        viewModel.processPayload(null, agentResponse);

        // Verify that sendReceipts was called with MESSAGE_READ
        verify(botClient).sendReceipts(BundleConstants.MESSAGE_READ, "agent_msg_1");
    }
}
