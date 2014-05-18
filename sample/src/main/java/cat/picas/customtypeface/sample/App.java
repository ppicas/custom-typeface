package cat.picas.customtypeface.sample;

import android.app.Application;
import android.graphics.Typeface;

import cat.picas.customtypeface.CustomTypeface;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "permanent-marker.ttf");
        CustomTypeface.getInstance().registerTypeface("permanent-marker", typeface);

        typeface = Typeface.createFromAsset(getAssets(), "audiowide.ttf");
        CustomTypeface.getInstance().registerTypeface("audiowide", typeface);
    }
}
