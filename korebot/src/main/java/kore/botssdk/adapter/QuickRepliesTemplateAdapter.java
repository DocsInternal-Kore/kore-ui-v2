package kore.botssdk.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.ListClickListner;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.QuickRepliesPayloadModel;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.view.viewHolder.QuickReplyViewHolder;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class QuickRepliesTemplateAdapter extends RecyclerView.Adapter<QuickReplyViewHolder> {

    ArrayList<QuickReplyTemplate> quickReplyTemplateArrayList;
    final Context context;
    final RecyclerView parentRecyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    boolean isLast;
    String quickWidgetColor, fillColor, quickReplyFontColor;
    int dp1;
    SharedPreferences sharedPreferences;
    ListClickListner listClickListner;

    public QuickRepliesTemplateAdapter(Context context, RecyclerView parentRecyclerView) {
        this.context = context;
        this.parentRecyclerView = parentRecyclerView;
        sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);

        quickWidgetColor = SDKConfiguration.BubbleColors.quickReplyColor;
        fillColor = SDKConfiguration.BubbleColors.quickReplyColor;
        quickReplyFontColor = "#3F51B5";

        fillColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_BG_COLOR, fillColor);
        quickWidgetColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, quickWidgetColor);
        quickReplyFontColor = sharedPreferences.getString(BotResponse.BUTTON_INACTIVE_TXT_COLOR, quickReplyFontColor);

        dp1 = (int) DimensionUtil.dp1;
    }

    @NonNull
    @Override
    public QuickReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = View.inflate(context, R.layout.quick_replies_item_cell, null);

        GradientDrawable gradientDrawable = (GradientDrawable) convertView.findViewById(R.id.quick_reply_view).getBackground();
        gradientDrawable.setStroke(2 * dp1, Color.parseColor(fillColor));
        gradientDrawable.setColor(Color.parseColor(fillColor));

        QuickReplyViewHolder viewHolder = new QuickReplyViewHolder(convertView);
        viewHolder.getQuickReplyTitle().setTextColor(Color.parseColor(quickReplyFontColor));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuickReplyViewHolder holder, int position) {
        QuickReplyTemplate quickReplyTemplate = quickReplyTemplateArrayList.get(position);

        if (quickReplyTemplate.getImage_url() != null && !quickReplyTemplate.getImage_url().isEmpty()) {
            Picasso.get().load(quickReplyTemplate.getImage_url()).into(holder.getQuickReplyImage());
            holder.getQuickReplyImage().setVisibility(View.VISIBLE);
        } else {
            holder.getQuickReplyImage().setVisibility(View.GONE);
        }

        holder.getQuickReplyTitle().setText(quickReplyTemplate.getTitle());

        holder.getQuickReplyRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position =  parentRecyclerView.getChildAdapterPosition(v);
                if (composeFooterInterface != null && invokeGenericWebViewInterface != null && isLast)
                {
                    QuickReplyTemplate quickReplyTemplate = quickReplyTemplateArrayList.get(position);
                    String quickReplyPayload;
                    try {
                        quickReplyPayload = (String) quickReplyTemplate.getPayload();
                    }catch (Exception e)
                    {
                        try {
                            QuickRepliesPayloadModel quickRepliesPayloadModel = (QuickRepliesPayloadModel) quickReplyTemplate.getPayload();
                            quickReplyPayload = quickRepliesPayloadModel.getName();
                        }
                        catch (Exception exception)
                        {
                            quickReplyPayload = "";
                        }
                    }

                    if (BundleConstants.BUTTON_TYPE_POSTBACK.equalsIgnoreCase(quickReplyTemplate.getContent_type())) {
                        composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), quickReplyPayload,false);
                    } else if(BundleConstants.BUTTON_TYPE_USER_INTENT.equalsIgnoreCase(quickReplyTemplate.getContent_type())){
                        invokeGenericWebViewInterface.invokeGenericWebView(BundleConstants.BUTTON_TYPE_USER_INTENT);
                    }else if(BundleConstants.BUTTON_TYPE_TEXT.equalsIgnoreCase(quickReplyTemplate.getContent_type())){
                        composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(),quickReplyPayload,false);
                    }else if(BundleConstants.BUTTON_TYPE_WEB_URL.equalsIgnoreCase(quickReplyTemplate.getContent_type())){
                        invokeGenericWebViewInterface.invokeGenericWebView(quickReplyPayload);
                    }else{
                        composeFooterInterface.onSendClick(quickReplyTemplate.getTitle(), quickReplyPayload,false);
                    }

                    listClickListner.listItemClicked(position);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {

        if (quickReplyTemplateArrayList == null) {
            return 0;
        } else {
            return quickReplyTemplateArrayList.size();
        }
    }

    public void setQuickReplyTemplateArrayList(ArrayList<QuickReplyTemplate> quickReplyTemplateArrayList) {
        this.quickReplyTemplateArrayList = quickReplyTemplateArrayList;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public void setListClickListner(ListClickListner listClickListner)
    {
        this.listClickListner = listClickListner;
    }
}

