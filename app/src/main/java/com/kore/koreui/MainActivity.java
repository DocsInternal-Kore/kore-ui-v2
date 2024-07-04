package com.kore.koreui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kore.botssdk.activity.BotChatActivity;
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
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MjAwMDAyNTk3NzEsImV4cCI6MTcyMDA4NjY1OTc3MSwiYXVkIjoiaHR0cHM6Ly9pZHByb3h5LmtvcmUuY29tL2F1dGhvcml6ZSIsImlzcyI6ImNzLTFlODQ1YjAwLTgxYWQtNTc1Ny1hMWU3LWQwZjZmZWEyMjdlOSIsInN1YiI6ImVtYWlsQGtvcmUuY29tIiwiaXNBbm9ueW1vdXMiOmZhbHNlfQ.0N_kJlyhkz-wNdFOIrLciY99qyduYQfneomCjpUBhgs";

        //Set clientId, If jwtToken is empty this value is mandatory
        String clientId = "";

        //Set clientSecret, If jwtToken is empty this value is mandatory
        String clientSecret = "";

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

        //Set Server url
        SDKConfig.setServerUrl(serverUrl);
        //Set Branding url
        SDKConfig.setBrandingUrl(brandingUrl);

        //Initialize the bot with bot config
        //You can pass client id and client secret as empty when you pass jwt token
        SDKConfig.initialize(botId, botName, clientId, clientSecret, identity, jwtToken);

        //Inject the custom template like below
        SDKConfig.setCustomTemplateView("link", new LinkTemplateView(MainActivity.this));
        SDKConfiguration.BubbleColors.showIcon = true;

        btnBotConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BotChatActivity.class);
                startActivity(intent);
            }
        });
    }
}