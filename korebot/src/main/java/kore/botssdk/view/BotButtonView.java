package kore.botssdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.BotButtonTemplateAdapter;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotButtonModel;
import kore.botssdk.view.viewUtils.DimensionUtil;
import kore.botssdk.view.viewUtils.LayoutUtils;
import kore.botssdk.view.viewUtils.MeasureUtils;

/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotButtonView extends LinearLayout {

    float dp1, layoutItemHeight = 0;
    ListView autoExpandListView;
    float restrictedMaxWidth, restrictedMaxHeight;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;

    public BotButtonView(Context context) {
        super(context);
        init();
    }

    public BotButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = DimensionUtil.dp1;
        View inflatedView = LayoutInflater.from(getContext()).inflate(R.layout.bot_custom_button_view, this, true);
        autoExpandListView = inflatedView.findViewById(R.id.botCustomButtonList);

        layoutItemHeight = getResources().getDimension(R.dimen.carousel_view_button_height_individual);
    }

    public void setRestrictedMaxWidth(float restrictedMaxWidth) {
        this.restrictedMaxWidth = restrictedMaxWidth;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void populateButtonList(ArrayList<BotButtonModel> botButtonModels, final boolean enabled) {
        final BotButtonTemplateAdapter buttonTypeAdapter;
        if (botButtonModels != null) {
            autoExpandListView.setVisibility(VISIBLE);
            buttonTypeAdapter = new BotButtonTemplateAdapter(getContext());
            buttonTypeAdapter.setEnabled(enabled);
            autoExpandListView.setAdapter(buttonTypeAdapter);
            buttonTypeAdapter.setBotButtonModels(botButtonModels);
            buttonTypeAdapter.setComposeFooterInterface(composeFooterInterface);
            buttonTypeAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
            buttonTypeAdapter.notifyDataSetChanged();
        } else {
            autoExpandListView.setAdapter(null);
            autoExpandListView.setVisibility(GONE);
        }

    }
}
