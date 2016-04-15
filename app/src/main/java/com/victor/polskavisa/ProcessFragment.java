package com.victor.polskavisa;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessFragment extends Fragment implements View.OnClickListener {

    private Activity context;

    private Boolean isWaitingClick = false;
    private int lastViewId = -1;

    private FloatingActionButton backButton;
    private FloatingActionButton toTheTop;

    private ScrollView scrollContainer;
    private Handler h;
    private Timer timer;

    private static final int SHOW = 1;
    private static final int HIDE = 0;

    private final int[] titleIds = {R.id.step_1_view_title, R.id.step_2_view_title, R.id.step_3_view_title, R.id.step_4_view_title, R.id.step_5_view_title, R.id.process_imp_view_title, R.id.payback_view_title, R.id.pass_receive_view_title};
    private final int[] containerIds = {R.id.step_1_view_container, R.id.step_2_view_container, R.id.step_3_view_container, R.id.step_4_view_container, R.id.step_5_view_container, R.id.process_imp_view_container, R.id.payback_view_container, R.id.pass_receive_view_container};
    private final int[] devIds = {R.id.step_1_dev, R.id.step_2_dev, R.id.step_3_dev, R.id.step_4_dev, R.id.step_5_dev, R.id.process_imp_dev, R.id.payback_dev, R.id.pass_receive_dev};
    private final int[] contentIds = {R.id.step_1_view_content, R.id.step_2_view_content, R.id.step_3_view_content, R.id.step_4_view_content, R.id.step_5_view_content, R.id.process_imp_view_content, R.id.payback_view_content, R.id.pass_receive_view_content};
    private final int[] stringIds = {R.string.step_1, R.string.step_2, R.string.step_3, R.string.step_4, R.string.step_5, R.string.process_imp, R.string.payback, R.string.pass_receive};
    private final int[] arrowImgIds = {R.id.step_1_arrow, R.id.step_2_arrow, R.id.step_3_arrow, R.id.step_4_arrow, R.id.step_5_arrow, R.id.process_imp_arrow, R.id.payback_arrow, R.id.pass_receive_arrow};

    private List<View> contList;
    private View[] dev;
    private TextView[] content;
    private TextView[] title;
    private ImageView[] arrow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.process_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if (backButton.isHiden()) {
            backButton.showAnim(100);
        }
        toTheTop = (FloatingActionButton) getActivity().findViewById(R.id.to_the_top_button);
        toTheTop.setOnClickListener(this);
        scrollContainer = (ScrollView) getActivity().findViewById(R.id.process_scroll);

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

        TextView contacts = (TextView) getActivity().findViewById(R.id.process_title_view);
        contacts.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf"));

        contList = new ArrayList<>();
        dev = new View[titleIds.length];
        content = new TextView[titleIds.length];
        arrow = new ImageView[titleIds.length];
        title = new TextView[titleIds.length];

        for (int i = 0; i < titleIds.length; i++) {

            contList.add(context.findViewById(containerIds[i]));
            dev[i] = context.findViewById(devIds[i]);
            dev[i].setVisibility(View.GONE);
            title[i] = (TextView) context.findViewById(titleIds[i]);
            title[i].setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-CondLight.ttf"));
            content[i] = (TextView) context.findViewById(contentIds[i]);
            content[i].setVisibility(View.GONE);
            content[i].setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
            arrow[i] = (ImageView) context.findViewById(arrowImgIds[i]);
            contList.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.to_the_top_button:
                scrollContainer.fullScroll(View.FOCUS_UP);
                break;
            default:
                int possition = contList.indexOf(view);
                int id = view.getId();
                if (content[possition].getVisibility() == View.GONE) {
                    arrow[possition].setImageResource(R.drawable.ic_arrow_up);
                    content[possition].setText(Html.fromHtml(getString(stringIds[possition])));
                    showViews(content[possition], dev[possition]);
                } else if (isWaitingClick && id == lastViewId) {
                    isWaitingClick = false;
                    hideViews(content[possition], dev[possition]);
                    arrow[possition].setImageResource(R.drawable.ic_arrow_down);
                } else {
                    isWaitingClick = true;
                    new Thread(new DoubleClickThread()).start();
                }
                lastViewId = id;
                break;
        }
    }

    private class DoubleClickThread implements Runnable {

        @Override
        public void run() {
            synchronized (isWaitingClick) {
                try {
                    Thread.sleep(750);
                    isWaitingClick = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void hideViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
        }
    }

    private void showViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
        if (!toTheTop.isHiden())
            toTheTop.hideAnim(100);
    }
}
