package com.victor.polskavisa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class DocsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton toTheTop;
    private FloatingActionButton backButton;

    private Activity context;

    private TextView title;
    private TextView cTitle;
    private TextView dTitle;

    private int[] buttonIdArray = {R.id.c_doc_ua, R.id.c_doc_pl, R.id.c_doc_eng, R.id.d_doc_ua, R.id.d_doc_pl};
    private String[] pdfNameArray = {"wiza_schengen_ua_200315.pdf", "wiza_schengen_pl_200315.pdf", "wiza_schengen_en_200315.pdf", "d_anketa_ukr_240114.pdf", "d_anketa_pl_240114.pdf"};

    private Map<Integer, String> resMap;

    private ScrollView scrollContainer;
    private Handler h;
    private Timer timer;

    private static final int SHOW = 1;
    private static final int HIDE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.docs_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();

        toTheTop = (FloatingActionButton) getActivity().findViewById(R.id.to_the_top_button);
        toTheTop.setOnClickListener(this);
        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if(backButton.isHiden()){
            backButton.showAnim(100);
        }

        scrollContainer = (ScrollView) getActivity().findViewById(R.id.docs_scroll);

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

        title = (TextView) context.findViewById(R.id.docs_title_view);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf"));
        cTitle = (TextView) context.findViewById(R.id.docs_c_view_title);
        cTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));
        dTitle = (TextView) context.findViewById(R.id.docs_d_view_title);
        dTitle.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf"));

        resMap = new HashMap<>();
        Button b;

        for(int i = 0; i < buttonIdArray.length; i++) {
            resMap.put(buttonIdArray[i], pdfNameArray[i]);
            b = (Button) context.findViewById(buttonIdArray[i]);
            b.setOnClickListener(this);
            b.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-CondLight.ttf"));
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
        int id = view.getId();
        switch (id) {
            case R.id.to_the_top_button:
                scrollContainer.fullScroll(View.FOCUS_UP);
                break;
            default:
                if(resMap.containsKey(id)){
                    copyReadAssets(resMap.get(id));
                }
        }
    }

    private void copyReadAssets(String pdf) {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(context.getFilesDir(), pdf);
        try {
            in = assetManager.open(pdf);
            out = context.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + context.getFilesDir() + "/" + pdf),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
