
package com.hack10.ios26;

import android.app.Activity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int BLUE = Color.rgb(10, 132, 255);
    private static final int BG = Color.rgb(15, 17, 23);
    private static final int CARD = Color.rgb(39, 42, 54);

    private CameraManager cameraManager;
    private String cameraId;
    private boolean flashlightOn = false;

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable rounded(int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(25));
        shape.setStroke(dp(1), 0x35FFFFFF);
        return shape;
    }

    private TextView makeText(String value, int size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private void openSettings(String action) {
        try {
            startActivity(new Intent(action));
        } catch (Exception e) {
            Toast.makeText(this, "Ajuste no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    private LinearLayout makeTile(String symbol, String name,
                                  String description, Runnable action) {

        LinearLayout tile = new LinearLayout(this);
        tile.setOrientation(LinearLayout.VERTICAL);
        tile.setGravity(Gravity.CENTER_VERTICAL);
        tile.setPadding(dp(16), dp(12), dp(12), dp(12));
        tile.setBackground(rounded(CARD));
        tile.setClickable(true);
        tile.setFocusable(true);
        tile.setOnClickListener(v -> action.run());

        TextView icon = makeText(symbol, 27, BLUE);
        tile.addView(icon);

        TextView title = makeText(name, 15, Color.WHITE);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setPadding(0, dp(5), 0, 0);
        tile.addView(title);

        TextView detail = makeText(description, 10, Color.LTGRAY);
        detail.setPadding(0, dp(3), 0, 0);
        tile.addView(detail);

        return tile;
    }

    private void addTileRow(LinearLayout parent,
                            String s1, String n1, String d1, Runnable a1,
                            String s2, String n2, String d2, Runnable a2) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(112));
        p.bottomMargin = dp(12);

        LinearLayout.LayoutParams left = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        left.rightMargin = dp(6);

        LinearLayout.LayoutParams right = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        right.leftMargin = dp(6);

        row.addView(makeTile(s1, n1, d1, a1), left);
        row.addView(makeTile(s2, n2, d2, a2), right);

        parent.addView(row, p);
    }

    private void initializeFlashlight() {
        try {
            cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

            for (String id : cameraManager.getCameraIdList()) {
                Boolean available = cameraManager.getCameraCharacteristics(id)
                        .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE);

                if (Boolean.TRUE.equals(available)) {
                    cameraId = id;
                    break;
                }
            }
        } catch (Exception e) {
            cameraId = null;
        }
    }

    private void toggleFlashlight() {
        if (cameraId == null) {
            initializeFlashlight();
        }

        if (cameraId == null) {
            Toast.makeText(this, "Linterna no disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            return;
        }

        try {
            flashlightOn = !flashlightOn;
            cameraManager.setTorchMode(cameraId, flashlightOn);

            Toast.makeText(this,
                    flashlightOn ? "Linterna encendida" : "Linterna apagada",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            flashlightOn = false;
            Toast.makeText(this, "No se pudo activar la linterna",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void addSlider(LinearLayout parent, String title, String symbol,
                           int max, int initial,
                           SeekBar.OnSeekBarChangeListener listener) {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(17), dp(13), dp(17), dp(13));
        box.setBackground(rounded(CARD));

        LinearLayout heading = new LinearLayout(this);
        heading.setGravity(Gravity.CENTER_VERTICAL);

        heading.addView(makeText(symbol, 23, BLUE));

        TextView label = makeText(title, 15, Color.WHITE);
        label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        label.setPadding(dp(12), 0, 0, 0);
        heading.addView(label);

        box.addView(heading);

        SeekBar bar = new SeekBar(this);
        bar.setMax(Math.max(1, max));
        bar.setProgress(Math.max(0, Math.min(initial, max)));
        bar.setProgressTintList(
                android.content.res.ColorStateList.valueOf(BLUE));
        bar.setThumbTintList(
                android.content.res.ColorStateList.valueOf(Color.WHITE));
        bar.setOnSeekBarChangeListener(listener);

        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(35));
        bp.topMargin = dp(8);
        box.addView(bar, bp);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        p.bottomMargin = dp(12);

        parent.addView(box, p);
    }

    private void buildInterface() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout screen = new LinearLayout(this);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setPadding(dp(21), dp(35), dp(21), dp(25));
        scroll.addView(screen);

        TextView brand = makeText("HACK10", 34, Color.WHITE);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        screen.addView(brand);

        TextView subtitle = makeText(
                "iOS 26  ·  LIQUID GLASS CONCEPT", 11, BLUE);
        subtitle.setPadding(0, dp(4), 0, dp(27));
        screen.addView(subtitle);

        TextView heading = makeText("Centro de control", 24, Color.WHITE);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setPadding(0, 0, 0, dp(17));
        screen.addView(heading);

        addTileRow(screen,
                "◉", "Wi-Fi", "Abrir configuración",
                () -> openSettings("android.settings.panel.action.WIFI"),
                "ᛒ", "Bluetooth", "Abrir configuración",
                () -> openSettings(Settings.ACTION_BLUETOOTH_SETTINGS));

        addTileRow(screen,
                "✈", "Modo avión", "Abrir configuración",
                () -> openSettings(Settings.ACTION_AIRPLANE_MODE_SETTINGS),
                "ϟ", "Linterna", "Encender / apagar",
                this::toggleFlashlight);

        TextView adjustments = makeText("Ajustes rápidos", 20, Color.WHITE);
        adjustments.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        adjustments.setPadding(0, dp(13), 0, dp(13));
        screen.addView(adjustments);

        addSlider(screen, "Brillo de pantalla", "☼", 100, 70,
                new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.screenBrightness = Math.max(0.05f, progress / 100f);
                    getWindow().setAttributes(params);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        AudioManager audio = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        addSlider(screen, "Volumen multimedia", "♫",
                maxVolume, currentVolume,
                new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (fromUser) {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        LinearLayout settings = makeTile(
                "⚙", "Configuración del dispositivo",
                "Abrir ajustes de Android",
                () -> openSettings(Settings.ACTION_SETTINGS));

        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(105));
        sp.topMargin = dp(5);
        screen.addView(settings, sp);

        TextView footer = makeText(
                "HACK10 DIGITAL  ·  ANDROID 10+",
                10, Color.rgb(145, 150, 165));
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        screen.addView(footer);

        setContentView(scroll);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);

        initializeFlashlight();
        buildInterface();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            toggleFlashlight();
        } else if (requestCode == 100) {
            Toast.makeText(this, "Permiso de cámara denegado",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
