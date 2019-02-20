package com.android.mediaplayer2;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekVolume;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //a classe MediaPlayer permite configurar a musica a ser executada
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.teste);

        inicializarSeekBar();
    }

    private void inicializarSeekBar(){
        seekVolume  = findViewById(R.id.seekVolumeId);

        //configura o audio manager conseguimos saber o volume atual e qual o volume máximo que o usuário pode colocar
        // Com getSystemService conseguimos recuperar serviços do sistema
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //recupera os valores de volume máximo e atual
        int volumeMaximo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volumeAtual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //configura os volumes máximos para o SeekBar
        seekVolume.setMax(volumeMaximo);

        //configura o progresso atual do SeekBar
        seekVolume.setProgress(volumeAtual);

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            //quando o usuário muda o SeekBar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //mudando o audio
                //progress - indice do volume que está sendo modificado
                //as flags fazem configurações adicionais
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //será chamado direto na interface, por isso o parâmetro View
    public void executarSom(View view){

        //se por algum motivo o objetivo mediaPlayer não foi criado, ele estará null
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }

    public void pausarMusica(View view){

        //isPlaying retorna true se a musica estiver executando
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    public void pararMusica(View view){
        if(mediaPlayer.isPlaying()){

            /*Quando stop() é executado ele remove a referencia de musica que criamos, dessa forma
            * se tentarmos executar novamente a musica não conseguiremos porque o mediaPlayer estará null.
            * Para que isso seja resolvdo devemos configurar novamente a classe MediaPlayer com a musica a ser executada
            * */
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.teste);
        }
    }

    //quando nossa activity for destruida podemos fazer algumas coisas para economizar recursos do usuário
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            //release libera recursos de memoria que estejam sendo usados
            mediaPlayer.release();
            //para desocupar espaços de memoria do mediaPlayer
            mediaPlayer = null;
        }
    }

    //toda vez que o usuário sair do app irá pausar a musica
    @Override
    protected void onStop() {
        super.onStop();

        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }
}
