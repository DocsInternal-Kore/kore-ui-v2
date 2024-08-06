package kore.botssdk.view;

import static kore.botssdk.models.BotResponse.VIEW_CSAT;
import static kore.botssdk.models.BotResponse.VIEW_NPS;
import static kore.botssdk.models.BotResponse.VIEW_STAR;
import static kore.botssdk.models.BotResponse.VIEW_THUMBS_UP_DOWN;
import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import java.util.List;
import java.util.Map;

import kore.botssdk.R;
import kore.botssdk.adapter.FeedbackRatingScaleAdapter;
import kore.botssdk.dialogs.FeedbackActionSheetFragment;
import kore.botssdk.listener.ChatContentStateListener;
import kore.botssdk.listener.ComposeFooterInterface;
import kore.botssdk.listener.InvokeGenericWebViewInterface;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.FeedbackRatingModel;
import kore.botssdk.models.FeedbackThumbsArrayModel;
import kore.botssdk.models.PayloadInner;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaFontUtils;
import kore.botssdk.view.viewUtils.DimensionUtil;

public class FeedbackTemplateView extends LinearLayout implements View.OnClickListener, ChatContentStateListener {
    private final Context mContext;
    private TextView tvFeedbackTemplateTitle;
    private ImageView icon1;
    private ImageView icon2;
    private ImageView icon3;
    private ImageView icon4;
    private ImageView icon5;
    private RatingBar rbFeedback;
    private LinearLayoutCompat emojis;
    private RelativeLayout rlViewNPS;
    private RecyclerView rvRatingScale;
    private LinearLayoutCompat thumbsUpDown, llThumbsUp, llThumbsDown;
    private TextView thumbsUp;
    private TextView thumbsDown;
    private TextView thumbsUpText;
    private TextView thumbsDownText;
    private PayloadInner payloadInnerIn;

    public FeedbackTemplateView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public FeedbackTemplateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public FeedbackTemplateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    public InvokeGenericWebViewInterface getInvokeGenericWebViewInterface() {
        return invokeGenericWebViewInterface;
    }


    public void setInvokeGenericWebViewInterface(InvokeGenericWebViewInterface invokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface;
    }

    public ComposeFooterInterface getComposeFooterInterface() {
        return composeFooterInterface;
    }

    public void setComposeFooterInterface(ComposeFooterInterface composeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface;
    }

    private InvokeGenericWebViewInterface invokeGenericWebViewInterface;
    private ComposeFooterInterface composeFooterInterface;


    private void init() {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.feedback_template_view, this, true);
        tvFeedbackTemplateTitle = itemView.findViewById(R.id.tv_feedback_template_title);
        rbFeedback = itemView.findViewById(R.id.rbFeedback);
        emojis = itemView.findViewById(R.id.emojis);
        rlViewNPS = itemView.findViewById(R.id.rlViewNPS);
        thumbsUpDown = itemView.findViewById(R.id.thumbs_up_down);
        thumbsUp = itemView.findViewById(R.id.thumbs_up);
        thumbsDown = itemView.findViewById(R.id.thumbs_down);
        thumbsUpText = itemView.findViewById(R.id.text_thumbs_up);
        thumbsDownText = itemView.findViewById(R.id.text_thumbs_down);
        rvRatingScale = itemView.findViewById(R.id.rvRatingScale);
        llThumbsUp = itemView.findViewById(R.id.llThumbsUp);
        llThumbsDown = itemView.findViewById(R.id.llThumbsDown);

        thumbsUp.setText(new String(Character.toChars(0x1F44D)));
        thumbsDown.setText(new String(Character.toChars(0x1F44E)));

        icon1 = itemView.findViewById(R.id.icon_1);
        icon2 = itemView.findViewById(R.id.icon_2);
        icon3 = itemView.findViewById(R.id.icon_3);
        icon4 = itemView.findViewById(R.id.icon_4);
        icon5 = itemView.findViewById(R.id.icon_5);

        dp1 = (int) DimensionUtil.dp1;
    }

    public void populateData(PayloadInner payloadInner, boolean isEnabled) {

        if (payloadInner != null) {
            this.payloadInnerIn = payloadInner;
            tvFeedbackTemplateTitle.setText(payloadInner.getText());
            String viewType = payloadInner.getView();
            emojis.setVisibility(viewType.equals(VIEW_CSAT) ? View.VISIBLE : View.GONE);
            rbFeedback.setVisibility(viewType.equals(VIEW_STAR) ? View.VISIBLE : View.GONE);
            rlViewNPS.setVisibility(viewType.equals(VIEW_NPS) ? View.VISIBLE : View.GONE);
            thumbsUpDown.setVisibility(viewType.equals(VIEW_THUMBS_UP_DOWN) ? View.VISIBLE : View.GONE);

            switch (payloadInner.getView()) {
                case VIEW_STAR: {
                    rbFeedback.setRating(payloadInner.getCheckedPosition());
                    rbFeedback.setIsIndicator(!isEnabled);
                }
                break;

                case VIEW_NPS: {
                    rvRatingScale.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    List<FeedbackRatingModel> array = payloadInner.getNumbersArrays();
                    FeedbackRatingScaleAdapter adapter = new FeedbackRatingScaleAdapter(array, isEnabled, payloadInner.getCheckedPosition());
                    adapter.setComposeFooterInterface(composeFooterInterface);
                    adapter.setListener(FeedbackTemplateView.this);
                    rvRatingScale.setAdapter(adapter);
                }

                case VIEW_CSAT: {
                    resetAll();
                    loademojis(payloadInner.getCheckedPosition() != -1 ? payloadInner.getCheckedPosition() - 1 : -1);
                    icon1.setOnClickListener(this);
                    icon2.setOnClickListener(this);
                    icon3.setOnClickListener(this);
                    icon4.setOnClickListener(this);
                    icon5.setOnClickListener(this);
                }
                break;

                case VIEW_THUMBS_UP_DOWN: {
                    List<FeedbackThumbsArrayModel> array = payloadInner.getThumbsArrays();
                    if (array.size() > 1) {
                        thumbsUpText.setVisibility(VISIBLE);
                        thumbsDownText.setVisibility(VISIBLE);

                        if (array.get(0).getThumpUpId().equalsIgnoreCase(BundleConstants.POSITIVE))
                            thumbsUpText.setText(array.get(0).getReviewText());
                        else
                            thumbsUpText.setText(array.get(1).getReviewText());

                        if (array.get(0).getThumpUpId().equalsIgnoreCase(BundleConstants.NEGATIVE))
                            thumbsDownText.setText(array.get(0).getReviewText());
                        else
                            thumbsDownText.setText(array.get(1).getReviewText());
                    }

                    llThumbsUp.setOnClickListener(view -> {
                        if (payloadInnerIn.getCheckedPosition() != -1) return;
                        llThumbsUp.setBackgroundColor(ContextCompat.getColor(mContext, R.color.thumbsUpBg));
                        onSelect(1, BotResponse.SELECTED_FEEDBACK);
                        composeFooterInterface.onSendClick(thumbsUpText.getText().toString(), thumbsUpText.getText().toString(), false);
                    });
                    llThumbsDown.setOnClickListener(view -> {
                        if (payloadInnerIn.getCheckedPosition() != -1) return;
                        llThumbsDown.setBackgroundColor(ContextCompat.getColor(mContext, R.color.thumbsDownBg));
                        onSelect(2, BotResponse.SELECTED_FEEDBACK);
                        composeFooterInterface.onSendClick(thumbsDownText.getText().toString(), thumbsDownText.getText().toString(), false);
                    });
                    break;
                }
            }

            if (payloadInner.getSliderView() && !payloadInner.getDialogCancel()) {
                payloadInner.setDialogCancel(true);
                FeedbackActionSheetFragment bottomSheetDialog = new FeedbackActionSheetFragment();
                bottomSheetDialog.setSkillName("skillName", "trigger");
                bottomSheetDialog.setData(payloadInner);
                bottomSheetDialog.setComposeFooterInterface(composeFooterInterface);
                bottomSheetDialog.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface);
                bottomSheetDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "add_tags");
            }
            rbFeedback.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                if (!isEnabled) return;
                onSelect((int) rating, BotResponse.SELECTED_FEEDBACK);
                composeFooterInterface.onSendClick(((int) rating) + "", rating + "", false);
            });
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        int position = -1;
        if (id == R.id.icon_1) {
            position = 1;
        } else if (id == R.id.icon_2) {
            position = 2;
        } else if (id == R.id.icon_3) {
            position = 3;
        } else if (id == R.id.icon_4) {
            position = 4;
        } else if (id == R.id.icon_5) {
            position = 5;
        }
        onSelect(position, BotResponse.SELECTED_FEEDBACK);
        composeFooterInterface.onSendClick(position + "", position + "", false);
        loademojis(position - 1);
    }

    private void resetAll() {
        icon1.setImageResource(R.drawable.feedback_icon_1);
        icon2.setImageResource(R.drawable.feedback_icon_2);
        icon3.setImageResource(R.drawable.feedback_icon_3);
        icon4.setImageResource(R.drawable.feedback_icon_4);
        icon5.setImageResource(R.drawable.feedback_icon_5);
    }

    public void loademojis(int position) {
        this.payloadInnerIn.setEmojiPosition(position);
        switch (position) {
            case 0:
                Glide.with(mContext).load(R.drawable.feedback_icon_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon1));
                break;
            case 1:
                Glide.with(mContext).load(R.drawable.feedback_icon_2).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon2));
                break;
            case 2:
                Glide.with(mContext).load(R.drawable.feedback_icon_3).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon3));
                break;
            case 3:
                Glide.with(mContext).load(R.drawable.feedback_icon_4).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon4));
                break;
            case 4:
                Glide.with(mContext).load(R.drawable.feedback_icon_5).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(new DrawableImageViewTarget(icon5));
                break;
        }
    }

    @Override
    public void onSelect(int value, String key) {
        payloadInnerIn.setCheckedPosition(value);
    }
}
