package com.lila.dontworry;
//https://developers.google.com/youtube/android/player/

import android.os.Bundle;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubePlayer extends YouTubeFailureRecoveryActivity{


    private YouTubePlayerView playerView;
    private String youtube_url;

    String DEVELOPER_KEY = "";



    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_youtube);

        // The unique video id of the youtube video (can be obtained from video url)
        youtube_url = "0Bmhjf0rKe8" ;

        playerView = (YouTubePlayerView) findViewById(R.id.player);
        playerView.initialize(DEVELOPER_KEY, this);



    }



    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

        player.setFullscreen(true);
        player.setShowFullscreenButton(false);
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

        if (!wasRestored) {
            player.loadVideo(youtube_url);
        }

    }
}
 