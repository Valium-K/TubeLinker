package dev.valium.tubelinker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if(intent.getAction().equals(Intent.ACTION_SEND)) {
            String link = intent.getStringExtra("android.intent.extra.TEXT");

            if("text/plain".equals(intent.getType()) && link.contains("youtu")) {

                // https://youtu.be/XXXXXXX
                String targetUrl = link.split("/")[3];

                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.y2mate.com/kr/youtube-mp3/" + targetUrl));
                startActivity(browserIntent);
            }
            else
                showToastMessage("유튜브 링크만 주세요.");
        }
    }
    private void showToastMessage(String message) {
        MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this,
                message,
                Toast.LENGTH_SHORT).show());
    }
}