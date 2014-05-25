package cat.picas.customtypeface.sample;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class AllCapsTextView extends TextView {

    public AllCapsTextView(Context context) {
        this(context, null, R.attr.allCapsTextViewStyle);
        init(context);
    }

    public AllCapsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.allCapsTextViewStyle);
        init(context);
    }

    public AllCapsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setTransformationMethod(new TransformationMethod() {
            @Override
            public CharSequence getTransformation(CharSequence source, View view) {
                return source.toString().toUpperCase();
            }

            @Override
            public void onFocusChanged(View view, CharSequence sourceText, boolean focused,
                    int direction, Rect previouslyFocusedRect) { }
        });
    }
}
