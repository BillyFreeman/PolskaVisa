package com.victor.polskavisa;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class BioFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton backButton;
    private FloatingActionButton toTheTop;
    private TextView title;
    private TextView content;

    private ScrollView scrollContainer;
    private Handler h;
    private Timer timer;

    private static final int SHOW = 1;
    private static final int HIDE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.bio_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toTheTop = (FloatingActionButton) getActivity().findViewById(R.id.to_the_top_button);
        toTheTop.setOnClickListener(this);
        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if(backButton.isHiden()){
            backButton.showAnim(100);
        }

        scrollContainer = (ScrollView) getActivity().findViewById(R.id.bio_scroll);

        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW:
                        toTheTop.showAnim(100);
                        break;
                    case HIDE:
                        toTheTop.hideAnim(100);
                        break;
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (scrollContainer.getScrollY() >= 960 && toTheTop.isHiden()) {
                    h.sendEmptyMessage(SHOW);
                } else if (scrollContainer.getScrollY() < 960 && !toTheTop.isHiden()) {
                    h.sendEmptyMessage(HIDE);
                }
            }
        }, 0, 10);

        title = (TextView) getActivity().findViewById(R.id.bio_title_view);
        content = (TextView) getActivity().findViewById(R.id.bio_content_view);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf"));
        content.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
        content.setText(Html.fromHtml(getString(R.string.bio_content)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
        if (!toTheTop.isHiden())
            toTheTop.hideAnim(100);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_the_top_button:
                scrollContainer.fullScroll(View.FOCUS_UP);
                break;
        }
    }
}
