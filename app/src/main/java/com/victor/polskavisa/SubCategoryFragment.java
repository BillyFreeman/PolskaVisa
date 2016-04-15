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
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class SubCategoryFragment extends Fragment implements View.OnClickListener {

    public static final int C_TOURISM = 1;
    public static final int C_BUSINESS = 2;
    public static final int C_FAMILY = 3;
    public static final int C_SPORT = 4;
    public static final int C_STUDENT = 5;
    public static final int C_MED = 6;
    public static final int C_PRESS = 7;
    public static final int C_TIR = 8;
    public static final int D_NATIONAL = 9;

    private static final int SHOW = 1;
    private static final int HIDE = 0;

    private TextView title;

    private View genDocs;
    private View addDocs;
    private View emplDocs;
    private View finDocs;
    private View pStay;
    private View impInfo;

    private View genDev;
    private View addDev;
    private View emplDev;
    private View finDev;
    private View stayDev;
    private View impDev;

    private TextView genTitle;
    private TextView addTitle;
    private TextView emplTitle;
    private TextView finTitle;
    private TextView pStayTitle;
    private TextView impTitle;

    private ImageView genArrow;
    private ImageView addArrow;
    private ImageView emplArrow;
    private ImageView finArrow;
    private ImageView pStayArrow;
    private ImageView impArrow;

    private TextView genContentPt1;
    private TextView genContentPt2;
    private TextView addContent;
    private TextView emplContent;
    private TextView finContent;
    private TextView pStayContent;
    private TextView impContent;

    private Button requirements;

    private String titleString;
    private int genStringPt1Id;
    private int genStringPt2Id;
    private int addStringId;
    private int emplStringId;
    private int finStringId;
    private int stayStringId;
    private int infoStringId;

    private Activity context;

    private Boolean isWaitingClick = false;
    private int lastViewId = -1;

    private ScrollView scrollContainer;
    private FloatingActionButton toTheTop;

    private Handler h;
    private Timer timer;

    private FloatingActionButton backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.subcategory_c, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        toTheTop = (FloatingActionButton) getActivity().findViewById(R.id.to_the_top_button);

        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if (backButton.isHiden()) {
            backButton.showAnim(100);
        }

        scrollContainer = (ScrollView) getActivity().findViewById(R.id.c_category_scroll);

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

        genDocs = context.findViewById(R.id.c_general_docs);
        addDocs = context.findViewById(R.id.c_additional_docs);
        emplDocs = context.findViewById(R.id.c_employment_docs);
        finDocs = context.findViewById(R.id.c_finance_docs);
        pStay = context.findViewById(R.id.c_purpose_to_stay);
        impInfo = context.findViewById(R.id.c_important_info);

        genArrow = (ImageView) context.findViewById(R.id.c_gen_docs_arrow);
        addArrow = (ImageView) context.findViewById(R.id.c_add_docs_arrow);
        emplArrow = (ImageView) context.findViewById(R.id.c_empl_docs_arrow);
        finArrow = (ImageView) context.findViewById(R.id.c_fin_docs_arrow);
        pStayArrow = (ImageView) context.findViewById(R.id.c_stay_arrow);
        impArrow = (ImageView) context.findViewById(R.id.c_info_arrow);

        genContentPt1 = (TextView) context.findViewById(R.id.c_gen_docs_pt1);
        genContentPt2 = (TextView) context.findViewById(R.id.c_gen_docs_pt2);
        addContent = (TextView) context.findViewById(R.id.c_add_docs_content);
        emplContent = (TextView) context.findViewById(R.id.c_empl_docs_content);
        finContent = (TextView) context.findViewById(R.id.c_fin_docs_content);
        pStayContent = (TextView) context.findViewById(R.id.c_stay_content);
        impContent = (TextView) context.findViewById(R.id.c_info_content);

        genDev = context.findViewById(R.id.gen_dev);
        addDev = context.findViewById(R.id.add_dev);
        emplDev = context.findViewById(R.id.empl_dev);
        finDev = context.findViewById(R.id.fin_dev);
        stayDev = context.findViewById(R.id.stay_dev);
        impDev = context.findViewById(R.id.info_dev);

        requirements = (Button) context.findViewById(R.id.c_photo_cond);

        genTitle = (TextView) context.findViewById(R.id.c_gen_docs_title);
        addTitle = (TextView) context.findViewById(R.id.c_add_docs_title);
        emplTitle = (TextView) context.findViewById(R.id.c_empl_docs_title);
        finTitle = (TextView) context.findViewById(R.id.c_fin_docs_title);
        pStayTitle = (TextView) context.findViewById(R.id.c_stay_title);
        impTitle = (TextView) context.findViewById(R.id.c_info_title);

        genContentPt1.setVisibility(View.GONE);
        genContentPt2.setVisibility(View.GONE);
        addContent.setVisibility(View.GONE);
        emplContent.setVisibility(View.GONE);
        finContent.setVisibility(View.GONE);
        pStayContent.setVisibility(View.GONE);
        impContent.setVisibility(View.GONE);
        requirements.setVisibility(View.GONE);
        impInfo.setVisibility(View.GONE);

        genDev.setVisibility(View.GONE);
        addDev.setVisibility(View.GONE);
        emplDev.setVisibility(View.GONE);
        finDev.setVisibility(View.GONE);
        stayDev.setVisibility(View.GONE);
        impDev.setVisibility(View.GONE);

        Typeface light = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");

        genTitle.setTypeface(light);
        addTitle.setTypeface(light);
        emplTitle.setTypeface(light);
        finTitle.setTypeface(light);
        pStayTitle.setTypeface(light);
        impTitle.setTypeface(light);

        genContentPt1.setTypeface(light);
        genContentPt2.setTypeface(light);
        addContent.setTypeface(light);
        emplContent.setTypeface(light);
        finContent.setTypeface(light);
        pStayContent.setTypeface(light);
        impContent.setTypeface(light);
        requirements.setTypeface(light);

        title = (TextView) getActivity().findViewById(R.id.c_sub_title);
        title.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf"));

        switch (CategoryCFragment.currentSubCategory) {
            case C_TOURISM:
                titleString = getString(R.string.tur_visa);
                genStringPt1Id = R.string.tourism_gen_content_pt1;
                genStringPt2Id = R.string.tourism_gen_content_pt2;
                addStringId = R.string.tourism_add_content;
                emplStringId = R.string.tourism_empl_content;
                finStringId = R.string.tourism_fin_content;
                stayStringId = R.string.tourism_purpose_content;
                break;
            case C_BUSINESS:
                titleString = getString(R.string.bus_visa);
                genStringPt1Id = R.string.business_gen_content_pt1;
                genStringPt2Id = R.string.business_gen_content_pt2;
                addStringId = R.string.business_add_content;
                emplStringId = R.string.business_empl_content;
                finStringId = R.string.business_fin_content;
                stayStringId = R.string.business_purpose_content;
                break;
            case C_FAMILY:
                titleString = getString(R.string.fam_visa);
                genStringPt1Id = R.string.family_gen_content_pt1;
                genStringPt2Id = R.string.family_gen_content_pt2;
                addStringId = R.string.family_add_content;
                emplStringId = R.string.family_empl_content;
                finStringId = R.string.family_fin_content;
                stayStringId = R.string.family_purpose_content;
                break;
            case C_SPORT:
                titleString = getString(R.string.sport_visa);
                genStringPt1Id = R.string.sport_gen_content_pt1;
                genStringPt2Id = R.string.sport_gen_content_pt2;
                addStringId = R.string.sport_add_content;
                emplStringId = R.string.sport_empl_content;
                finStringId = R.string.sport_fin_content;
                stayStringId = R.string.sport_purpose_content;
                break;
            case C_STUDENT:
                titleString = getString(R.string.stud_visa);
                genStringPt1Id = R.string.student_gen_content_pt1;
                genStringPt2Id = R.string.student_gen_content_pt2;
                addStringId = R.string.student_add_content;
                emplStringId = R.string.student_empl_content;
                finStringId = R.string.student_fin_content;
                stayStringId = R.string.student_purpose_content;
                break;
            case C_MED:
                titleString = getString(R.string.med_visa);
                genStringPt1Id = R.string.med_gen_content_pt1;
                genStringPt2Id = R.string.med_gen_content_pt2;
                addStringId = R.string.med_add_content;
                emplStringId = R.string.med_empl_content;
                finStringId = R.string.med_fin_content;
                stayStringId = R.string.med_purpose_content;
                break;
            case C_PRESS:
                titleString = getString(R.string.jurn_visa);
                genStringPt1Id = R.string.press_gen_content_pt1;
                genStringPt2Id = R.string.press_gen_content_pt2;
                addStringId = R.string.press_add_content;
                emplStringId = R.string.press_empl_content;
                finStringId = R.string.press_fin_content;
                stayStringId = R.string.press_purpose_content;
                break;
            case C_TIR:
                titleString = getString(R.string.driv_visa);
                genStringPt1Id = R.string.tir_gen_content_pt1;
                genStringPt2Id = R.string.tir_gen_content_pt2;
                addStringId = R.string.tir_add_content;
                emplStringId = R.string.tir_empl_content;
                finStringId = R.string.tir_fin_content;
                stayStringId = R.string.tir_purpose_content;
                break;
            case D_NATIONAL:
                impInfo.setVisibility(View.VISIBLE);
                titleString = getString(R.string.visa_d);
                genStringPt1Id = R.string.national_gen_content_pt1;
                genStringPt2Id = R.string.national_gen_content_pt2;
                addStringId = R.string.national_add_content;
                emplStringId = R.string.national_empl_content;
                finStringId = R.string.national_fin_content;
                stayStringId = R.string.national_purpose_content;
                infoStringId = R.string.national_info_content;
                break;
            default:
                titleString = "Default";
                genDocs.setVisibility(View.GONE);
                addDocs.setVisibility(View.GONE);
                emplDocs.setVisibility(View.GONE);
                finDocs.setVisibility(View.GONE);
                pStay.setVisibility(View.GONE);
                impInfo.setVisibility(View.GONE);
                break;
        }
        title.setText(titleString);
        genDocs.setOnClickListener(this);
        addDocs.setOnClickListener(this);
        emplDocs.setOnClickListener(this);
        finDocs.setOnClickListener(this);
        pStay.setOnClickListener(this);
        impInfo.setOnClickListener(this);
        requirements.setOnClickListener(this);
        toTheTop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.c_general_docs:
                if (genContentPt1.getVisibility() == View.GONE) {
                    genArrow.setImageResource(R.drawable.ic_arrow_up);
                    genContentPt1.setText(Html.fromHtml(getString(genStringPt1Id)));
                    genContentPt2.setText(Html.fromHtml(getString(genStringPt2Id)));
                    genDev.setVisibility(View.VISIBLE);
                    genContentPt1.setVisibility(View.VISIBLE);
                    requirements.setVisibility(View.VISIBLE);
                    genContentPt2.setVisibility(View.VISIBLE);
                } else if (isWaitingClick && id == lastViewId) {
                    isWaitingClick = false;
                    hideViews(genContentPt1, genContentPt2, requirements, genDev);
                    genArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    isWaitingClick = true;
                    new Thread(new DoubleClickThread()).start();
                }
                break;
            case R.id.c_additional_docs:
                if (addContent.getVisibility() == View.GONE) {
                    addArrow.setImageResource(R.drawable.ic_arrow_up);
                    addContent.setText(Html.fromHtml(getString(addStringId)));
                    addContent.setVisibility(View.VISIBLE);
                    addDev.setVisibility(View.VISIBLE);
                } else if (isWaitingClick && id == lastViewId) {
                    isWaitingClick = false;
                    hideViews(addContent, addDev);
                    addArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    isWaitingClick = true;
                    new Thread(new DoubleClickThread()).start();
                }
                break;
            case R.id.c_employment_docs:
                if (emplContent.getVisibility() == View.GONE) {
                    emplArrow.setImageResource(R.drawable.ic_arrow_up);
                    emplContent.setText(Html.fromHtml(getString(emplStringId)));
                    emplContent.setVisibility(View.VISIBLE);
                    emplDev.setVisibility(View.VISIBLE);
                } else if (isWaitingClick && id == lastViewId) {
                    isWaitingClick = false;
                    hideViews(emplContent, emplDev);
                    emplArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    isWaitingClick = true;
                    new Thread(new DoubleClickThread()).start();
                }
                break;
            case R.id.c_finance_docs:
                if (finContent.getVisibility() == View.GONE) {
                    finArrow.setImageResource(R.drawable.ic_arrow_up);
                    finContent.setText(Html.fromHtml(getString(finStringId)));
                    finContent.setVisibility(View.VISIBLE);
                    finDev.setVisibility(View.VISIBLE);
                } else if (isWaitingClick && id == lastViewId) {
                    isWaitingClick = false;
                    hideViews(finContent, finDev);
                    finArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    isWaitingClick = true;
                    new Thread(new DoubleClickThread()).start();
                }
                break;
            case R.id.c_purpose_to_stay:
                if (pStayContent.getVisibility() == View.GONE) {
                    pStayArrow.setImageResource(R.drawable.ic_arrow_up);
                    pStayContent.setText(Html.fromHtml(getString(stayStringId)));
                    pStayContent.setVisibility(View.VISIBLE);
                    stayDev.setVisibility(View.VISIBLE);
                } else if (isWaitingClick && id == lastViewId) {
                    isWaitingClick = false;
                    hideViews(pStayContent, stayDev);
                    pStayArrow.setImageResource(R.drawable.ic_arrow_down);
                } else {
                    isWaitingClick = true;
                    new Thread(new DoubleClickThread()).start();
                }
                break;
            case R.id.c_important_info:
                if (CategoryCFragment.currentSubCategory == SubCategoryFragment.D_NATIONAL) {
                    if (impContent.getVisibility() == View.GONE) {
                        impArrow.setImageResource(R.drawable.ic_arrow_up);
                        impContent.setText(Html.fromHtml(getString(infoStringId)));
                        impContent.setVisibility(View.VISIBLE);
                        impDev.setVisibility(View.VISIBLE);
                    } else if (isWaitingClick && id == lastViewId) {
                        isWaitingClick = false;
                        hideViews(impContent, impDev);
                        impArrow.setImageResource(R.drawable.ic_arrow_down);
                    } else {
                        isWaitingClick = true;
                        new Thread(new DoubleClickThread()).start();
                    }
                }
                break;
            case R.id.c_photo_cond:
                copyReadAssets();
                break;
            case R.id.to_the_top_button:
                scrollContainer.fullScroll(View.FOCUS_UP);
                break;
        }
        lastViewId = id;
    }

    private void hideViews(View... views) {
        for (View v : views) {
            v.setVisibility(View.GONE);
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


    private void copyReadAssets() {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(context.getFilesDir(), "Photo_requirements.pdf");
        try {
            in = assetManager.open("Photo_requirements.pdf");
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
                Uri.parse("file://" + context.getFilesDir() + "/Photo_requirements.pdf"),
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timer.cancel();
        if (!toTheTop.isHiden())
            toTheTop.hideAnim(100);
    }
}
