package kore.botssdk.net;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

/**
 * This class is for defining properties
 */
@SuppressLint("UnknownNullness")
public class SDKConfiguration {
    private static final String DEFAULT_JWT_TOKEN_STRING = "PLEASE_ENTER_JWT_TOKEN";
    public static final String APP_REQ_COLOR = "#3942f6"; // KORA COLOR "#3942f6" // BMC COLOR 2f91e5
    /**
     * bot init text  and related settings
     */
    private static boolean TRIGGER_INIT_MESSAGE = false;
    private static String INIT_MESSAGE = "Welpro";
    private static boolean TIME_STAMPS_REQUIRED = true;
    private static final boolean APPLY_FONT_STYLE = true;
    protected static HashMap<String, View> hsh = new HashMap<>();
    private static boolean isZendeskEvent = false;

    public static boolean isTriggerInitMessage() {
        return TRIGGER_INIT_MESSAGE;
    }

    public static void setTriggerInitMessage(boolean triggerInitMessage) {
        TRIGGER_INIT_MESSAGE = triggerInitMessage;
    }

    public static String getInitMessage() {
        return INIT_MESSAGE;
    }

    public static void setInitMessage(String initMessage) {
        INIT_MESSAGE = initMessage;
    }

    public static boolean isApplyFontStyle() {
        return APPLY_FONT_STYLE;
    }

    public static boolean isZendeskEvent() {
        return isZendeskEvent;
    }

    public static void setIsZendeskEvent(boolean isZendesk) {
        isZendeskEvent = isZendesk;
    }

    //JWTServer related configurations
    public static class JWTServer {
        static String JWT_SERVER_URL = "PLEASE_ENTER_JWT_SERVER_URL";

        public static void setJwtServerUrl(String jwtServerUrl) {
            JWT_SERVER_URL = jwtServerUrl;
        }

        static String jwt_token = DEFAULT_JWT_TOKEN_STRING;

        public static void setJwt_token(String jwt_token) {
            JWTServer.jwt_token = jwt_token;
        }

        public static String getJwt_token() {
            return !jwt_token.equals(DEFAULT_JWT_TOKEN_STRING) ? jwt_token : "";
        }
    }

    //Server related configurations
    public static class Server {
        public static void setKoreBotServerUrl(String koreBotServerUrl) {
            KORE_BOT_SERVER_URL = koreBotServerUrl;
        }

        public static void setServerUrl(String serverUrl) {
            SERVER_URL = serverUrl;
        }

        static String KORE_BOT_SERVER_URL = "PLEASE_ENTER_BOT_SERVER_URL";
        public static final String TTS_WS_URL = "wss://speech.kore.ai/tts/ws";
        public static final boolean IS_ANONYMOUS_USER = false;
        public static String SERVER_URL = "PLEASE_ENTER_SERVER_URL";
        public static String TOKEN_SERVER_URL = "PLEASE_ENTER_TOKEN_SERVER_URL";
        public static String Branding_SERVER_URL = "PLEASE_ENTER_BRANDING_SERVER_URL";
        public static final String koreAPIUrl = "PLEASE_ENTER_KORE_API_URL";
        public static HashMap<String, Object> queryParams = new HashMap<>();

        public static RestResponse.BotCustomData customData = new RestResponse.BotCustomData();

        public static void setQueryParams(HashMap<String, Object> queryParams) {
            Server.queryParams = queryParams;
        }

        public static void setCustomData(RestResponse.BotCustomData customData) {
            Server.customData = customData;
        }

        public static void setBrandingUrl(String url) {
            Branding_SERVER_URL = url;
        }

        public static void setTokenUrl(String url) {
            TOKEN_SERVER_URL = url;
        }
    }

    public static class Client {

        public static void setClient_id(String client_id) {
            Client.client_id = client_id;
        }

        public static void setClient_secret(String client_secret) {
            Client.client_secret = client_secret;
        }

        public static void setIdentity(String identity) {
            Client.identity = identity;
        }

        public static void setBot_name(String bot_name) {
            Client.bot_name = bot_name;
        }

        public static void setBot_id(String bot_id) {
            Client.bot_id = bot_id;
        }

        public static String client_id = "PLEASE_ENTER_CLIENT_ID";
        public static String client_secret = "PLEASE_ENTER_CLIENT_SECRET";
        public static String identity = "PLEASE_ENTER_IDENTITY";
        public static String bot_name = "PLEASE_ENTER_BOT_NAME";
        public static String bot_id = "PLEASE_ENTER_BOT_ID";
        public static final boolean enable_ack_delivery = false;
        public static final boolean isWebHook = false;
        public static final String webHook_client_id = "PLEASE_ENTER_WEBHOOK_CLIENT_ID";
        public static final String webHook_client_secret = "PLEASE_ENTER_WEBHOOK_CLIENT_SECRET";
        public static final String webHook_identity = "PLEASE_ENTER_WEBHOOK_IDENTITY";
        public static final String webHook_bot_id = "PLEASE_ENTER_WEBHOOK_BOTID";

        //Weebhook
        // for webhook based communication use following option
        public static String webhookURL = "PLEASE_ENTER_WEBHOOK_URL";
        public static int apiVersion = 2;
    }

    public static class BubbleColors {
        public static String rightBubbleSelected = APP_REQ_COLOR;
        public static boolean showVideoOption = false;

        public static void setRightBubbleSelected(String rightBubbleSelected) {
            BubbleColors.rightBubbleSelected = rightBubbleSelected;
        }

        public static void setRightBubbleUnSelected(String rightBubbleUnSelected) {
            BubbleColors.rightBubbleUnSelected = rightBubbleUnSelected;
        }

        public static void setLeftBubbleSelected(String leftBubbleSelected) {
            BubbleColors.leftBubbleSelected = leftBubbleSelected;
        }

        public static void setLeftBubbleUnSelected(String leftBubbleUnSelected) {
            BubbleColors.leftBubbleUnSelected = leftBubbleUnSelected;
        }

        public static void setLeftBubbleTextColor(String leftBubbleTextColor) {
            BubbleColors.leftBubbleTextColor = leftBubbleTextColor;
        }

        public static void setRightBubbleTextColor(String rightBubbleTextColor) {
            BubbleColors.rightBubbleTextColor = rightBubbleTextColor;
        }

        public static void setWhiteColor(String whiteColor) {
            BubbleColors.whiteColor = whiteColor;
        }

        public static void setLeftBubbleBorderColor(String leftBubbleBorderColor) {
            BubbleColors.leftBubbleBorderColor = leftBubbleBorderColor;
        }

        public static void setRightLinkColor(String rightLinkColor) {
            BubbleColors.rightLinkColor = rightLinkColor;
        }

        public static void setLeftLinkColor(String leftLinkColor) {
            BubbleColors.leftLinkColor = leftLinkColor;
        }


        public static String rightBubbleUnSelected = APP_REQ_COLOR;
        public static String leftBubbleSelected = "#D3D3D3";
        public static String leftBubbleUnSelected = "#f8f9f8";
        public static String leftBubbleTextColor = "#404051";
        public static String rightBubbleTextColor = "#161628";//"#757587";
        public static String whiteColor = "#FFFFFF";
        public static String leftBubbleBorderColor = "#eeeef2";
        public static String rightLinkColor = APP_REQ_COLOR;
        public static String leftLinkColor = APP_REQ_COLOR;
        public static final boolean BubbleUI = false;
        public static boolean showIcon = true;
        public static boolean showIconTop = true;
        public static boolean showAttachment = true;
        public static boolean showASRMicroPhone = true;
        public static boolean showTextToSpeech = true;
        public static boolean showQuickRepliesBottom = true;

        public static int getIcon() {
            return icon;
        }

        public static void setIcon(int icon) {
            BubbleColors.icon = icon;
        }

        public static String getIcon_url() {
            return icon_url;
        }

        public static void setIcon_url(String icon_url) {
            BubbleColors.icon_url = icon_url;
        }

        private static int icon = -1;
        private static String icon_url = "";

        public static String getProfileColor() {
            return profileColor;
        }

        public static void setProfileColor(String profileColor) {
            BubbleColors.profileColor = profileColor;
        }

        static String profileColor = APP_REQ_COLOR;

        public static void setQuickReplyColor(String quickReplyColor) {
            BubbleColors.quickReplyColor = quickReplyColor;
        }

        public static String quickReplyColor = "#EEEEF0";
        public static String quickReplyTextColor = "#000000";
        public static String quickBorderColor = "#000000";

    }

    public static boolean isTimeStampsRequired() {
        return TIME_STAMPS_REQUIRED;
    }

    public static void setTimeStampsRequired(boolean timeStampsRequired) {
        TIME_STAMPS_REQUIRED = timeStampsRequired;
    }

    /**
     * don't use relative it is licenced version
     */
    public enum FONT_TYPES {
        ROBOTO, RELATIVE
    }

    private static final FONT_TYPES fontType = FONT_TYPES.ROBOTO;

    public static void setCustomTemplateView(String templateName, View templateView) {
        hsh.put(templateName, templateView);
        Log.e("HashMap Count", hsh.size() + "");
    }

    public static HashMap<String, View> getCustomTemplateView() {
        return hsh;
    }

}
