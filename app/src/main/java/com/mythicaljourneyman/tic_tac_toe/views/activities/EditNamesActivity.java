package com.mythicaljourneyman.tic_tac_toe.views.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.mythicaljourneyman.tic_tac_toe.R;
import com.mythicaljourneyman.tic_tac_toe.databinding.ActivityEditNamesBinding;
import com.mythicaljourneyman.tic_tac_toe.preferences.AppPreferences;

public class EditNamesActivity extends AppCompatActivity {
    ActivityEditNamesBinding mBinding;
    private boolean shouldSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_names);
        mBinding.player1Name.setText(AppPreferences.getPlayer1Name(this));
        mBinding.player2Name.setText(AppPreferences.getPlayer2Name(this));

        final String player1Symbol = AppPreferences.getPlayer1Symbol(this);
        final String player2Symbol = AppPreferences.getPlayer2Symbol(this);
        mBinding.player1Symbol.setText(player1Symbol);
        mBinding.player2Symbol.setText(player2Symbol);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.player1Symbol.getText().equals("X")) {

                    mBinding.player1Symbol.setText("O");
                    AppPreferences.setPlayer1Symbol(EditNamesActivity.this, "O");

                    mBinding.player2Symbol.setText("X");
                    AppPreferences.setPlayer2Symbol(EditNamesActivity.this, "X");
                } else {

                    mBinding.player1Symbol.setText("X");
                    AppPreferences.setPlayer1Symbol(EditNamesActivity.this, "X");

                    mBinding.player2Symbol.setText("O");
                    AppPreferences.setPlayer2Symbol(EditNamesActivity.this, "O");
                }
            }
        };

        mBinding.player1Symbol.setOnClickListener(onClickListener);

        mBinding.player1Symbol.setOnClickListener(onClickListener);
        mBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldSave = false;
                finish();
            }
        });
        mBinding.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldSave = true;
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shouldSave) {
            Editable editable1 = mBinding.player1Name.getEditableText();
            if (editable1 != null) {
                String name1 = editable1.toString().trim();
                if (!TextUtils.isEmpty(name1)) {
                    AppPreferences.setPlayer1Name(this, name1);

                }
            }

            Editable editable2 = mBinding.player2Name.getEditableText();
            if (editable2 != null) {
                String name2 = editable2.toString().trim();
                if (!TextUtils.isEmpty(name2)) {
                    AppPreferences.setPlayer2Name(this, name2);

                }
            }
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, EditNamesActivity.class);
        return intent;

    }
}
