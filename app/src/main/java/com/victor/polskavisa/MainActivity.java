package com.victor.polskavisa;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements View.OnClickListener {

    private MainFragment mainFragment;
    private FragmentTransaction fragmentTransaction;

    private FloatingActionButton stepBack;
    private FloatingActionButton toTheTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.relative_container);

        stepBack = new FloatingActionButton(this, getResources().getDrawable(R.drawable.ic_arrow_back), getResources().getColor(R.color.tool_bar_color));
        stepBack.setId(R.id.toolbar_back_button);
        stepBack.setAlign(RelativeLayout.ALIGN_PARENT_TOP);
        stepBack.setAlign(RelativeLayout.ALIGN_PARENT_LEFT);
        stepBack.setSize(64);
        stepBack.create(rl);
        if (stepBack.isHiden())
            stepBack.hideAnim(0);
        stepBack.setOnClickListener(this);

        toTheTop = new FloatingActionButton(this, getResources().getDrawable(R.drawable.ic_arrow_to_the_top), getResources().getColor(R.color.tool_bar_color));
        toTheTop.setId(R.id.to_the_top_button);
        toTheTop.setAlign(RelativeLayout.ALIGN_PARENT_BOTTOM);
        toTheTop.setAlign(RelativeLayout.ALIGN_PARENT_RIGHT);
        toTheTop.setSize(64);
        toTheTop.create(rl);
        if(toTheTop.isHiden()){
            toTheTop.hideAnim(0);
        }

        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_container, mainFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_button:
                if (getFragmentManager().getBackStackEntryCount() > 0)
                    getFragmentManager().popBackStack();
                break;
            default:
                break;
        }
    }
}
