package com.packt.androidconcurrency.chapter6.example3;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.ImageView;

import com.packt.androidconcurrency.LaunchActivity;
import com.packt.androidconcurrency.R;
import com.packt.androidconcurrency.chapter6.CachingDownloadService;
import com.packt.androidconcurrency.chapter6.DownloadService;

public class DownloadActivity extends Activity {

    public static final String URL =
        "http://www.nasa.gov/images/content/158270main_solarflare.jpg";

    private static final BitmapHandler handler = new BitmapHandler();
    private static final Messenger messenger = new Messenger(handler);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch6_example3_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.attach((ImageView)findViewById(R.id.img));

        Intent intent = new Intent(this, CachingDownloadService.class);
        intent.putExtra(DownloadService.DOWNLOAD_FROM_URL, URL);
        intent.putExtra(DownloadService.REQUEST_ID, 1);
        intent.putExtra(DownloadService.MESSENGER, messenger);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.detach();
    }

    private static class BitmapHandler extends Handler {
        private ImageView view;

        @Override
        public void handleMessage(Message message) {
            if (message.what == DownloadService.SUCCESSFUL) {
                if (view != null)
                    view.setImageURI((Uri)message.obj);
            } else {
                Log.w(LaunchActivity.TAG, "download failed :(");
            }
        }

        public void attach(ImageView view) {
            this.view = view;
        }

        public void detach() {
            this.view = null;
        }
    }
}
