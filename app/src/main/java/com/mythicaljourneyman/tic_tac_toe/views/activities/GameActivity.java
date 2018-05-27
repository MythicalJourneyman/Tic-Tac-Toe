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
    public static Intent getStartIntentForTwoPlayer3(Context context, String player1Name, String player2Name, String player1Symbol, String player2Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_2_NAME, player2Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        intent.putExtra(PLAYER_2_SYMBOL, player2Symbol);
        intent.putExtra(GRID_SIZE, 3);
        return intent;
    }

    public static Intent getStartIntentForTwoPlayer4(Context context, String player1Name, String player2Name, String player1Symbol, String player2Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_2_NAME, player2Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        intent.putExtra(PLAYER_2_SYMBOL, player2Symbol);
        intent.putExtra(GRID_SIZE, 4);
        return intent;
    }

    public static Intent getStartIntentForSinglePlayer3(Context context, String player1Name, String player1Symbol, String player2Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        intent.putExtra(PLAYER_2_SYMBOL, player2Symbol);
        intent.putExtra(GRID_SIZE, 3);
        return intent;
    }

    public static Intent getStartIntentForSinglePlayer4(Context context, String player1Name, String player1Symbol, String player2Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        intent.putExtra(PLAYER_2_SYMBOL, player2Symbol);
        intent.putExtra(GRID_SIZE, 4);
        return intent;
    }

    private ActivityGameBinding mBinding;

    @Override
    public void onBackPressed() {
        //finish game on back press
        endGame();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer = null;
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
        }


        // get grid size from intent.
        // set default grid size to 4.
        mGridSize = getIntent().getIntExtra(GRID_SIZE, 3);
        if (mGridSize == 3) {
            mBinding.threeGrid.setVisibility(View.VISIBLE);
        } else if (mGridSize == 4) {
            mBinding.fourGrid.setVisibility(View.VISIBLE);
        }

        initializeViews(player1Name, player2Name, player1Symbol, player2Symbol, isComputer);
    }

    MediaPlayer mMediaPlayer;

    private void initializeViews(String player1Name, String player2Name, String player1Symbol, String player2Symbol, boolean isComputer) {
        // set player names and symbols
        mBinding.player1Name.setText(player1Name);
        mBinding.player2Name.setText(player2Name);
        setPlayer1Symbol(player1Symbol);
        setPlayer2Symbol(player2Symbol);
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
            list.add(new Item());
        }

        int colorX = ContextCompat.getColor(this, R.color.colorX);
        int colorO = ContextCompat.getColor(this, R.color.colorO);

        // initialize layout manager and recycler view
        mBinding.list.setLayoutManager(new GridLayoutManager(this, mGridSize, GridLayoutManager.VERTICAL, false));
        mBinding.list.setHasFixedSize(true);
//        mBinding.list.setLayoutAnimation(new LayoutAnimationController(new AlphaAnimation(0,1)));
//        mBinding.list.setItemAnimator(new DefaultItemAnimator());
        mBinding.list.setAdapter(new ItemAdapter(list, isComputer, player1Name, player2Name, player1Symbol, player2Symbol, colorX, colorO));
        mMediaPlayer = MediaPlayer.create(this, R.raw.click);

        highlightPlayer(0, colorX, Color.WHITE);

    }

    private void highlightPlayer(int player, int color, int colorPlain) {
        if (player == 0) {
            mBinding.player1Symbol.setTextColor(color);
            mBinding.player1Icon.setTextColor(color);
            mBinding.player2Symbol.setTextColor(colorPlain);
            mBinding.player2Icon.setTextColor(colorPlain);
        } else {
            mBinding.player1Symbol.setTextColor(colorPlain);
            mBinding.player1Icon.setTextColor(colorPlain);
            mBinding.player2Symbol.setTextColor(color);
            mBinding.player2Icon.setTextColor(color);
        }
    }

    private void setPlayer1Symbol(String symbol) {
        if (symbol.equalsIgnoreCase("X") || symbol.equalsIgnoreCase("O")) {
            mBinding.player1Symbol.setText(symbol);
            mBinding.player1Symbol.setVisibility(View.VISIBLE);
            mBinding.player1Icon.setVisibility(View.GONE);
        } else {
            mBinding.player1Icon.setText(symbol);
            mBinding.player1Symbol.setVisibility(View.GONE);
            mBinding.player1Icon.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayer2Symbol(String symbol) {
        if (symbol.equalsIgnoreCase("X") || symbol.equalsIgnoreCase("O")) {
            mBinding.player2Symbol.setText(symbol);
            mBinding.player2Symbol.setVisibility(View.VISIBLE);
            mBinding.player2Icon.setVisibility(View.GONE);
        } else {
            mBinding.player2Icon.setText(symbol);
            mBinding.player2Symbol.setVisibility(View.GONE);
            mBinding.player2Icon.setVisibility(View.VISIBLE);
        }
    }

    /**
     * finish game. save player score and exit.
     */
    private void endGame() {
        finish();
    }

    private void playClick() {
        mMediaPlayer.start();
    }

    class Item {

        boolean winner = false;
        boolean checked = false;
        boolean makeMove = false;
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
        private ArrayList<Item> mList;

        class Player {
            int color;
            String name;
            String symbol;
            boolean isIcon = false;

            public Player(int color, String name, String symbol, boolean isIcon) {
                this.color = color;
                this.name = name;
                this.symbol = symbol;
                this.isIcon = isIcon;
            }

            public Player(int color, String name, String symbol) {
                this.color = color;
                this.name = name;
                this.symbol = symbol;
            }

            public Player() {
            }
        }

        int mCurrentPlayer = 0;
        //        String mPlayer2Symbol = "O";
//        String mPlayer1Symbol = "X";
//        String mPlayer1Name = "X";
//        String mPlayer2Name = "O";
        Player[] mPlayerData;
        boolean isComputer;
        HashSet<Integer> mEmptyPlaces = new HashSet<>();

        ItemAdapter(ArrayList<Item> list, boolean isComputer, String player1Name, String player2Name, String player1Symbol, String player2Symbol, int colorX, int colorO) {
            mList = list;
            this.isComputer = isComputer;
            mMoves = new String[mGridSize][mGridSize];
//            mPlayer1Name = player1Name;
//            mPlayer2Name = player2Name;
//            mPlayer2Symbol = player2Symbol;
//            mPlayer1Symbol = player1Symbol;
            mCurrentPlayer = 0;
            mColorX = colorX;
            mColorO = colorO;
            mPlayerData = new Player[]{
                    player1Symbol.equals("X") || player1Symbol.equals("O") ? new Player(mColorX, player1Name, player1Symbol) : new Player(mColorX, player1Name, player1Symbol, true),
                    player2Symbol.equals("X") || player2Symbol.equals("O") ? new Player(mColorO, player2Name, player2Symbol) : new Player(mColorO, player2Name, player2Symbol, true)};
            for (int i = 0; i < mGridSize * mGridSize; i++) {
                mEmptyPlaces.add(i);
            }
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
        //        private String[] mMoves;
        private String[][] mMoves;
        private int mTotalMoves = 0;
        private int mLastMove = -1;
        private boolean isGameOver = false;

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
                            if (!isGameOver && isComputer && mCurrentPlayer == 1) {
                                makeComputerMove();
                            }
                        }
                    }
                });
            } else {
                if (item.winner) {
                    updatePosition(holder, item, false);
                } else if (item.makeMove) {
                    updatePosition(holder, item, true);
                }
            }

        }

        private void makeComputerMove() {
            int position = -1;
            ArrayList<Integer> list = new ArrayList<>(mEmptyPlaces);
            Collections.shuffle(list);
            if (list.size() > 0) {
                position = list.get(0);
            }
            if (position > -1 && position < mList.size()) {
                mList.get(position).makeMove = true;
                mList.get(position).checked = true;
                mBinding.list.findViewHolderForAdapterPosition(position).itemView.performClick();
            }

        }

        private void togglePlayer() {
            mCurrentPlayer++;
            mCurrentPlayer = mCurrentPlayer % 2;
        }

        private void updatePosition(ItemHolder holder, Item item, boolean isMove) {
            int position = holder.getAdapterPosition();

            if (isMove) {
                mLastMove = position;
                playClick();
                mTotalMoves++;
                mBinding.score.setText(String.valueOf(mTotalMoves));
                item.checked = true;
                mEmptyPlaces.remove(position);
                // set value
                int row = position / mGridSize;
                int column = position - (row * mGridSize);
                mMoves[row][column] = mPlayerData[mCurrentPlayer].symbol;

                holder.mBinding.container.setOnClickListener(null);

                int[] score = checkScore(row, column);
                if (score[0] > -1 && score[0] < mPlayerData.length) {
                    Toast.makeText(GameActivity.this, "Winner is " + mPlayerData[score[0]].name, Toast.LENGTH_SHORT).show();
                    isGameOver = true;

                    for (int i = 1; i < score.length; i++) {
                        mList.get(score[i]).winner = true;
                        notifyItemChanged(score[i]);
                    }

                } else if (score[0] == 2) {
                    Toast.makeText(GameActivity.this, "DRAW", Toast.LENGTH_SHORT).show();
                    isGameOver = true;
                } else {

                }
            }

            if (mPlayerData[mCurrentPlayer].isIcon) {
                holder.mBinding.item.setVisibility(View.GONE);
                holder.mBinding.icon.setVisibility(View.VISIBLE);
                holder.mBinding.icon.setText(mPlayerData[mCurrentPlayer].symbol);
            } else {
                holder.mBinding.item.setVisibility(View.VISIBLE);
                holder.mBinding.icon.setVisibility(View.GONE);
                holder.mBinding.item.setText(mPlayerData[mCurrentPlayer].symbol);
            }
            holder.mBinding.item.setText(mPlayerData[mCurrentPlayer].symbol);
            holder.mBinding.icon.setText(mPlayerData[mCurrentPlayer].symbol);

            if (isMove) {

            }

            if (item.winner) {
                holder.mBinding.item.setTextColor(Color.WHITE);
                holder.mBinding.icon.setTextColor(Color.WHITE);
                holder.mBinding.container.setCardBackgroundColor(mPlayerData[mCurrentPlayer].color);
                holder.mBinding.container.setCardElevation(12);

            } else {
                holder.mBinding.item.setTextColor(mPlayerData[mCurrentPlayer].color);
                holder.mBinding.icon.setTextColor(mPlayerData[mCurrentPlayer].color);
            }


            // change player keep it in last
            if (isMove && !isGameOver) {
                togglePlayer();
                highlightPlayer(mCurrentPlayer, mPlayerData[mCurrentPlayer].color, Color.WHITE);
            }


        }

        //        private int[] checkScore() {
//            String player1WinningCombo = "";
//            String player2WinningCombo = "";
//
//            for (int i = 0; i < mGridSize; i++) {
//                player1WinningCombo += mPlayerData[0].symbol;
//                player2WinningCombo += mPlayerData[1].symbol;
//
//            }
//
//
//            int[] values = new int[4];
//            String val = "";
//            String val2 = "";
//            for (int i = 0; i < mMoves.length; i++) {
//                val2 += mMoves[i];
//                switch (i) {
//                    case 0:
//                        val = mMoves[0] + mMoves[1] + mMoves[2];
//                        values[1] = 0;
//                        values[2] = 1;
//                        values[3] = 2;
//                        break;
//
//                    case 1:
//                        val = mMoves[3] + mMoves[4] + mMoves[5];
//                        values[1] = 3;
//                        values[2] = 4;
//                        values[3] = 5;
//
//                        break;
//                    case 2:
//                        val = mMoves[6] + mMoves[7] + mMoves[8];
//                        values[1] = 6;
//                        values[2] = 7;
//                        values[3] = 8;
//
//                        break;
//
//                    case 3:
//                        val = mMoves[0] + mMoves[3] + mMoves[6];
//                        values[1] = 0;
//                        values[2] = 3;
//                        values[3] = 6;
//
//                        break;
//                    case 4:
//                        val = mMoves[1] + mMoves[4] + mMoves[7];
//                        values[1] = 1;
//                        values[2] = 4;
//                        values[3] = 7;
//
//                        break;
//
//                    case 5:
//                        val = mMoves[2] + mMoves[5] + mMoves[8];
//                        values[1] = 2;
//                        values[2] = 5;
//                        values[3] = 8;
//
//                        break;
//                    case 6:
//                        val = mMoves[0] + mMoves[4] + mMoves[8];
//                        values[1] = 0;
//                        values[2] = 4;
//                        values[3] = 8;
//
//                        break;
//
//                    case 7:
//                        val = mMoves[2] + mMoves[4] + mMoves[6];
//                        values[1] = 2;
//                        values[2] = 4;
//                        values[3] = 6;
//
//                        break;
//                }
//                if (val.equals(player1WinningCombo)) {
//                    values[0] = 0;
//                    return values;
//                } else if (val.equals(player2WinningCombo)) {
//                    values[0] = 1;
//                    return values;
//                }
//            }
//            if (val2.length() == 9) {
//                values[0] = 2;
//                return values;
//
//            }
//            values[0] = -1;
//            return values;
//        }
        private int[] checkScore(int row, int column) {
//            String player1WinningCombo = "";
//            String player2WinningCombo = "";
//
//            for (int i = 0; i < mGridSize; i++) {
//                player1WinningCombo += mPlayerData[0].symbol;
//                player2WinningCombo += mPlayerData[1].symbol;
//
//            }

            boolean hasWon = true;

            int[] values = new int[mGridSize + 1];
            String val = "";

            int i = row;
            int j = column;
            String symbol = mMoves[i][j];


            //check row
            for (int k = 0; k < mGridSize; k++, j++, j = j % mGridSize) {
                values[k + 1] = (i * mGridSize) + j;
                if (TextUtils.isEmpty(mMoves[i][j]) || !mMoves[i][j].equals(symbol)) {
                    hasWon = false;
                    break;
                }
            }

            i = row;
            j = column;
            // check column
            if (!hasWon) {
                hasWon = true;
                for (int k = 0; k < mGridSize; k++, i++, i = i % mGridSize) {
                    values[k + 1] = (i * mGridSize) + j;
                    if (TextUtils.isEmpty(mMoves[i][j]) || !mMoves[i][j].equals(symbol)) {
                        hasWon = false;
                        break;
                    }
                }
            }

            // check diagonal 1
            i = row;
            j = column;
            if (!hasWon) {
                if (i == j) {
                    hasWon = true;
                    for (int k = 0; k < mGridSize; k++, i++, j++, i = i % mGridSize, j = j % mGridSize) {
                        values[k + 1] = (i * mGridSize) + j;
                        if (TextUtils.isEmpty(mMoves[i][j]) || !mMoves[i][j].equals(symbol)) {
                            hasWon = false;
                            break;
                        }
                    }
                }
            }
            // check diagonal 2
            i = row;
            j = column;
            if (!hasWon) {
                if ((i + j) % mGridSize == mGridSize - 1) {
                    hasWon = true;
                    for (int k = 0; k < mGridSize; k++, i++, j = j + mGridSize - 1, i = i % mGridSize, j = j % mGridSize) {
                        values[k + 1] = (i * mGridSize) + j;
                        if (TextUtils.isEmpty(mMoves[i][j]) || !mMoves[i][j].equals(symbol)) {
                            hasWon = false;
                            break;
                        }
                    }
                }
            }

            // if somebody won return who won
            if (hasWon) {
                values[0] = mCurrentPlayer;
                return values;
            }

            // if nobody won and all moves done
            if (mTotalMoves == mGridSize * mGridSize) {
                values[0] = 2;
                return values;

            }

            // nobody won and moves remaining
            values[0] = -1;
            return values;
        }

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
