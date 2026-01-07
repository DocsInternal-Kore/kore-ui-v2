# Kore Bot SDK UI Integration (Android)

This document explains **how to integrate Kore Bot SDK with UI** in an Android application using **Gradle**, including detailed inline comments for every configuration option.

---

## Prerequisites

- Android Studio (latest recommended)
- Android application with Gradle
- Kore.ai Bot credentials:
    - Bot ID
    - Bot Name
    - Identity
    - Server URL
    - Branding URL
    - JWT Server URL
- Authentication:
    - JWT Token OR
    - Client ID & Client Secret

---

## Step 1: Add JitPack Repository

Add the following snippet in **project-level `build.gradle`**  
(or `settings.gradle` for newer Android versions):

```gradle
// Required to download Kore UI SDK from GitHub releases
maven { url 'https://www.jitpack.io' }
```

---

## Step 2: Add SDK Dependency

Add the dependency in **app-level `build.gradle`** under `dependencies`:

```gradle
// Kore Bot UI SDK dependency
implementation 'com.github.DocsInternal-Kore:kore-ui-v2:0.3.7'
```

---

## Step 3: SDK Initialization (With Detailed Comments)

The SDK must be initialized **before launching the bot UI**.

```java
// JWT Token
// If empty, SDK will generate token using clientId & clientSecret
String jwtToken = "";

// Client ID
// Mandatory only when jwtToken is empty
String clientId = "PLEASE_ENTER_CLIENT_ID";

// Client Secret
// Mandatory only when jwtToken is empty
String clientSecret = "PLEASE_ENTER_CLIENT_SECRET";

// Bot ID
// Mandatory - identifies the bot to connect
String botId = "PLEASE_ENTER_BOT_ID";

// Identity
// Mandatory - unique user identifier
String identity = "PLEASE_ENTER_IDENTITY";

// Bot Name
// Mandatory - used for UI and session identification
String botName = "PLEASE_ENTER_BOT_NAME";

// Server URL
// Mandatory - Kore.ai bot server endpoint
String serverUrl = "PLEASE_ENTER_SERVER_URL";

// Branding URL
// Mandatory - used to fetch UI branding configuration
String brandingUrl = "PLEASE_ENTER_BRANDING_SERVER_URL";

// JWT Server URL
// Mandatory - endpoint used for JWT token generation
String jwtServerUrl = "PLEASE_ENTER_JWT_SERVER_URL";

// Webhook configuration
// false = WebSocket (default)
// true  = Webhook based communication
SDKConfig.isWebHook(false);

// Initialize Kore SDK
// If jwtToken is empty, clientId & clientSecret will be used
SDKConfig.initialize(
        botId,
        botName,
        clientId,
        clientSecret,
        identity,
        jwtToken,
        serverUrl,
        brandingUrl,
        jwtServerUrl
);

// Optional: Set query parameters for socket connection
// Sample format can be derived from getQueryParams()
SDKConfig.setQueryParams(getQueryParams());

// Inject custom template rendering
// "link" refers to template type
SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);

// Show bot icon next to bot messages
SDKConfig.setIsShowIcon(true);

// Position bot icon
// true  = top of message
// false = bottom of message
SDKConfig.setIsShowIconTop(false);

// Enable timestamps for user & bot messages
SDKConfig.setIsTimeStampsRequired(true);

// Show or hide the bot header
SDKConfig.setIsShowHeader(true);

// Show minimize icon in header
SDKConfig.showHeaderMinimize(true);

// Override branding API response with local branding model
// false = use server branding
// true  = use local branding
SDKConfig.setLocalBranding(false, getLocalBrandingModel());

// Update status bar color based on header background
SDKConfig.setIsUpdateStatusBarColor(false);

// Reset existing bot session (optional)
// Use when switching users or restarting conversation
// SDKConfig.disconnectBotSession(MainActivity.this);

// Enable or disable attachments
SDKConfiguration.OverrideKoreConfig.showAttachment = true;

// Enable microphone for ASR (speech-to-text)
SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = true;

// Enable Text-to-Speech for bot messages
SDKConfiguration.OverrideKoreConfig.showTextToSpeech = true;

// Enable emoji shortcut decryption if bot supports it
SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable = false;

// Pass custom data to bot
// Useful for sending app-specific metadata
RestResponse.BotCustomData customData = new RestResponse.BotCustomData();
customData.put("key", "value");
SDKConfig.setCustomData(customData);
```

---

## Step 4: Launch Bot Chat Screen

Navigate to the bot chat UI using an `Intent`:

```java
// Launch Kore Bot chat activity
Intent intent = new Intent(MainActivity.this, NewBotChatActivity.class);
startActivity(intent);
```

---

## Best Practices

- Initialize SDK only once (preferably in Application or launcher activity)
- Do not hardcode client secrets in production builds
- Call `disconnectBotSession()` when user identity changes

---
