package com.mythicaljourneyman.tic_tac_toe.views.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mythicaljourneyman.tic_tac_toe.R;
import com.mythicaljourneyman.tic_tac_toe.databinding.ActivityGameBinding;
import com.mythicaljourneyman.tic_tac_toe.databinding.LayoutGameItemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GameActivity extends AppCompatActivity {
    private final static String PLAYER_1_NAME = "player_1_name";
    private final static String PLAYER_2_NAME = "player_2_name";
    private final static String PLAYER_1_SYMBOL = "player_1_symbol";
    private final static String PLAYER_2_SYMBOL = "player_2_symbol";
    private final static String GRID_SIZE = "grid";
    private int mGridSize = 3;

    /**
     * intent to start the game. It takes player name .
     *
     * @param context     context
     * @param player1Name name of the player 1
     * @param player2Name name of the player 2
     * @return intent
     */
    public static Intent getStartIntentForTwoPlayer(Context context, String player1Name, String player2Name, String player1Symbol, String player2Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_2_NAME, player2Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        intent.putExtra(PLAYER_2_SYMBOL, player2Symbol);
        return intent;
    }

    public static Intent getStartIntentForSinglePlayer(Context context, String player1Name, String player1Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        return intent;
    }

    private ActivityGameBinding mBinding;

    @Override
    public void onBackPressed() {
        //finish game on back press
        endGame();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_game);

        // get player names from intent
        String player1Name = getIntent().getStringExtra(PLAYER_1_NAME);
        String player2Name = getIntent().getStringExtra(PLAYER_2_NAME);
        String player1Symbol = getIntent().getStringExtra(PLAYER_1_SYMBOL);
        String player2Symbol = getIntent().getStringExtra(PLAYER_2_SYMBOL);
        boolean isComputer = false;

        if (TextUtils.isEmpty(player2Name)) {
            player2Name = "Computer";
            isComputer = true;
            if (player1Symbol.equals("X")) {
                player2Symbol = "O";
            } else {
                player2Symbol = "X";
            }
        }


        // get grid size from intent.
        // set default grid size to 4.
        mGridSize = getIntent().getIntExtra(GRID_SIZE, 3);

        initializeViews(player1Name, player2Name, player1Symbol, player2Symbol, isComputer);
    }

    private void initializeViews(String player1Name, String player2Name, String player1Symbol, String player2Symbol, boolean isComputer) {
        // set player names and symbols
        mBinding.player1Name.setText(player1Name);
        mBinding.player2Name.setText(player2Name);
        mBinding.player1Symbol.setText(player1Symbol);
        mBinding.player2Symbol.setText(player2Symbol);
        //finish game on back press
        mBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGame();
            }
        });

        // calculate total number of items for grid
        int totalItems = mGridSize * mGridSize;

        // prepare data for the grid
        ArrayList<Item> list = new ArrayList<>();
        for (int i = 0; i < totalItems; i++) {
            list.add(new Item(""));
        }

        int colorX = ContextCompat.getColor(this, R.color.colorX);
        int colorO = ContextCompat.getColor(this, R.color.colorO);

        // initialize layout manager and recycler view
        mBinding.list.setLayoutManager(new GridLayoutManager(this, mGridSize, GridLayoutManager.VERTICAL, false));
        mBinding.list.setHasFixedSize(true);
//        mBinding.list.setLayoutAnimation(new LayoutAnimationController(new AlphaAnimation(0,1)));
//        mBinding.list.setItemAnimator(new DefaultItemAnimator());
        mBinding.list.setAdapter(new ItemAdapter(list, isComputer, player1Name, player2Name, player1Symbol, player2Symbol, colorX, colorO));

        if (player1Symbol.equals("X")) {
            highlightPlayer(0, colorX, Color.WHITE);
        } else {
            highlightPlayer(0, colorO, Color.WHITE);
        }

    }

    private void highlightPlayer(int player, int color, int colorPlain) {
        if (player == 0) {
            mBinding.player1Symbol.setTextColor(color);
            mBinding.player2Symbol.setTextColor(colorPlain);
        } else {
            mBinding.player1Symbol.setTextColor(colorPlain);
            mBinding.player2Symbol.setTextColor(color);
        }
    }

    /**
     * finish game. save player score and exit.
     */
    private void endGame() {
        finish();
    }

    private void playClick() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.start();
    }

    class Item {
        Item(String value) {
            this.value = value;
        }

        boolean winner = false;
        boolean checked = false;
        String value;
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
        private ArrayList<Item> mList;

        String mCurrentPlayer = "X";
        String mPlayer2Symbol = "O";
        String mPlayer1Symbol = "X";
        String mPlayer1Name = "X";
        String mPlayer2Name = "O";
        boolean isComputer;
        HashSet<Integer> mEmptyPlaces = new HashSet<>();

        ItemAdapter(ArrayList<Item> list, boolean isComputer, String player1Name, String player2Name, String player1Symbol, String player2Symbol, int colorX, int colorO) {
            mList = list;
            this.isComputer = isComputer;
            mPlayer1Name = player1Name;
            mPlayer2Name = player2Name;
            mPlayer2Symbol = player2Symbol;
            mPlayer1Symbol = player1Symbol;
            mCurrentPlayer = player1Symbol;
            mColorX = colorX;
            mColorO = colorO;
            mEmptyPlaces.add(0);
            mEmptyPlaces.add(1);
            mEmptyPlaces.add(2);
            mEmptyPlaces.add(3);
            mEmptyPlaces.add(4);
            mEmptyPlaces.add(5);
            mEmptyPlaces.add(6);
            mEmptyPlaces.add(7);
            mEmptyPlaces.add(8);
        }


        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutGameItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_game_item, parent, false);
            return new ItemHolder(binding);
        }

        private int mColorX = 0xFF4CAF50;
        private int mColorO = 0xffffffff;
        private int mTileAnimationTimeInMilliSeconds = 250;

        @Override
        public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
            final Item item = mList.get(position);

            // if item is not paired
            if (!item.checked) {

                holder.mBinding.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isGameOver) {
                            updatePosition(holder, item, true);
                            if (!isGameOver && isComputer && mCurrentPlayer.equals(mPlayer2Symbol)) {
                                makeComputerMove(mPlayer2Symbol);
                            }
                        }
                    }
                });
            } else {
                if (item.winner) {
                    updatePosition(holder, item, false);
                } else if (!TextUtils.isEmpty(item.value)) {
                    updatePosition(holder, item, true);
                }
            }

        }

        private void makeComputerMove(String computerSymbol) {
            int position = -1;
            ArrayList<Integer> list = new ArrayList<>(mEmptyPlaces);
            Collections.shuffle(list);
            if (list.size() > 0) {
                position = list.get(0);
            }
            if (position > -1 && position < mList.size()) {
                mList.get(position).value = computerSymbol;
                mList.get(position).checked = true;
                mBinding.list.findViewHolderForAdapterPosition(position).itemView.performClick();
            }

        }

        private void updatePosition(ItemHolder holder, Item item, boolean isMove) {
            int position = holder.getAdapterPosition();

            if (isMove) {

                mTotalMoves++;
                mBinding.score.setText(String.valueOf(mTotalMoves));
                item.checked = true;
                mEmptyPlaces.remove(position);
                // set value
                mMoves[position] = mCurrentPlayer;

                holder.mBinding.container.setOnClickListener(null);
            }

            holder.mBinding.item.setText(mMoves[position]);

            if (isMove) {
                int[] score = checkScore();
                if (score[0] == 0) {
                    if (mPlayer1Symbol.equals("X")) {
                        Toast.makeText(GameActivity.this, "Winner is " + mPlayer1Name, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, "Winner is " + mPlayer2Name, Toast.LENGTH_SHORT).show();
                    }
                    isGameOver = true;
                    mList.get(score[3]).winner = true;
                    mList.get(score[1]).winner = true;
                    mList.get(score[2]).winner = true;
                    notifyItemChanged(score[3]);
                    notifyItemChanged(score[1]);
                    notifyItemChanged(score[2]);
                } else if (score[0] == 1) {
                    if (mPlayer1Symbol.equals("O")) {
                        Toast.makeText(GameActivity.this, "Winner is " + mPlayer1Name, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(GameActivity.this, "Winner is " + mPlayer2Name, Toast.LENGTH_SHORT).show();
                    }
                    isGameOver = true;
                    mList.get(score[3]).winner = true;
                    mList.get(score[1]).winner = true;
                    mList.get(score[2]).winner = true;
                    notifyItemChanged(score[3]);
                    notifyItemChanged(score[1]);
                    notifyItemChanged(score[2]);
                } else if (score[0] == 2) {
                    Toast.makeText(GameActivity.this, "DRAW", Toast.LENGTH_SHORT).show();
                    isGameOver = true;
                } else {

                }
            }

            if (mCurrentPlayer.equals("X")) {
                if (item.winner) {
                    holder.mBinding.item.setTextColor(Color.WHITE);
                    holder.mBinding.container.setCardBackgroundColor(mColorX);

                } else {
                    holder.mBinding.item.setTextColor(mColorX);

                }
                if (isMove && !isGameOver) {
                    mCurrentPlayer = "O";
                    if (mPlayer1Symbol.equals(mCurrentPlayer)) {
                        highlightPlayer(0, mColorO, Color.WHITE);
                    } else {
                        highlightPlayer(1, mColorO, Color.WHITE);
                    }
                }
            } else {
                if (item.winner) {
                    holder.mBinding.item.setTextColor(Color.WHITE);
                    holder.mBinding.container.setCardBackgroundColor(mColorO);
                    holder.mBinding.container.setCardElevation(12);

                } else {
                    holder.mBinding.item.setTextColor(mColorO);

                }
                if (isMove && !isGameOver) {
                    mCurrentPlayer = "X";
                    if (mPlayer1Symbol.equals(mCurrentPlayer)) {
                        highlightPlayer(0, mColorX, Color.WHITE);
                    } else {
                        highlightPlayer(1, mColorX, Color.WHITE);
                    }
                }
            }


        }

        private int[] checkScore() {
            int[] values = new int[4];
            String val = "";
            String val2 = "";
            for (int i = 0; i < mMoves.length; i++) {
                val2 += mMoves[i];
                switch (i) {
                    case 0:
                        val = mMoves[0] + mMoves[1] + mMoves[2];
                        values[1] = 0;
                        values[2] = 1;
                        values[3] = 2;
                        break;

                    case 1:
                        val = mMoves[3] + mMoves[4] + mMoves[5];
                        values[1] = 3;
                        values[2] = 4;
                        values[3] = 5;

                        break;
                    case 2:
                        val = mMoves[6] + mMoves[7] + mMoves[8];
                        values[1] = 6;
                        values[2] = 7;
                        values[3] = 8;

                        break;

                    case 3:
                        val = mMoves[0] + mMoves[3] + mMoves[6];
                        values[1] = 0;
                        values[2] = 3;
                        values[3] = 6;

                        break;
                    case 4:
                        val = mMoves[1] + mMoves[4] + mMoves[7];
                        values[1] = 1;
                        values[2] = 4;
                        values[3] = 7;

                        break;

                    case 5:
                        val = mMoves[2] + mMoves[5] + mMoves[8];
                        values[1] = 2;
                        values[2] = 5;
                        values[3] = 8;

                        break;
                    case 6:
                        val = mMoves[0] + mMoves[4] + mMoves[8];
                        values[1] = 0;
                        values[2] = 4;
                        values[3] = 8;

                        break;

                    case 7:
                        val = mMoves[2] + mMoves[4] + mMoves[6];
                        values[1] = 2;
                        values[2] = 4;
                        values[3] = 6;

                        break;
                }
                if (val.equals("XXX")) {
                    values[0] = 0;
                    return values;
                } else if (val.equals("OOO")) {
                    values[0] = 1;
                    return values;
                }
            }
            if (val2.length() == 9) {
                values[0] = 2;
                return values;

            }
            values[0] = -1;
            return values;
        }

        private String[] mMoves = {"", "", "", "", "", "", "", "", ""};
        private int mTotalMoves = 0;
        private boolean isGameOver = false;

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {
            LayoutGameItemBinding mBinding;

            ItemHolder(LayoutGameItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }
        }
    }

    private void updateScoreOnUI(int score) {
        mBinding.score.setText(String.valueOf(score));
    }

}
