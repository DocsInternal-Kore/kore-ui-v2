package com.kore.koreui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.application.AppControl;
import kore.botssdk.net.SDKConfig;
import kore.botssdk.net.SDKConfiguration;

public class MainActivity extends AppCompatActivity {
    Button btnBotConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBotConnect = findViewById(R.id.btnBotConnect);

        //If token is empty sdk token generation will happen. if not empty we will use this token for bot connection.
        String jwtToken = "";

        //Set clientId, If jwtToken is empty this value is mandatory
        String clientId = "cs-1e845b00-81ad-5757-a1e7-d0f6fea227e9";

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = "5OcBSQtH/k6Q/S6A3bseYfOee02YjjLLTNoT1qZDBso=";

        //Set botId, This value is mandatory
        String botId = "st-b9889c46-218c-58f7-838f-73ae9203488c";

        //Set identity, This value is mandatory
        String identity = "email@kore.com";

        //Set botName, This value is mandatory
        String botName = "Kore.ai Bot";

        //Set serverUrl, This value is mandatory
        String serverUrl = "https://bots.kore.ai/";

        //Set brandingUrl, This value is mandatory
        String brandingUrl = "https://bots.kore.ai/";

        //Set JwtServerUrl, This value is mandatory
        String jwtServerUrl = "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/";

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
        SDKConfiguration.BubbleColors.showASRMicroPhone = true;

        //Flag to show the text to speech Speaker icon
        SDKConfiguration.BubbleColors.showTextToSpeech = true;

        //Flag to show the attachment icon
        SDKConfiguration.BubbleColors.showAttachment = true;

        //Flag to show timestamp of each bot and user messages
        SDKConfiguration.setTimeStampsRequired(true);

        btnBotConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BotChatActivity.class);
                startActivity(intent);
            }
        });
    }
}