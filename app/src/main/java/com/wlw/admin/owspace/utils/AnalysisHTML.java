package com.wlw.admin.owspace.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.widget.LinearLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Iterator;


/**
 * @author admin
 */
public class AnalysisHTML {
    public static final String HTML_STRING = "string";
    public static final String HTML_URL = "url";
    private MyHandler handler = new MyHandler();
    private Activity context;
    private LinearLayout fuView;
    private PaintViewUtil paintViewUtil;
    private int viewType = -1;
    private SpannableStringBuilder ssb;

    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    parseDocument((Document) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    public void loadHtml(Activity paramActivity, String content, String type, LinearLayout linearLayout) {
        context = paramActivity;
        fuView = linearLayout;
        String str = content.replaceAll("<br/>", "br;");
        if (TextUtils.equals(type, HTML_URL)) {
            loadHtmlUrl(str);
        } else if (TextUtils.equals(type, HTML_STRING)) {
            loadHtmlString(str);
        }
    }

    private void loadHtmlString(String str) {
        ThreadPoolManager.getThreadPollProxy().execute(() -> {
            Document doc = Jsoup.parseBodyFragment(str);
            Message message = Message.obtain();
            message.obj = doc;
            message.what = 1;
            handler.sendMessage(message);
        });
    }

    private void loadHtmlUrl(String paramString) {
        ThreadPoolManager.getThreadPollProxy().execute(() -> {
            try {
                Document doc = Jsoup.connect(paramString).get();
                Message message = Message.obtain();
                message.obj = doc;
                message.what = 1;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void parseDocument(Document paramDocument) {
        paintViewUtil = new PaintViewUtil(context);
        Iterator<Element> iterator = paramDocument.getAllElements().iterator();
        while (iterator.hasNext()) {
            Element localElement = iterator.next();
            if ((localElement.nodeName().matches("p[1-9]?[0-9]?")) ||
                    (localElement.nodeName().matches("h[1-9]?[0-9]?")) ||
                    (localElement.nodeName().matches("poetry")) ||
                    (localElement.nodeName().matches("block"))) {
                parseChildOfPH(localElement);
            }
            if (localElement.nodeName().matches("img")) {
                viewType = 9;
                String imgUrl = localElement.attr("src");
                if (imgUrl.isEmpty()) {
                    imgUrl = localElement.attr("href");
                }
                if (!TextUtils.isEmpty(localElement.attr("jump_url"))) {
                    ssb = new SpannableStringBuilder(localElement.attr("jump_url"));
                }
                String imgWidth = localElement.attr("width");
                String imgHeight = localElement.attr("height");
                paintViewUtil.addTypeView(context, fuView, viewType, imgWidth, imgHeight, imgUrl);
            }
        }
    }

    private void parseChildOfPH(Element paramElement) {
        String str1 = paramElement.text().replace("br;", "\n");
        if (!TextUtils.isEmpty(str1)) {
            String type = paramElement.nodeName();
            ssb = new SpannableStringBuilder("\n" + str1);
            switch (type) {
                case "h1":
                    viewType = 1;
                    break;
                case "h2":
                    viewType = 2;
                    break;
                case "h3":
                    viewType = 3;
                    break;
                case "h4":
                    viewType = 4;
                    break;
                case "h5":
                    viewType = 5;
                    break;
                case "h6":
                    viewType = 6;
                    break;
                case "block":
                    viewType = 7;
                    break;
                case "poetry":
                    viewType = 8;
                    break;
                case "hr":
                    viewType = 10;
                    break;
                default:
                    viewType = 0;
                    if (type.contains("strong")) {
                        viewType = 11;
                    }
                    ssb = new SpannableStringBuilder("\n" + setFirstLineSpace(str1, 2));
                    break;
            }
            paintViewUtil.addTypeView(context, fuView, viewType, ssb);
        }
    }

    private String setFirstLineSpace(String str, int paramInt) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = paramInt; i >= 0; i--) {
            stringBuilder.append("  ");
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

}
