package com.kore.koreui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.view.CustomTemplateView;

/**
 * Created by Pradeep Mahato on 21/7/17.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonView extends CustomTemplateView {

    ListView autoExpandListView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    private Context context;

    public BotButtonView(@NonNull Context context) {
        super(context);
        init(context);
    }

    @Override
    public void populateTemplate(@NonNull String payloadInner, boolean isLast) {
//        if(payloadInner.getButtons() != null)
//        {
//            final BotButtonTemplateAdapter buttonTypeAdapter;
//            autoExpandListView.setVisibility(VISIBLE);
//            buttonTypeAdapter = new BotButtonTemplateAdapter(getContext());
//            buttonTypeAdapter.setEnabled(isLast);
//            autoExpandListView.setAdapter(buttonTypeAdapter);
//            buttonTypeAdapter.setBotButtonModels(payloadInner.getButtons());
//            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
//            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
//            buttonTypeAdapter.notifyDataSetChanged();
//        }
    }

    @NonNull
    @Override
    public CustomTemplateView getNewInstance() {
        return new BotButtonView(context);
    }

    private void init(Context context) {
        this.context = context;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.button_view, this, true);
        autoExpandListView = (ListView) inflatedView.findViewById(R.id.botCustomButtonList);
    }

    @Override
    public void setComposeFooterInterface(@NonNull ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    @Override
    public void setInvokeGenericWebViewInterface(@NonNull InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }
}
