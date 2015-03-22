package cat.ppicas.customtypeface.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                ViewStub viewStub = (ViewStub) findViewById(R.id.view_stub);
                viewStub.inflate();
            }
        });
    }
}
