package com.kim.tinyplanes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Tiny Planes: a thin native shell around the HTML game in assets/index.html.
 * Tuned for the Galaxy S24: landscape lock, true full screen (no status or
 * navigation bar), game drawn into the camera cut-out area, highest refresh
 * rate the panel offers (120 Hz on the S24), screen kept awake while playing.
 */
public class MainActivity extends Activity {

    private WebView web;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        WindowManager.LayoutParams lp = window.getAttributes();
        if (Build.VERSION.SDK_INT >= 28) {
            // Use the full 2340-px width, including the punch-hole camera side.
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        if (Build.VERSION.SDK_INT >= 30) {
            // Ask for the fastest refresh mode at the current resolution (120 Hz on S24).
            Display display = getDisplay();
            if (display != null) {
                Display.Mode current = display.getMode();
                Display.Mode best = current;
                for (Display.Mode m : display.getSupportedModes()) {
                    if (m.getPhysicalWidth() == current.getPhysicalWidth()
                            && m.getPhysicalHeight() == current.getPhysicalHeight()
                            && m.getRefreshRate() > best.getRefreshRate()) {
                        best = m;
                    }
                }
                lp.preferredDisplayModeId = best.getModeId();
            }
        }
        window.setAttributes(lp);

        web = new WebView(this);
        web.setBackgroundColor(Color.parseColor("#1B2A4A"));
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);
        web.setVerticalScrollBarEnabled(false);
        web.setHorizontalScrollBarEnabled(false);
        web.setHapticFeedbackEnabled(false);

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);                 // remembers best round + sound settings
        s.setMediaPlaybackRequiresUserGesture(false); // engine + music start cleanly
        s.setSupportZoom(false);
        s.setBuiltInZoomControls(false);
        s.setTextZoom(100);                           // ignore the phone's font-size setting
        s.setAllowFileAccess(true);

        setContentView(web);
        goImmersive();
        web.loadUrl("file:///android_asset/index.html");
    }

    private void goImmersive() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 30) {
            window.setDecorFitsSystemWindows(false);
            WindowInsetsController c = window.getInsetsController();
            if (c != null) {
                c.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                c.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) goImmersive();
    }

    @Override
    protected void onPause() {
        // Pauses the game loop and audio when you leave the app.
        if (web != null) { web.onPause(); web.pauseTimers(); }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (web != null) { web.resumeTimers(); web.onResume(); }
        goImmersive();
    }

    @Override
    protected void onDestroy() {
        if (web != null) { web.destroy(); web = null; }
        super.onDestroy();
    }
}
