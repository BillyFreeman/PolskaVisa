package com.victor.polskavisa;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryCFragment extends Fragment {

    public static int currentSubCategory;

    private ArrayList<ListItemInterface> visaTypes = new ArrayList<>();
    private RecyclerView cList;
    private RecyclerView.LayoutManager manager;
    private PVAdapterShort adapter;

    private FragmentTransaction ft;

    private Typeface headingFont;
    private Typeface subFont;
    private ArrayList<Typeface> fontList = new ArrayList<>();

    private FloatingActionButton backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.category_c_fragment, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        headingFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf");
        subFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");


        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if(backButton.isHiden()){
            backButton.showAnim(100);
        }

        if (visaTypes.size() == 0) {
            initList();
        }

        cList = (RecyclerView) getActivity().findViewById(R.id.c_types_list);
        manager = new LinearLayoutManager(getActivity());
        cList.setLayoutManager(manager);
        adapter = new PVAdapterShort(visaTypes, fontList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ft = getActivity().getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_left, R.animator.slide_right, R.animator.slide_left_back, R.animator.slide_right_back);
                switch (position) {
                    case 1:
                        currentSubCategory = SubCategoryFragment.C_TOURISM;
                        break;
                    case 2:
                        currentSubCategory = SubCategoryFragment.C_BUSINESS;
                        break;
                    case 3:
                        currentSubCategory = SubCategoryFragment.C_FAMILY;
                        break;
                    case 4:
                        currentSubCategory = SubCategoryFragment.C_SPORT;
                        break;
                    case 5:
                        currentSubCategory = SubCategoryFragment.C_STUDENT;
                        break;
                    case 6:
                        currentSubCategory = SubCategoryFragment.C_MED;
                        break;
                    case 7:
                        currentSubCategory = SubCategoryFragment.C_PRESS;
                        break;
                    case 8:
                        currentSubCategory = SubCategoryFragment.C_TIR;
                        break;
                }
                ft.replace(R.id.main_container, new SubCategoryFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        cList.setAdapter(adapter);
        cList.setHasFixedSize(true);
    }

    private void initList() {
        String heading = getActivity().getResources().getString(R.string.visa_c);
        String tur = getActivity().getResources().getString(R.string.tur_visa);
        String busn = getActivity().getResources().getString(R.string.bus_visa);
        String fam = getActivity().getResources().getString(R.string.fam_visa);
        String sport = getActivity().getResources().getString(R.string.sport_visa);
        String stud = getActivity().getResources().getString(R.string.stud_visa);
        String med = getActivity().getResources().getString(R.string.med_visa);
        String jurn = getActivity().getResources().getString(R.string.jurn_visa);
        String driv = getActivity().getResources().getString(R.string.driv_visa);

        visaTypes.add(new ListItemObject(-1, heading, "", -1, true));
        visaTypes.add(new ListItemObject(R.drawable.tourism_icon, tur, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.busines_icon, busn, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.family_icon, fam, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.sport_icon, sport, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.study_icon, stud, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.med_icon, med, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.journalist_icon, jurn, "", R.drawable.ic_arrow_left, false));
        visaTypes.add(new ListItemObject(R.drawable.driver_icon, driv, "", R.drawable.ic_arrow_left, false));

        fontList.add(headingFont);
        fontList.add(subFont);
    }
}
