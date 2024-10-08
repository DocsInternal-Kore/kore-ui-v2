package kore.botssdk.fileupload.core;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import kore.botssdk.fileupload.configurations.Constants;
import kore.botssdk.fileupload.configurations.FileUploadEndPoints;
import kore.botssdk.fileupload.listeners.ChunkUploadListener;
import kore.botssdk.fileupload.ssl.KoreHttpsUrlConnectionBuilder;
import kore.botssdk.utils.LogUtils;


public class UploadExecutor implements Runnable {

    public final String LOG_TAG = "UploadExecutor";

    /*Intent params*/
    private final String fileName;
    private final String accessToken;
    private final String fileToken;
    private final String userOrTeamId;
    private byte[] dataToSet = null;
    private int chunkNo = 0;
    private final Context context;
    private ChunkUploadListener mListener = null;
    private final String host;
    private final boolean isAnonymousUser;
    private final boolean isWebhook;
    private final String botId;

    private static final String BOUNDARY = "*****";
    private static final String LINE_FEED = "\r\n";
    private static final String CHARSET = "UTF-8";

    public UploadExecutor(Context context, String fileName, String fileToken, String accessToken, String userOrTeamId, byte[] dataToPost,
                          int chunkNo, ChunkUploadListener listener, String host, boolean isAnonymousUser, boolean isWebHook, String botId) {
        this.fileName = fileName;
        this.fileToken = fileToken;
        this.accessToken = accessToken;
        this.userOrTeamId = userOrTeamId;
        this.dataToSet = dataToPost;
        this.chunkNo = chunkNo;
        this.mListener = listener;
        this.context = context;
        this.host = host;
        this.isAnonymousUser = isAnonymousUser;
        this.isWebhook = isWebHook;
        this.botId = botId;
    }

    @Override
    public void run() {
        String serverResponse = null;
        BufferedReader input = null;
        DataOutputStream dataOutputStream = null;
        try {

            LogUtils.d(LOG_TAG, "About to send chunks" + chunkNo + "for file" + fileName);
            String FULL_URL = null;
            if (isAnonymousUser) {
                if (!isWebhook)
                    FULL_URL = host + String.format(FileUploadEndPoints.ANONYMOUS_CHUNK_UPLOAD, userOrTeamId, fileToken);
                else
                    FULL_URL = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_CHUNK_UPLOAD, botId, "ivr", fileToken);
            } else {
                if (!isWebhook)
                    FULL_URL = host + String.format(FileUploadEndPoints.CHUNK_UPLOAD, userOrTeamId, fileToken);
                else
                    FULL_URL = host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_CHUNK_UPLOAD, botId, "ivr", fileToken);
            }
            KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, FULL_URL);
            HttpsURLConnection httpsURLConnection = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();
            httpsURLConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestProperty("User-Agent", Constants.getUserAgent());
            httpsURLConnection.setRequestProperty("Authorization", accessToken);
            httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
            httpsURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            httpsURLConnection.setReadTimeout(Constants.CONNECTION_READ_TIMEOUT);

            dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
            addFormField(dataOutputStream, "chunkNo", String.valueOf(chunkNo));

            // Add fileToken as a string part
            addFormField(dataOutputStream, "fileToken", fileToken);

            // Add chunk as a file part (byte array)
            addFilePart(dataOutputStream, "chunk", dataToSet, fileName);

            // End of multipart/form-data
            dataOutputStream.writeBytes("--" + BOUNDARY + "--" + LINE_FEED);
            dataOutputStream.flush();

            //Real upload starting here -->>
            LogUtils.d("upload new", "good so far");
            input = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

            serverResponse = "";
            for (int c = input.read(); c != -1; c = input.read()) {
                serverResponse = serverResponse + (char) c;
            }
            input.close();
            httpsURLConnection.disconnect();
            LogUtils.d(LOG_TAG, "Got serverResponse for chunk upload" + serverResponse);
            int statusCode = httpsURLConnection.getResponseCode();
            LogUtils.e(LOG_TAG, "status code for chunks" + chunkNo + "is" + statusCode);

            String chunkNo = null;

            if (statusCode == 200) {
                JSONObject jsonObject = new JSONObject(serverResponse);

                if (jsonObject.get("chunkNo") != null) {
                    chunkNo = (String) jsonObject.get("chunkNo");
                    if (mListener != null)
                        mListener.notifyChunkUploadCompleted(chunkNo, fileName);
                    LogUtils.e(LOG_TAG, "Response for chunk ::::" + chunkNo + "for file" + fileName);
                }

            } else {
                if (mListener != null)
                    mListener.notifyChunkUploadCompleted(chunkNo, fileName);
                throw new Exception("Response code not 200");
            }


        } catch (Exception e) {
            LogUtils.e(LOG_TAG, "Exception in uploading chunk " + e);
            e.printStackTrace();
            if (mListener != null)
                mListener.notifyChunkUploadCompleted(chunkNo + "", fileName);
            LogUtils.e(LOG_TAG, "Failed to post message for chunk no:: " + this.chunkNo);
        } finally {
            try {
                if (input != null)
                    input.close();
                if (dataOutputStream != null)
                    dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addFormField(DataOutputStream dataOutputStream, String name, String value) throws IOException {
        dataOutputStream.writeBytes("--" + BOUNDARY + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=" + CHARSET + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.writeBytes(value + LINE_FEED);
        dataOutputStream.flush();
    }

    private void addFilePart(DataOutputStream dataOutputStream, String fieldName, byte[] uploadFileBytes, String fileName) throws IOException {
        dataOutputStream.writeBytes("--" + BOUNDARY + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: application/octet-stream" + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);

        dataOutputStream.write(uploadFileBytes);
        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.flush();
    }

}