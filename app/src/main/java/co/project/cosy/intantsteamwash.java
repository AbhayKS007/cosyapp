package co.project.cosy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.Vector;


public class intantsteamwash extends AppCompatActivity {
    RecyclerView recyclerView;
    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intantsteamwash);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        youtubeVideos.add( new YouTubeVideos
                ("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xLCn88bfW1o\"" +
                        " frameborder=\"0\" allowfullscreen></iframe>"));
        VideoAdapter videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);
    }
}