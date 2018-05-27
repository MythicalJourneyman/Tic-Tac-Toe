package com.mythicaljourneyman.tic_tac_toe.views.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mythicaljourneyman.tic_tac_toe.R;
import com.mythicaljourneyman.tic_tac_toe.databinding.ActivityHomeBinding;
import com.mythicaljourneyman.tic_tac_toe.preferences.AppPreferences;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding mBinding;
    private int mGridSize = 3;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        mGridSize = AppPreferences.getGridSize(this);
        final int color = ContextCompat.getColor(this, R.color.colorX);
        final int colorPlain = Color.WHITE;

        setColorForGrid(mGridSize, color, colorPlain);

        mBinding.threeGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreferences.setGridSize(HomeActivity.this, 3);
                mGridSize = 3;
                setColorForGrid(3, color, colorPlain);
            }
        });

        mBinding.fourGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGridSize = 4;
                AppPreferences.setGridSize(HomeActivity.this, 4);
                setColorForGrid(4, color, colorPlain);
            }
        });


        mBinding.twoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGridSize == 3) {
                    startActivity(GameActivity.getStartIntentForTwoPlayer3(HomeActivity.this, AppPreferences.getPlayer1Name(HomeActivity.this), AppPreferences.getPlayer2Name(HomeActivity.this), AppPreferences.getPlayer1Symbol(HomeActivity.this), AppPreferences.getPlayer2Symbol(HomeActivity.this)));
                } else {
                    startActivity(GameActivity.getStartIntentForTwoPlayer4(HomeActivity.this, AppPreferences.getPlayer1Name(HomeActivity.this), AppPreferences.getPlayer2Name(HomeActivity.this), AppPreferences.getPlayer1Symbol(HomeActivity.this), AppPreferences.getPlayer2Symbol(HomeActivity.this)));

                }
            }
        });

        mBinding.singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGridSize == 3) {
                    startActivity(GameActivity.getStartIntentForSinglePlayer3(HomeActivity.this, AppPreferences.getPlayer1Name(HomeActivity.this), AppPreferences.getPlayer1Symbol(HomeActivity.this), AppPreferences.getPlayer2Symbol(HomeActivity.this)));
                } else {
                    startActivity(GameActivity.getStartIntentForSinglePlayer4(HomeActivity.this, AppPreferences.getPlayer1Name(HomeActivity.this), AppPreferences.getPlayer1Symbol(HomeActivity.this), AppPreferences.getPlayer2Symbol(HomeActivity.this)));

                }
            }
        });

        mBinding.chooseNames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditNamesActivity.getStartIntent(HomeActivity.this));
            }
        });
    }

    private void setColorForGrid(int gridSize, int color, int colorPlain) {
        if (gridSize == 3) {
            mBinding.threeGrid.setTextColor(color);
            mBinding.fourGrid.setTextColor(colorPlain);
        } else if (gridSize == 4) {
            mBinding.fourGrid.setTextColor(color);
            mBinding.threeGrid.setTextColor(colorPlain);
        }
    }
}
