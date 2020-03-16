package com.tolcompany.tol;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;
import com.tolcompany.tol.Fragments.ArtistFragment;
import com.tolcompany.tol.Fragments.HistoryFragment;
import com.tolcompany.tol.Fragments.SearchFragment;
import com.tolcompany.tol.Fragments.SongFragment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    private CoordinatorLayout coordinatorLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private RelativeLayout relativeLayout;
    private ImageButton btn_play_pause;
    private ImageView img;
    private SeekBar seekBar;
    private CoordinatorLayout bottomBarLayout;
    private MediaPlayer mediaPlayer;
    private int duracion;
    private int duracion_tiempo_real;
    final Handler handler = new Handler();
    private TextView txtde;
    private TextView txta;
    private final String url = "https://firebasestorage.googleapis.com/v0/b/platzigram-5dbcb.appspot.com/o/aviation.mp3?alt=media&token=35370a50-f3bb-44fb-bc89-fac63bac6295"; // your URL here


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //reproductor
        coordinatorLayout = findViewById(R.id.c_BottomPlayer);
        //comportamiento
        bottomSheetBehavior = BottomSheetBehavior.from(coordinatorLayout);
        //relative layout que levanta el layout al hacer click
        relativeLayout = findViewById(R.id.btn_Reaccion);
        //boton de play
        btn_play_pause = findViewById(R.id.btn_Play);
        //seekbar
        seekBar = findViewById(R.id.seek_Bar);
        seekBar.setMax(99);//va a llegar hasta el 100%
        //bottombar
        bottomBarLayout = findViewById(R.id.c_BottomBarLayout);
        //imagen de la cancion
        img = findViewById(R.id.img_View);
        //txtde
        txtde = findViewById(R.id.txt_from);
        //txta
        txta = findViewById(R.id.txt_to);
        btn_play_pause.setImageResource(R.drawable.ic_play);
        //cargamos imagen en el view
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/platzigram-5dbcb.appspot.com/o/tlsp.jpg?alt=media&token=1302d683-1076-4966-a33f-dad7a5c0e528").into(img);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int state = bottomSheetBehavior.getState();
                if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomBarLayout.setVisibility(View.GONE);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    bottomBarLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //hay algun cambio en el seekbar, cambiar a la posicion de la cancion
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mediaPlayer.isPlaying()) {
                    SeekBar seekBar = (SeekBar) view;
                    int posicionPlay = (duracion / 100) * seekBar.getProgress();
                    mediaPlayer.seekTo(posicionPlay);
                }
                return false;
            }
        });
        //creamos un objeto de tipo BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //los parametros del metodo replace(en donde quiero poner mi fragment, el fragment que voy a inyectar)
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SongFragment()).commit();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {

                    case R.id.tab_artist:
                        selectedFragment = new ArtistFragment();

                        break;
                    case R.id.tab_music:
                        selectedFragment = new SongFragment();
                        break;
                    case R.id.tab_search:
                        selectedFragment = new SearchFragment();
                        break;

                    case R.id.tab_history:
                        selectedFragment = new HistoryFragment();
                        break;
                }

                //los parametros del metodo replace(en donde quiero poner mi fragment, el fragment que voy a inyectar)
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();

                return true;
            }
        });

        /**********************************Reproductor de musica************************/
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        btn_play_pause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                try {
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                duracion = mediaPlayer.getDuration();
                duracion_tiempo_real = 0;
                //mandar duracion de la cancion
                txta.setText(String.format("%d:%d", TimeUnit.SECONDS.toMinutes(TimeUnit.MILLISECONDS.toSeconds(duracion)),TimeUnit.MILLISECONDS.toSeconds(duracion)));
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();

                    btn_play_pause.setImageResource(R.drawable.ic_pause);
                } else {
                    mediaPlayer.pause();

                    btn_play_pause.setImageResource(R.drawable.ic_play);
                }

                //actualizar seekbar
                updateSeekBar();

            }


        });

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    private void updateSeekBar() {
        seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / duracion) * 100));
        if (mediaPlayer.isPlaying()) {
            final Runnable updater = new Runnable() {
                @Override
                public void run() {
                    //recursividad
                    updateSeekBar();
                    if(duracion_tiempo_real==60){
                        duracion_tiempo_real = 0;
                    }else{
                        duracion_tiempo_real = duracion_tiempo_real + 1000;
                    }
                    txtde.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(duracion_tiempo_real), TimeUnit.MILLISECONDS.toSeconds(duracion_tiempo_real) + TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duracion_tiempo_real))));
                }
            };
            handler.postDelayed(updater, 1000);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        seekBar.setSecondaryProgress(i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        btn_play_pause.setImageResource(R.drawable.ic_play);
        mediaPlayer.stop();
    }
}

