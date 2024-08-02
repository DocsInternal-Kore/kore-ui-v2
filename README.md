# How to integrate BotSdk with UI through gradle implementation

1. Add below snippet in project/build.gradle
   
```
maven { url 'https://www.jitpack.io' }
```
2. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.DocsInternal-Kore:kore-ui-v2:0.1.9'
```
3. You can initialize the bot by providing the bot config like below. You can pass Jwt Token as empty so that we generate token in the SDK. If token is passed we will use it to establish bot connection.
```
//If token is empty sdk token generation will happen. if not empty we will use this token for bot connection.
String jwtToken = "";

//Set clientId, If jwtToken is empty this value is mandatory
String clientId = "PLEASE_ENTER_CLIENT_ID";

//Set clientSecret, If jwtToken is empty this value is mandatory
String clientSecret = "PLEASE_ENTER_CLIENT_SECRET";

//Set botId, This value is mandatory
String botId = "PLEASE_ENTER_BOT_ID";

//Set identity, This value is mandatory
String identity = "PLEASE_ENTER_IDENTITY";

//Set botName, This value is mandatory
String botName = "PLEASE_ENTER_BOT_NAME";

//Set serverUrl, This value is mandatory
String serverUrl = "PLEASE_ENTER_SERVER_URL";

//Set brandingUrl, This value is mandatory
String brandingUrl = "PLEASE_ENTER_BRANDING_SERVER_URL";

//Set JwtServerUrl, This value is mandatory
String jwtServerUrl = "PLEASE_ENTER_JWT_SERVER_URL";

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

```
4. You can navigate to the bot chat window through Intent as below snippet
```
Intent intent = new Intent(MainActivity.this, BotChatActivity.class);
startActivity(intent);
```
