package kore.botssdk.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import kore.botssdk.R;
import kore.botssdk.adapter.QuickRepliesTemplateAdapter;
import kore.botssdk.application.AppControl;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.listener.ListClickListner;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.models.QuickReplyTemplate;
import kore.botssdk.utils.KaFontUtils;

public class BotQuickRepliesTemplateView extends LinearLayout implements ListClickListner {

    RecyclerView recyclerView;
    ComposeFooterInterface composeFooterInterface;
    InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    View view;
    PayloadInner payloadInner;
    int dp1;

    public BotQuickRepliesTemplateView(Context context) {
        super(context);
        init();
    }

    public BotQuickRepliesTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BotQuickRepliesTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dp1 = (int) AppControl.getInstance().getDimensionUtil().density;
        view = LayoutInflater.from(getContext()).inflate(R.layout.quick_reply_view, this, true);
        recyclerView = view.findViewById(R.id.rvQuickReplies);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10 * dp1));
        KaFontUtils.applyCustomFont(getContext(), view);
    }


    public void populateQuickReplyView(PayloadInner payloadInner, boolean isLastItem) {
        if (payloadInner != null) {
            this.payloadInner = payloadInner;

            ArrayList<QuickReplyTemplate> quickReplyTemplates = payloadInner.getQuick_replies();

            if (quickReplyTemplates != null && quickReplyTemplates.size() > 0) {
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);

                recyclerView.setLayoutManager(layoutManager);

                QuickRepliesTemplateAdapter quickRepliesAdapter;
                if (recyclerView.getAdapter() == null) {
                    quickRepliesAdapter = new QuickRepliesTemplateAdapter(getContext(), recyclerView);
                    recyclerView.setAdapter(quickRepliesAdapter);
                    quickRepliesAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                    quickRepliesAdapter.setComposeFooterInterface(composeFooterInterface);
                }

                quickRepliesAdapter = (QuickRepliesTemplateAdapter) recyclerView.getAdapter();
                quickRepliesAdapter.setQuickReplyTemplateArrayList(quickReplyTemplates);
                quickRepliesAdapter.setIsLast(isLastItem);
                quickRepliesAdapter.setListClickListner(BotQuickRepliesTemplateView.this);

                if (!this.payloadInner.isIs_end()) recyclerView.setVisibility(VISIBLE);
                else {
                    recyclerView.setVisibility(GONE);
                }
            } else {
                recyclerView.setVisibility(GONE);
            }
        } else {
            recyclerView.setVisibility(GONE);
        }
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    @Override
    public void listItemClicked(int position) {
        payloadInner.setIs_end(true);
        recyclerView.setVisibility(GONE);
    }

    public static class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.top = verticalSpaceHeight;
        }
    }
}
