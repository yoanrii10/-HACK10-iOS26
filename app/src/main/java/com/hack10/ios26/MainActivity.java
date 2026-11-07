package com.hack10.ios26;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable rounded(int color, int strokeColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(24));
        shape.setStroke(dp(1), strokeColor);
        return shape;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout screen = new LinearLayout(this);
        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setPadding(dp(22), dp(38), dp(22), dp(24));
        screen.setBackgroundColor(Color.rgb(16, 17, 22));

        TextView brand = new TextView(this);
        brand.setText("HACK10");
        brand.setTextColor(Color.WHITE);
        brand.setTextSize(32);
        brand.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        screen.addView(brand);

        TextView subtitle = new TextView(this);
        subtitle.setText("iOS 26  ·  LIQUID GLASS CONCEPT");
        subtitle.setTextColor(Color.rgb(10, 132, 255));
        subtitle.setTextSize(12);
        subtitle.setPadding(0, dp(4), 0, dp(24));
        screen.addView(subtitle);

        TextView heading = new TextView(this);
        heading.setText("Centro de control");
        heading.setTextColor(Color.WHITE);
        heading.setTextSize(23);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setPadding(0, 0, 0, dp(14));
        screen.addView(heading);

        String[][] tiles = {
            {"◉", "Wi-Fi"},
            {"ᛒ", "Bluetooth"},
            {"✈", "Modo avión"},
            {"ϟ", "Linterna"},
            {"☼", "Brillo"},
            {"♫", "Sonido"}
        };

        for (String[] tile : tiles) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(dp(18), 0, dp(18), 0);
            row.setBackground(rounded(Color.rgb(40, 42, 53), 0x55FFFFFF));

            TextView symbol = new TextView(this);
            symbol.setText(tile[0]);
            symbol.setTextColor(Color.rgb(10, 132, 255));
            symbol.setTextSize(22);
            symbol.setGravity(Gravity.CENTER);
            row.addView(symbol, new LinearLayout.LayoutParams(dp(42), dp(56)));

            TextView label = new TextView(this);
            label.setText(tile[1]);
            label.setTextColor(Color.WHITE);
            label.setTextSize(16);
            label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            LinearLayout.LayoutParams labelParams =
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            labelParams.leftMargin = dp(8);
            row.addView(label, labelParams);

            LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(66));
            rowParams.bottomMargin = dp(10);
            screen.addView(row, rowParams);
        }

        TextView note = new TextView(this);
        note.setText("Prototipo visual · Los controles todavía no ejecutan acciones del sistema.");
        note.setTextColor(Color.rgb(170, 172, 184));
        note.setTextSize(11);
        note.setPadding(0, dp(12), 0, 0);
        screen.addView(note);

        setContentView(screen);
    }
}
