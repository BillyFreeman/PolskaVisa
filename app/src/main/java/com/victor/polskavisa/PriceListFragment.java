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

public class PriceListFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton toTheTop;
    private FloatingActionButton backButton;

    private TextView title;
    private TextView tableTitle;
    private TextView tableAdd;
    private TextView zeroTitle;

    private Activity context;

    private Boolean isWaitingClick = false;
    private int lastViewId = -1;

    private final int[] titleIds = {R.id.zero_deal_view_title, R.id.zero_cod_obl_view_title, R.id.zero_cod_nonobl_view_title, R.id.zero_national_view_title, R.id.zero_imp_view_title};
    private final int[] containerIds = {R.id.zero_deal_view_container, R.id.zero_cod_obl_view_container, R.id.zero_cod_nonobl_view_container, R.id.zero_national_view_container, R.id.zero_imp_view_container};
    private final int[] devIds = {R.id.zero_deal_dev, R.id.zero_cod_obl_dev, R.id.zero_cod_nonobl_dev, R.id.zero_national_dev, R.id.zero_imp_dev};
    private final int[] contentIds = {R.id.zero_deal_view_content, R.id.zero_cod_obl_view_content, R.id.zero_cod_nonobl_view_content, R.id.zero_national_view_content, R.id.zero_imp_view_content};
    private final int[] stringIds = {R.string.zero_deal, R.string.zero_cod_obl, R.string.zero_cod_nonobl, R.string.zero_national, R.string.zero_imp};
    private final int[] arrowImgIds = {R.id.zero_deal_arrow, R.id.zero_cod_obl_arrow, R.id.zero_cod_nonobl_arrow, R.id.zero_national_arrow, R.id.zero_imp_arrow};

    private List<View> contList;
    private View[] dev;
    private TextView[] content;
    private TextView[] titleArray;
    private ImageView[] arrow;

    private ScrollView scrollContainer;
    private Handler h;
    private Timer timer;

    private static final int SHOW = 1;
    private static final int HIDE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.price_list_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        toTheTop = (FloatingActionButton) getActivity().findViewById(R.id.to_the_top_button);
        toTheTop.setOnClickListener(this);
        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if (backButton.isHiden()) {
            backButton.showAnim(100);
        }

        scrollContainer = (ScrollView) getActivity().findViewById(R.id.price_list_scroll);

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

        title = (TextView) getActivity().findViewById(R.id.price_title_view);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf"));
        tableTitle = (TextView) getActivity().findViewById(R.id.price_table_title);
        tableTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
        tableAdd = (TextView) getActivity().findViewById(R.id.price_table_add);
        tableAdd.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
        tableAdd.setText(Html.fromHtml(getString(R.string.price_list_table_add)));
        zeroTitle = (TextView) getActivity().findViewById(R.id.zero_price_title_id);
        zeroTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));

        contList = new ArrayList<>();
        dev = new View[titleIds.length];
        content = new TextView[titleIds.length];
        arrow = new ImageView[titleIds.length];
        titleArray = new TextView[titleIds.length];

        for (int i = 0; i < titleIds.length; i++) {

            contList.add(context.findViewById(containerIds[i]));
            dev[i] = context.findViewById(devIds[i]);
            dev[i].setVisibility(View.GONE);
            titleArray[i] = (TextView) context.findViewById(titleIds[i]);
            titleArray[i].setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-CondLight.ttf"));
            content[i] = (TextView) context.findViewById(contentIds[i]);
            content[i].setVisibility(View.GONE);
            content[i].setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
            arrow[i] = (ImageView) context.findViewById(arrowImgIds[i]);
            contList.get(i).setOnClickListener(this);
        }
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
}
