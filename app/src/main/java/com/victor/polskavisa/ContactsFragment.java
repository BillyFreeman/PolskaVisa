package com.victor.polskavisa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ContactsFragment extends Fragment implements View.OnClickListener {

    private Activity context;

    private TextView caution;
    private Button gMapButton;

    private Boolean isWaitingClick = false;
    private int lastViewId = -1;

    private FloatingActionButton backButton;
    private FloatingActionButton toTheTop;

    private ScrollView scrollContainer;
    private Handler h;
    private Timer timer;

    private static final int SHOW = 1;
    private static final int HIDE = 0;

    private final int[] titleIds = {R.id.contacts_north_title, R.id.contacts_east_title, R.id.contacts_west_title, R.id.contacts_south_title, R.id.contacts_call_title};
    private final int[] containerIds = {R.id.contacts_north, R.id.contacts_east, R.id.contacts_west, R.id.contacts_south, R.id.contacts_call};
    private final int[] devIds = {R.id.contacts_north_dev, R.id.contacts_east_dev, R.id.contacts_west_dev, R.id.contacts_south_dev, R.id.contacts_call_dev};
    private final int[] contentIds = {R.id.contacts_north_content, R.id.contacts_east_content, R.id.contacts_west_content, R.id.contacts_south_content, R.id.contacts_call_content};
    private final int[] stringIds = {R.string.contact_north_content, R.string.contact_east_content, R.string.contact_west_content, R.string.contact_south_content, R.string.contact_call_content};
    private final int[] arrowImgIds = {R.id.contacts_north_arrow, R.id.contacts_east_arrow, R.id.contacts_west_arrow, R.id.contacts_south_arrow, R.id.contacts_call_arrow};

    private List<View> contList;
    private View[] dev;
    private TextView[] content;
    private TextView[] title;
    private ImageView[] arrow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.contacts_fragment, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        caution = (TextView) context.findViewById(R.id.contacts_caution);
        caution.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-CondLight.ttf"));

        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if (backButton.isHiden()) {
            backButton.showAnim(100);
        }
        toTheTop = (FloatingActionButton) getActivity().findViewById(R.id.to_the_top_button);
        toTheTop.setOnClickListener(this);
        scrollContainer = (ScrollView) getActivity().findViewById(R.id.contacts_scroll);

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

        gMapButton = (Button) getActivity().findViewById(R.id.g_map_button);
        gMapButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
        gMapButton.setOnClickListener(this);

        TextView contacts = (TextView) getActivity().findViewById(R.id.address_title_view);
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
            case R.id.g_map_button:
                if (isGoogleMapsInstalled()) {
                    Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "На вашем устройстве не установлено приложение Google Maps", Toast.LENGTH_SHORT).show();
                }
                break;
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

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
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
