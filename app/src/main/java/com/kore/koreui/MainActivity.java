package com.kore.koreui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import kore.botssdk.activity.BotChatActivity;
import kore.botssdk.net.SDKConfig;

public class MainActivity extends AppCompatActivity {
    Button btnBotConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnBotConnect = findViewById(R.id.btnBotConnect);

        //If token is empty sdk token generation will happen. if not empty we will use this token for bot connection.
        String jwtToken = "";

        //Set Server url
        SDKConfig.setServerUrl("https://bots.kore.ai/");
        //Set Branding url
        SDKConfig.setBrandingUrl("https://bots.kore.ai/");
        //Initialize the bot with bot config
        //You can pass client id and client secret as null when you pass jwt token
        SDKConfig.initialize("st-b9889c46-218c-58f7-838f-73ae9203488c", "Zen Banking Bot", "cs-1e845b00-81ad-5757-a1e7-d0f6fea227e9", "5OcBSQtH/k6Q/S6A3bseYfOee02YjjLLTNoT1qZDBso=", "email@kore.com", jwtToken);

        //Inject the custom template like below
        SDKConfig.setCustomTemplateView("link", new LinkTemplateView(MainActivity.this));

        btnBotConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BotChatActivity.class);
                startActivity(intent);
            }
        });
    }
}