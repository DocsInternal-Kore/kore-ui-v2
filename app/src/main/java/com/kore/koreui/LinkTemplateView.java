package com.kore.koreui;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kore.koreui.model.ResponsePayload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import kore.botssdk.fileupload.utils.StringUtils;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.CustomTemplateView;

public class LinkTemplateView extends CustomTemplateView {
    private ImageView ivPdfDownload;
    private TextView tvPdfName;
    private Context context;
    private ProgressBar pbDownload;
    private Gson gson = new Gson();

    public LinkTemplateView(@NonNull Context context) {
        super(context);
        init(context);
    }

    @Override
    public void populateTemplate(@NonNull String botResponse, boolean isLast) {

        Type botResp = new TypeToken<ResponsePayload>() {
        }.getType();
        ResponsePayload responsePayload = gson.fromJson(botResponse, botResp);

        if (responsePayload != null) {
            tvPdfName.setText(responsePayload.getFileName());
            ivPdfDownload.setOnClickListener(v -> {
                File fileLocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + responsePayload.getFileName());
                if (!StringUtils.isNullOrEmpty(responsePayload.getUrl())) {
                    ivPdfDownload.setVisibility(GONE);
                    pbDownload.setVisibility(VISIBLE);

                    if (writeBase64ToDisk(responsePayload.getUrl(), fileLocation)) {
                        ivPdfDownload.setVisibility(VISIBLE);
                        pbDownload.setVisibility(GONE);

                        Toast.makeText(context, "Statement downloaded successfully under downloads", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ivPdfDownload.setVisibility(VISIBLE);
                    pbDownload.setVisibility(GONE);
                    Toast.makeText(context, "Statement can not be downloaded", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @NonNull
    @Override
    public CustomTemplateView getNewInstance() {
        return new LinkTemplateView(context);
    }

    @Override
    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {

    }

    @Override
    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface composeFooterInterface) {

    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.pdf_download_view, this, true);
        tvPdfName = view.findViewById(R.id.tv_pdf_item_title);
        ivPdfDownload = view.findViewById(R.id.ivPdfDownload);
        pbDownload = view.findViewById(R.id.pbDownload);

        KaFontUtils.applyCustomFont(getContext(), view);
    }

    private boolean writeBase64ToDisk(String fileData, File fileLocation) {
        FileOutputStream os = null;
        try {
            fileData = fileData.substring(fileData.indexOf(",") + 1);
            byte[] pdfAsBytes = Base64.decode(String.valueOf(fileData), 0);
            os = new FileOutputStream(fileLocation, false);
            os.write(pdfAsBytes);
            os.flush();
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ivPdfDownload.setVisibility(VISIBLE);
            pbDownload.setVisibility(GONE);
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        }
    }
}
