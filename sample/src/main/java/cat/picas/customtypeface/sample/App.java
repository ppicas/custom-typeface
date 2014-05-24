package cat.picas.customtypeface.sample;

import android.app.Application;
import android.graphics.Typeface;

import cat.picas.customtypeface.CustomTypeface;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register a Typeface creating first the object, and then registering the object
        // with a name.
        Typeface typeface = Typeface.createFromAsset(getAssets(), "permanent-marker.ttf");
        CustomTypeface.getInstance().registerTypeface("permanent-marker", typeface);

        // Also you can directly use this shortcut to let CustomTypeface to create the
        // Typeface object for you.
        CustomTypeface.getInstance().registerTypeface("audiowide", getAssets(), "audiowide.ttf");
    }
}
