package cat.picas.customtypeface.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import cat.picas.customtypeface.CustomTypeface;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        // Delegate the view create to CustomTypeface
        return CustomTypeface.getInstance().createView(name, context, attrs);
    }
}
