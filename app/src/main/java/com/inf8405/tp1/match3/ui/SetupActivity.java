package com.inf8405.tp1.match3.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inf8405.tp1.match3.R;
import com.inf8405.tp1.match3.model.Game;

/**
 * Created by Lam on 1/28/2017.
 */

public class SetupActivity extends AbstractBaseActivity {

    private TextView rulesMsg;
    private LinearLayout.LayoutParams layoutParams;
    private LinearLayout containerLayout;
    private PopupWindow popUpWindow;
    private LinearLayout setupLevelLayout;
    private final int NB_LEVEL = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int levelAllowed = Game.getInstance().getGameLevel();
        setContentView(R.layout.level_menu);
        setupLevelLayout = (LinearLayout)findViewById(R.id.level);
        // Create a popUpWindow to signal the user
        popUpWindow = new PopupWindow(this);
        containerLayout = new LinearLayout(this);
        rulesMsg = new TextView(this);
        rulesMsg.setText(setupLevelLayout.getResources().getString(R.string.allowed_level) + " " + levelAllowed);
        rulesMsg.setBackgroundColor(Color.WHITE);

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.addView(rulesMsg, layoutParams);
        popUpWindow.setContentView(containerLayout);

        for(int i = 1; i <= NB_LEVEL; ++i){
            // Create a button dyanamically with Niveau + level as text
            Button btn = new Button(this);
            btn.setText("Niveau "+i);
            LinearLayout.LayoutParams layoutParamsBtn = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParamsBtn.weight = 1;
            int padding_in_dp = 2;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            btn.setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
            btn.setLayoutParams(layoutParamsBtn);
            btn.setDuplicateParentStateEnabled(true);
            btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Button btn = (Button)v;
                    Intent intent = new Intent(SetupActivity.this, GridActivity.class);
                    int levelAllowed = Game.getInstance().getGameLevel();
                    int btnLevel = Integer.valueOf(btn.getText().toString().substring(btn.getText().length() - 1));
                    if(btnLevel <= levelAllowed){
                        intent.putExtra("level", btnLevel);
                        popUpWindow.dismiss();
                        startActivity(intent);
                    }
                    else {
                        // Popup a window to signal the player of wrong prerequisis
                        popUpWindow.showAtLocation(setupLevelLayout, Gravity.CENTER, 0, 0);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                // close your dialog
                                popUpWindow.dismiss();
                            }

                        }, 3000);

                    }
                }
            });
            Drawable bgShape = btn.getBackground();
            bgShape.setAlpha(32);
            btn.setBackground(bgShape);
            btn.setTag(i);
            setupLevelLayout.addView(btn);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        int levelAllowed = Game.getInstance().getGameLevel();
        for (int i = 1; i <= levelAllowed; ++i){
            setupLevelLayout.findViewWithTag(i).getBackground().setAlpha(255);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Close or back button
            case R.id.action_close:
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupOnClick(View v){
        popUpWindow.dismiss();
    }
}
