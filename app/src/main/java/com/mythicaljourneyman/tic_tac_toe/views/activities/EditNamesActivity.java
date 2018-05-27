package com.mythicaljourneyman.tic_tac_toe.views.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mythicaljourneyman.tic_tac_toe.R;
import com.mythicaljourneyman.tic_tac_toe.databinding.ActivityEditNamesBinding;
import com.mythicaljourneyman.tic_tac_toe.databinding.LayoutSymbolChooserBinding;
import com.mythicaljourneyman.tic_tac_toe.databinding.LayoutSymbolChooserItemBinding;
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
        setPlayer1Symbol(player1Symbol);
        setPlayer2Symbol(player2Symbol);

        final String[] icons = getResources().getStringArray(R.array.icons);
        int player1SymbolPosition = -1;
        int player2SymbolPosition = -1;
        for (int i = 0; i < icons.length; i++) {
            if (icons[i].equals(player1Symbol)) {
                player1SymbolPosition = i;
            }
            if (icons[i].equals(player2Symbol)) {
                player2SymbolPosition = i;
            }

            if (player1SymbolPosition != -1 && player2SymbolPosition != -1) {
                break;
            }
        }

        if (player1SymbolPosition == -1) {
            player1SymbolPosition = 0;
        }
        if (player2SymbolPosition != -1) {
            player2SymbolPosition = 1;
        }

        final int finalPlayer1SymbolPosition = player1SymbolPosition;

        mBinding.player1Symbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbolClickAction(icons, finalPlayer1SymbolPosition, true);
            }
        });
        mBinding.player1Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbolClickAction(icons, finalPlayer1SymbolPosition, true);
            }
        });


        final int finalPlayer2SymbolPosition = player2SymbolPosition;
        mBinding.player2Symbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbolClickAction(icons, finalPlayer2SymbolPosition, false);
            }
        });
        mBinding.player2Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbolClickAction(icons, finalPlayer2SymbolPosition, false);
            }
        });

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

    private void symbolClickAction(final String[] icons, int position, final boolean isPlayer1) {

        LayoutSymbolChooserBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layout_symbol_chooser, null, false);

        AlertDialog alertDialog = new AlertDialog.Builder(EditNamesActivity.this)
                .setTitle("Choose Symbol")
                .setView(binding.getRoot())
                .create();
        binding.list.setAdapter(new SymbolAdapter(icons, position, isPlayer1, alertDialog));
        binding.list.setLayoutManager(new GridLayoutManager(this, 2));
        alertDialog.show();
    }

    private void setPlayer1Symbol(String symbol) {
        if (symbol.equalsIgnoreCase("X") || symbol.equalsIgnoreCase("O")) {
            mBinding.player1Symbol.setText(symbol);
            mBinding.player1Symbol.setVisibility(View.VISIBLE);
            mBinding.player1Icon.setVisibility(View.INVISIBLE);
        } else {
            mBinding.player1Icon.setText(symbol);
            mBinding.player1Symbol.setVisibility(View.INVISIBLE);
            mBinding.player1Icon.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayer2Symbol(String symbol) {
        if (symbol.equalsIgnoreCase("X") || symbol.equalsIgnoreCase("O")) {
            mBinding.player2Symbol.setText(symbol);
            mBinding.player2Symbol.setVisibility(View.VISIBLE);
            mBinding.player2Icon.setVisibility(View.INVISIBLE);
        } else {
            mBinding.player2Icon.setText(symbol);
            mBinding.player2Symbol.setVisibility(View.INVISIBLE);
            mBinding.player2Icon.setVisibility(View.VISIBLE);
        }
    }

    class SymbolAdapter extends RecyclerView.Adapter<SymbolAdapter.SymbolViewHolder> {
        private String[] mList;
        private int mCurrentItem = 0;
        private boolean isPlayer1;
        private AlertDialog mAlertDialog;

        public SymbolAdapter(String[] list, int currentItem, boolean isPlayer1, AlertDialog alertDialog) {
            mList = list;
            mCurrentItem = currentItem;
            this.isPlayer1 = isPlayer1;
            this.mAlertDialog = alertDialog;
        }

        @NonNull
        @Override
        public SymbolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutSymbolChooserItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_symbol_chooser_item, parent, false);
            return new SymbolViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull SymbolViewHolder holder, int position) {
            final String item = mList[position];
            holder.mBinding.textView.setText(item);
            holder.mBinding.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPlayer1) {
                        setPlayer1Symbol(item);
                        AppPreferences.setPlayer1Symbol(EditNamesActivity.this, item);
                    } else {
                        setPlayer2Symbol(item);
                        AppPreferences.setPlayer2Symbol(EditNamesActivity.this, item);

                    }
                    mAlertDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.length;
        }

        class SymbolViewHolder extends RecyclerView.ViewHolder {
            LayoutSymbolChooserItemBinding mBinding;

            SymbolViewHolder(LayoutSymbolChooserItemBinding itemBinding) {
                super(itemBinding.getRoot());
                mBinding = itemBinding;
            }
        }
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
