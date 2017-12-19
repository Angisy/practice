package com.sx.wx.viewpager;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        LayoutInflater inflater = getLayoutInflater();
//        View view1 = inflater.inflate(R.layout.find, null);
//        View view2 = inflater.inflate(R.layout.me, null);
//        View view3 = inflater.inflate(R.layout.she, null);
//        View view4 = inflater.inflate(R.layout.you, null);
//        final ArrayList<View> list = new ArrayList<>();
//        list.add(view1);
//        list.add(view2);
//        list.add(view3);
//        list.add(view4);
//        PagerAdapter pagerAdapter = new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return list.size();
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//
//            @Override
//            public View instantiateItem(ViewGroup container, int position) {
//                container.addView(list.get(position));
//                return list.get(position);
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                container.removeView(list.get(position));
//            }
//        };
//        viewPager.setAdapter(pagerAdapter);
        WebView webView = findViewById(R.id.web_view);
//        webView.addJavascriptInterface(new JavaScriptObject(), "blin");

        webView.loadUrl("https://www.baidu.com/");
//        webView.loadUrl("http://183.224.28.129:8091/appservice/view/netWorkProblemCell");
        webView.getSettings().setJavaScriptEnabled(true);

    }

    private class JavaScriptObject {
        @JavascriptInterface
        public void callAndroid() {

        }

    }
}
