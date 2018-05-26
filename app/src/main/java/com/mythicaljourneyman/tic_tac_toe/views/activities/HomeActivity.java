package com.mythicaljourneyman.tic_tac_toe.views.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mythicaljourneyman.tic_tac_toe.R;
import com.mythicaljourneyman.tic_tac_toe.databinding.ActivityHomeBinding;
import com.mythicaljourneyman.tic_tac_toe.preferences.AppPreferences;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding mBinding;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        mBinding.twoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(GameActivity.getStartIntentforTwoPlayer(HomeActivity.this, AppPreferences.getPlayer1Name(HomeActivity.this), AppPreferences.getPlayer2Name(HomeActivity.this), AppPreferences.getPlayer1Symbol(HomeActivity.this), AppPreferences.getPlayer2Symbol(HomeActivity.this)));
            }
        });
        mBinding.chooseNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditNamesActivity.getStartIntent(HomeActivity.this));
            }
        });
    }
}
