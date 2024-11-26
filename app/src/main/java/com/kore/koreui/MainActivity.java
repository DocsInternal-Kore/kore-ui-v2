package com.kore.koreui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.application.AppControl;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleUtils;

public class MainActivity extends AppCompatActivity {
    Button btnBotConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBotConnect = findViewById(R.id.btnBotConnect);

        //If token is empty sdk token generation will happen. if not empty we will use this token for bot connection.
        String jwtToken = getConfigValue("jwtToken");

        //Set clientId, If jwtToken is empty this value is mandatory
        String clientId = getConfigValue("clientId");//PLEASE_ENTER_BOT_CLIENT_ID

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = getConfigValue("clientSecret");//PLEASE_ENTER_BOT_CLIENT_SECRET

        //Set botId, This value is mandatory
        String botId = getConfigValue("botId");//PLEASE_ENTER_BOT_ID

        //Set identity, This value is mandatory
        String identity = getConfigValue("identity");//PLEASE_ENTER_IDENTITY

        //Set botName, This value is mandatory
        String botName = getConfigValue("botName");//PLEASE_ENTER_BOT_NAME

        //Set serverUrl, This value is mandatory
        String serverUrl = getConfigValue("serverUrl");//PLEASE_ENTER_SERVER_URL

        //Set brandingUrl, This value is mandatory
        String brandingUrl = getConfigValue("brandingUrl");//PLEASE_ENTER_BRANDING_SERVER_URL

        //Set JwtServerUrl, This value is mandatory
        String jwtServerUrl = getConfigValue("jwtServerUrl");//PLEASE_ENTER_JWT_SERVER_URL

        //Set Server url
        SDKConfig.setServerUrl(serverUrl);
        //Set Branding url
        SDKConfig.setBrandingUrl(brandingUrl);
        //Set JwtServer url
        SDKConfig.setJWTUrl(jwtServerUrl);

        new AppControl(MainActivity.this);

        //Initialize the bot with bot config
        //You can pass client id and client secret as empty when you pass jwt token
        SDKConfig.initialize(botId, botName, clientId, clientSecret, identity, jwtToken);

        //Inject the custom template like below
        SDKConfig.setCustomTemplateView("link", new LinkTemplateView(MainActivity.this));

        //Flag to show the bot icon beside the bot response
        SDKConfiguration.BubbleColors.showIcon = true;

        //Flag to show the bot icon in top position or bottom of the bot response
        SDKConfiguration.BubbleColors.showIconTop = false;

        //Flag to show the Speech to text micro phone icon
        SDKConfiguration.BubbleColors.showASRMicroPhone = false;

        //Flag to show the text to speech Speaker icon
        SDKConfiguration.BubbleColors.showTextToSpeech = false;

        //Flag to show the attachment icon
        SDKConfiguration.BubbleColors.showAttachment = true;

        //Flag to show the quickReplies at Bottom
        SDKConfiguration.BubbleColors.showQuickRepliesBottom = false;

        SDKConfiguration.setIsZendeskEvent(true);

        //Flag to show timestamp of each bot and user messages
        SDKConfiguration.setTimeStampsRequired(true);

        btnBotConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BotChatActivity.class);
                Bundle bundle = new Bundle();
                //This should not be null
                bundle.putString(BundleUtils.BOT_NAME_INITIALS, String.valueOf(SDKConfiguration.Client.bot_name.charAt(0)));
                startActivity(intent);
            }
        });
    }

    public String getConfigValue(String name) {
        try {
            InputStream rawResource = getResources().openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(MainActivity.class.getSimpleName(), "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(MainActivity.class.getSimpleName(), "Failed to open config file.");
        }

        return null;
    }
}