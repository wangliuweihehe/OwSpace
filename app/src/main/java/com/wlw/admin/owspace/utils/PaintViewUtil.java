package com.wlw.admin.owspace.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.request.RequestOptions;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.glide.GlideApp;
import com.wlw.admin.owspace.view.widget.SelectTextView;


public class PaintViewUtil {
    private final String LINE_H3 = "LINE_H3";
    private final String LINE_H4 = "LINE_H4";
    private final String LINE_HR = "LINE_HR";
    private Typeface typeface;
    private SelectTextView blockTv;
    private int imgH, imgW;
    private View lineView;
    private SelectTextView ntv;
    private SelectTextView poetryTv;

    public PaintViewUtil(Context context) {
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/PMingLiU.ttf");
    }

    private void addBlock(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        paintBlockLine(paramContext, paramViewGroup);
        paintBlockTextView(paramContext, paramViewGroup, paramSpannableStringBuilder);
    }

    private void addH3TextView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        addLines(paramContext, paramViewGroup);
        ntv = new SelectTextView(paramContext);
        ntv.setSingleLine(false);
        ntv.setTextIsSelectable(true);
        setFont(ntv);
        paramViewGroup.addView(ntv);
        putTextSpanViewSetting(ntv, paramSpannableStringBuilder);
        paramViewGroup.addView(ntv);
    }

    private void addH4TextView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        addLines(paramContext, paramViewGroup);
        ntv = new SelectTextView(paramContext);
        ntv.setSingleLine(false);
        ntv.setTextIsSelectable(true);
        ntv.setTextColor(paramContext.getResources().getColor(R.color.black));
        setFont(ntv);
        paramViewGroup.addView(ntv);
        putTextSpanViewSetting(ntv, paramSpannableStringBuilder);

    }

    private void addH5TextView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {

        ntv = new SelectTextView(paramContext);
        ntv.setSingleLine(false);
        ntv.setTextIsSelectable(true);
        ntv.setTextColor(paramContext.getResources().getColor(R.color.green));
        ntv.setTextSize(10);
        setFont(ntv);
        paramViewGroup.addView(ntv);
        putTextSpanViewSetting(ntv, paramSpannableStringBuilder);

    }

    private void addLines(Context paramContext, ViewGroup paramViewGroup) {
        lineView = new View(paramContext);
        LinearLayout.LayoutParams lParam = getLinearParams();
        paramViewGroup.addView(lineView);
    }


    private void addH6TextView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        ntv = new SelectTextView(paramContext);
        ntv.setSingleLine(false);
        ntv.setTextIsSelectable(true);
        ntv.setTextColor(paramContext.getResources().getColor(R.color.black));
        ntv.setTextSize(8);
        ntv.setLineSpacing(1.5f, 1.5f);
        setFont(ntv);
        paramViewGroup.addView(ntv);
        putTextSpanViewSetting(ntv, paramSpannableStringBuilder);

    }

    private void addImageView(Activity paramActivity, ViewGroup paraViewGroup, String imgWidth, String imgHeight, String imgUrl) {
        ImageView localImageView = (ImageView) View.inflate(paramActivity, R.layout.item_img_view, null);
        LinearLayout.LayoutParams lParam = getLinearParams();
        if ((imgWidth != null) && (imgHeight != null) && (!imgWidth.isEmpty()) && (!imgHeight.isEmpty())) {
            String width = imgWidth.replace("px", "");
            String height = imgHeight.replace("px", "");
            imgW = AppUtils.dp2px(paramActivity, (float) Double.parseDouble(width));
            imgH = AppUtils.dp2px(paramActivity, (float) Double.parseDouble(height));
            lParam.height = AppUtils.getWindowWidth(paramActivity) * imgH / imgW;
        }
        localImageView.setLayoutParams(lParam);
        if(imgUrl.contains(".gif")){
            GlideApp.with(paramActivity).asGIF().load(imgUrl).into(localImageView);
        }else{
            Glide.with(paramActivity).load(imgUrl).into(localImageView);
        }
        paraViewGroup.addView(localImageView);
    }

    private void addTextSpanView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        ntv = new SelectTextView(paramContext);
        ntv.setSingleLine(false);
        ntv.setLineSpacing(1.5f, 1.8f);
        ntv.setTextSize(15);
        ntv.setTextIsSelectable(true);
        ntv.setTextColor(paramContext.getResources().getColor(R.color.black));
        setFont(ntv);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 14, 0, 0);
        ntv.setGravity(Gravity.START);
        ntv.setLayoutParams(layoutParams);
        TextPaint tp = ntv.getPaint();
        tp.setFakeBoldText(true);
        putTextSpanViewSetting(ntv, paramSpannableStringBuilder);
        paramViewGroup.addView(ntv);
    }

    private void putTextSpanViewSetting(TextView paramTextView, SpannableStringBuilder paramSpannableStringBuilder) {
        paramTextView.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        paramTextView.setGravity(Gravity.START);
    }

    private void setFont(TextView selectTextView) {
        selectTextView.setTypeface(typeface);
    }


    private void paintBlockTextView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        blockTv = new SelectTextView(paramContext);
        LinearLayout.LayoutParams lParam = getLinearParams();
        blockTv.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        int i = 0;
        int j = 0;
        lParam.setMargins(i, j, i, j);
        blockTv.setLayoutParams(lParam);
        paramViewGroup.addView(blockTv);
    }

    private void addPoetry(Context paramContext, ViewGroup paraViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        poetryTv = new SelectTextView(paramContext);
        poetryTv.setSingleLine(false);
        poetryTv.setText(paramSpannableStringBuilder, TextView.BufferType.SPANNABLE);
        poetryTv.setTextColor(paramContext.getResources().getColor(R.color.black));
        setFont(poetryTv);
        paraViewGroup.addView(poetryTv);
    }

    private void addStrongTextSpanView(Context paramContext, ViewGroup paramViewGroup, SpannableStringBuilder paramSpannableStringBuilder) {
        ntv = new SelectTextView(paramContext);
        ntv.setSingleLine(false);
        ntv.setTextSize(15);
        ntv.setTextIsSelectable(true);
        ntv.setTextColor(paramContext.getResources().getColor(R.color.black));
        setFont(ntv);
        LinearLayout.LayoutParams lParam = getLinearParams();
        lParam.setMargins(0, 14, 0, 0);
        ntv.setGravity(Gravity.START);
        ntv.setLayoutParams(lParam);
        TextPaint paint = ntv.getPaint();
        paint.setFakeBoldText(true);
        putTextSpanViewSetting(ntv, paramSpannableStringBuilder);

    }

    private void paintBlockLine(Context paramContext, ViewGroup paramViewGroup) {
        lineView = new View(paramContext);
        LinearLayout.LayoutParams lParam = getLinearParams();
        lineView.setLayoutParams(lParam);
        paramViewGroup.addView(lineView);
    }

    private LinearLayout.LayoutParams getLinearParams() {
        return new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void addTypeView(Activity paramActivity, ViewGroup paramViewGroup, int type, SpannableStringBuilder paramSpannableStringBuilder) {
        this.addTypeView(paramActivity, paramViewGroup, type, paramSpannableStringBuilder, "", "", "");
    }

    public void addTypeView(Activity paramActivity, ViewGroup paramViewGroup, int type, String imgWidth, String imgHeight, String imgUrl) {
        this.addTypeView(paramActivity, paramViewGroup, type, null, imgWidth, imgHeight, imgUrl);
    }

    private void addTypeView(Activity paramActivity, ViewGroup paramViewGroup, int type, SpannableStringBuilder paramSpannableStringBuilder, String imgWidth, String imgHeight, String imgUrl) {
            switch (type) {
                case 0:
                case 1:
                case 2:
                    addTextSpanView(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                case 3:
                    addH3TextView(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                case 4:
                    addH4TextView(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                case 5:
                    addH5TextView(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                case 6:
                    addH6TextView(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                case 7:
                    addBlock(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                case 8:
                    addPoetry(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                case 9:
                    addImageView(paramActivity, paramViewGroup, imgWidth, imgHeight, imgUrl);
                    break;
                case 10:
                    addLines(paramActivity, paramViewGroup);
                    break;
                case 11:
                    addStrongTextSpanView(paramActivity, paramViewGroup, paramSpannableStringBuilder);
                    break;
                default:
                    break;
            }
        }
}
