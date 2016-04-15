package com.victor.polskavisa;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomThreePosAdapter extends BaseAdapter {

    ArrayList<ListItemInterface> objects;
    Context context;
    LayoutInflater inflater;

    public CustomThreePosAdapter(Context context, ArrayList<ListItemInterface> objects) {
        this.context = context;
        this.objects = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View newView = view;
        if (newView == null) {
            newView = inflater.inflate(R.layout.three_possition_row_layout, viewGroup, false);
        }

        ListItemObject object = getObject(i);

        ((ImageView) newView.findViewById(R.id.three_p_list_image)).setImageResource(object.getPicture());
        ((TextView) newView.findViewById(R.id.three_p_list_item_title)).setText(object.getTitle());
        ((TextView) newView.findViewById(R.id.three_p_list_item_title)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/PoiretOne-Regular.ttf"));
        ((ImageView) newView.findViewById(R.id.three_p_list_act_image)).setImageResource(object.getActPicture());

        return newView;
    }

    ListItemObject getObject(int i) {
        return ((ListItemObject) getItem(i));
    }
}
