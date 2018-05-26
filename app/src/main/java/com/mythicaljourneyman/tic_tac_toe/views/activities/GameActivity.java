package com.mythicaljourneyman.tic_tac_toe.views.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.mythicaljourneyman.tic_tac_toe.R;
import com.mythicaljourneyman.tic_tac_toe.databinding.ActivityGameBinding;
import com.mythicaljourneyman.tic_tac_toe.databinding.LayoutGameItemBinding;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
    public static Intent getStartIntentforTwoPlayer(Context context, String player1Name, String player2Name, String player1Symbol, String player2Symbol) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
        intent.putExtra(PLAYER_2_NAME, player2Name);
        intent.putExtra(PLAYER_1_SYMBOL, player1Symbol);
        intent.putExtra(PLAYER_2_SYMBOL, player2Symbol);
        return intent;
    }

    public static Intent getStartIntentforSinglePlayer(Context context, String player1Name) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra(PLAYER_1_NAME, player1Name);
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

        if (TextUtils.isEmpty(player2Name)) {
            player2Name = "Computer";
        }

        // get grid size from intent.
        // set default grid size to 4.
        mGridSize = getIntent().getIntExtra(GRID_SIZE, 3);

        initializeViews(player1Name, player2Name, player1Symbol, player2Symbol);
    }

    private void initializeViews(String player1Name, String player2Name, String player1Symbol, String player2Symbol) {
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

        // initialize layout manager and recycler view
        mBinding.list.setLayoutManager(new GridLayoutManager(this, mGridSize, GridLayoutManager.VERTICAL, false));
        mBinding.list.setHasFixedSize(true);
        mBinding.list.setAdapter(new ItemAdapter(list));
        mBinding.list.setItemAnimator(new DefaultItemAnimator());

    }

    /**
     * finish game. save player score and exit.
     */
    private void endGame() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        finish();
                    }
                });
    }

    private void playClick() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.start();
    }

    class Item {
        Item(String value) {
            this.value = value;
        }

        boolean open = false;
        boolean checked = false;
        String value;
    }

    class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
        private ArrayList<Item> mList;
        String mCurrentPlayer = "X";

        ItemAdapter(ArrayList<Item> list) {
            mList = list;
        }


        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutGameItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.layout_game_item, parent, false);
            return new ItemHolder(binding);
        }

        private int mColorCorrectPairingBackground = 0xFF4CAF50;
        private int mColorCorrectPairingText = 0xffffffff;
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
                            updatePosition(holder, item);
                        }
                    }
                });
            }

        }

        private void animate(ItemHolder holder, Item item) {

            // close a grid item
            if (item.open) {
                holder.mBinding.front.animate().alpha(0).rotationYBy(180).setInterpolator(new LinearInterpolator()).setDuration(mTileAnimationTimeInMilliSeconds).start();
                holder.mBinding.back.animate().alpha(1).rotationYBy(-180).setInterpolator(new LinearInterpolator()).setDuration(mTileAnimationTimeInMilliSeconds).start();
                holder.mBinding.container.animate().translationZ(0).start();
                item.open = false;
            }

            // open a grid item
            else {

                // play click sound
                playClick();

                holder.mBinding.front.animate().alpha(1).rotationYBy(180).setInterpolator(new LinearInterpolator()).setDuration(mTileAnimationTimeInMilliSeconds).start();
                holder.mBinding.back.animate().alpha(0).rotationYBy(-180).setInterpolator(new LinearInterpolator()).setDuration(mTileAnimationTimeInMilliSeconds).start();
                holder.mBinding.container.animate().translationZ(12).start();
                item.open = true;
            }
        }

        private void updatePosition(ItemHolder holder, Item item) {
            int position = holder.getAdapterPosition();
            mTotalMoves++;
            mBinding.score.setText(String.valueOf(mTotalMoves));
            item.checked = true;
            // set value
            mMoves[position] = mCurrentPlayer;

            holder.mBinding.item.setText(mMoves[position]);

            if (mCurrentPlayer.equals("X")) {
                holder.mBinding.item.setTextColor(0xffEF6C00);
                mCurrentPlayer = "O";
            } else {
                holder.mBinding.item.setTextColor(0xFF558B2F);
                mCurrentPlayer = "X";
            }
//            animate(holder, item);

            // check score
            int score = checkScore();
            if (score == 0) {
                Toast.makeText(GameActivity.this, "Winner is X", Toast.LENGTH_SHORT).show();
                isGameOver = true;
            } else if (score == 1) {
                Toast.makeText(GameActivity.this, "Winner is Y", Toast.LENGTH_SHORT).show();
                isGameOver = true;
            } else if (score == 2) {
                Toast.makeText(GameActivity.this, "DRAW", Toast.LENGTH_SHORT).show();
                isGameOver = true;
            } else {

            }

            // end game if all pairs have been identified
            if (mTotalMoves == 9) {
                Completable.complete()
                        .delay(1, TimeUnit.SECONDS)
                        .subscribe(new Action() {
                            @Override
                            public void run() throws Exception {
                                endGame();
                            }
                        });
            }

            holder.mBinding.container.setOnClickListener(null);


        }

        private int checkScore() {
            String val = "";
            String val2 = "";
            for (int i = 0; i < mMoves.length; i++) {
                val2 += mMoves[i];
                switch (i) {
                    case 0:
                        val = mMoves[0] + mMoves[1] + mMoves[2];

                        break;

                    case 1:
                        val = mMoves[3] + mMoves[4] + mMoves[5];

                        break;
                    case 2:
                        val = mMoves[6] + mMoves[7] + mMoves[8];

                        break;

                    case 3:
                        val = mMoves[0] + mMoves[3] + mMoves[6];

                        break;
                    case 4:
                        val = mMoves[1] + mMoves[4] + mMoves[7];

                        break;

                    case 5:
                        val = mMoves[2] + mMoves[5] + mMoves[8];

                        break;
                    case 6:
                        val = mMoves[0] + mMoves[4] + mMoves[8];

                        break;

                    case 7:
                        val = mMoves[2] + mMoves[4] + mMoves[6];

                        break;
                }
                if (val.equals("XXX")) {
                    return 0;
                } else if (val.equals("OOO")) {
                    return 1;
                }
            }
            if (val2.length() == 9) {
                return 2;

            }
            return -1;
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
