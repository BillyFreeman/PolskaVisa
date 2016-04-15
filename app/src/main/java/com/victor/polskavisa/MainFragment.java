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

import java.util.ArrayList;

public class MainFragment extends Fragment {
    private ArrayList<ListItemInterface> elements = new ArrayList<>();

    private RecyclerView mainList;
    private PVAdapter adapter;
    private RecyclerView.LayoutManager manager;

    private FragmentTransaction ft;

    private Typeface headingFont;
    private Typeface subFont;
    private ArrayList<Typeface> fontList = new ArrayList<>();

    private FloatingActionButton backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.menu_fragment, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainList = (RecyclerView) getActivity().findViewById(R.id.main_list);
        mainList.setHasFixedSize(true);

        backButton = (FloatingActionButton) getActivity().findViewById(R.id.toolbar_back_button);
        if(!backButton.isHiden()){
            backButton.hideAnim(100);
        }

        headingFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/halvetica_light.otf");
        subFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-CondLight.ttf");

        if (elements.size() == 0) {
            initList();
        }

        manager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(manager);

        adapter = new PVAdapter(this.elements, this.fontList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ft = getActivity().getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_left, R.animator.slide_right, R.animator.slide_left_back, R.animator.slide_right_back);
                switch (position) {
                    case 1:
                        ft.replace(R.id.main_container, new CategoryCFragment());
                        break;
                    case 2:
                        CategoryCFragment.currentSubCategory = SubCategoryFragment.D_NATIONAL;
                        ft.replace(R.id.main_container, new SubCategoryFragment());
                        break;
                    case 4:
                        ft.replace(R.id.main_container, new ContactsFragment());
                        break;
                    case 5:
                        ft.replace(R.id.main_container, new ConsulFragment());
                        break;
                    case 6:
                        ft.replace(R.id.main_container, new ProcessFragment());
                        break;
                    case 7:
                        ft.replace(R.id.main_container, new PriceListFragment());
                        break;
                    case 8:
                        ft.replace(R.id.main_container, new BioFragment());
                        break;
                    case 9:
                        ft.replace(R.id.main_container, new ImpInfoFragment());
                        break;
                    case 10:
                        ft.replace(R.id.main_container, new DocsFragment());
                        break;
                }
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        mainList.setAdapter(adapter);
        mainList.setHasFixedSize(true);
    }

    private void initList() {
        String cat_title = getActivity().getResources().getString(R.string.visa_category);
        String gen_title = getActivity().getResources().getString(R.string.general_info);
        String c_cat = getActivity().getResources().getString(R.string.c_category);
        String c_cat_d = getActivity().getResources().getString(R.string.c_category_detail);
        String d_cat = getActivity().getResources().getString(R.string.d_category);
        String d_cat_d = getActivity().getResources().getString(R.string.d_category_detail);
        String cont = getActivity().getResources().getString(R.string.contacts);
        String cont_d = getActivity().getResources().getString(R.string.contact_detail);
        String cons = getActivity().getResources().getString(R.string.consul);
        String cons_d = getActivity().getResources().getString(R.string.consul_detail);
        String tutor = getActivity().getResources().getString(R.string.step_by_step);
        String tutor_d = getActivity().getResources().getString(R.string.step_by_step_detail);
        String price = getActivity().getResources().getString(R.string.price_list);
        String price_d = getActivity().getResources().getString(R.string.price_list_detail);
        String bio = getActivity().getResources().getString(R.string.biomet);
        String bio_d = getActivity().getResources().getString(R.string.biomet_detail);
        String gen = getActivity().getResources().getString(R.string.important);
        String gen_d = getActivity().getResources().getString(R.string.important_detail);
        String docs = getActivity().getResources().getString(R.string.docs);
        String docs_d = getActivity().getResources().getString(R.string.docs_detail);


        elements.add(new ListItemObject(-1, cat_title, null, -1, true));
        elements.add(new ListItemObject(R.drawable.c_category_icon, c_cat, c_cat_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.d_category_icon, d_cat, d_cat_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(-1, gen_title, null, -1, true));
        elements.add(new ListItemObject(R.drawable.adress_icon, cont, cont_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.consul_icon, cons, cons_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.step_icon, tutor, tutor_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.pay_icon, price, price_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.biometrical_icon, bio, bio_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.general_icon, gen, gen_d, R.drawable.ic_arrow_left, false));
        elements.add(new ListItemObject(R.drawable.doc_icon, docs, docs_d, R.drawable.ic_arrow_left, false));

        fontList.add(headingFont);
        fontList.add(subFont);
    }
}
