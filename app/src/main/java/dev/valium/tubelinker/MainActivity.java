package dev.valium.tubelinker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        if(intent.getAction().equals(Intent.ACTION_SEND)) {
            String link = intent.getStringExtra("android.intent.extra.TEXT");

            if("text/plain".equals(intent.getType()) && link.contains("youtu")) {

                String targetVideoId = getTargetVideoId(link);
                if(targetVideoId == null) {
                    targetVideoId = getTargetVideoIdFromShortenUrl(link);
                }

                openY2mate(targetVideoId);
            }
            else
                showToastMessage("유튜브 링크만 주세요.");
        }
    }

    /**
     * https://youtu.be/XXXXXXX
     *
     * @param link
     * @return
     */
    private String getTargetVideoIdFromShortenUrl(String link) {
        return link.split("/")[3];
    }

    /**
     * https://www.youtube.com/watch?v=XXXXXXX
     * @param link
     * @return
     */
    private String getTargetVideoId(String link) {
        URL youtubeUrl = null;

        try {
            youtubeUrl = new URL(link);
        } catch (MalformedURLException e) {
            showToastMessage("URL을 파싱하지 못했어요...");

            return link;
        }

        Map<String, String> queryMap = getQueryMap(youtubeUrl.getQuery());

        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();

            if("v".equals(k)) {
               return v;
            }
        }

        return null;
    }

    private void openY2mate(String v) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.y2mate.com/kr/youtube-mp3/" + v));
        startActivity(browserIntent);
    }

    private Map<String, String> getQueryMap(String queryParams) {
        Map<String, String> queryMap = new HashMap<>();

        if(queryParams == null) return queryMap;

        String[] split = queryParams.split("&");
        for(int i = 0; i < split.length; i++) {
            if("".equals(split[i])) continue;

            String[] innerSplit = split[i].split("=");

            if("".equals(innerSplit[i])) continue;
            queryMap.put(innerSplit[0], innerSplit[1]);
        }

        return queryMap;
    }

    private void showToastMessage(String message) {
        MainActivity.this.runOnUiThread(() -> Toast.makeText(MainActivity.this,
                message,
                Toast.LENGTH_SHORT).show());
    }
}