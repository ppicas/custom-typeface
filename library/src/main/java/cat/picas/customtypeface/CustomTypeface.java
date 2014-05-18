package cat.picas.customtypeface;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO Document CustomTypeface
 */
public class CustomTypeface {

    private Map<Class<?>, Integer> mDefStyleAttrs = new HashMap<Class<?>, Integer>();
    private Map<String, Typeface> mTypefaces = new HashMap<String, Typeface>();

    public static CustomTypeface getInstance() {
        return SingletonHolder.instance;
    }

    public void applyTypeface(View view, AttributeSet attrs) {
        if (!(view instanceof TextView) || view.getContext() == null) {
            return;
        }
        TextView textView = (TextView) view;
        Resources.Theme theme = view.getContext().getTheme();

        List<Integer> defStyleAttrs = getHierarchyDefStyleAttrs(textView.getClass());
        for (Integer defStyleAttr : defStyleAttrs) {
            boolean applied = applyTypeface(textView, defStyleAttr, attrs, theme);
            if (applied) {
                break;
            }
        }
    }

    public void registerDefaultAttrStyle(Class<? extends TextView> clazz, int defAttrStyle) {
        mDefStyleAttrs.put(clazz, defAttrStyle);
    }

    public Typeface getTypeface(String typefaceName) {
        return mTypefaces.get(typefaceName);
    }

    public void registerTypeface(String typefaceName, Typeface typeface) {
        mTypefaces.put(typefaceName, typeface);
    }

    public View createView(String name, Context context, AttributeSet attrs) {
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            String prefix = null;
            if (name.indexOf('.') == -1) {
                prefix = "android.widget.";
            }
            View view = inflater.createView(name, prefix, attrs);
            applyTypeface(view, attrs);
            return view;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private List<Integer> getHierarchyDefStyleAttrs(Class<?> clazz) {
        List<Integer> attrs = new ArrayList<Integer>();
        while (clazz != null) {
            Integer attr = mDefStyleAttrs.get(clazz);
            if (attr != null) {
                attrs.add(attr);
            }
            clazz = clazz.getSuperclass();
        }
        attrs.add(0);
        return attrs;
    }

    private boolean applyTypeface(TextView textView, int defStyleAttr,
            AttributeSet attrs, Resources.Theme theme) {

        boolean applied = false;

        TypedArray typedArray = theme.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.textAppearance}, defStyleAttr, 0);
        int taResId = typedArray.getResourceId(0, -1);
        typedArray.recycle();
        if (taResId != -1) {
            typedArray = theme.obtainStyledAttributes(taResId, new int[]{
                    R.attr.customTypeface, R.attr.customTypefaceIgnoreParents});
            applied = applyTypedArray(textView, typedArray);
            typedArray.recycle();
        }

        typedArray = theme.obtainStyledAttributes(attrs,
                R.styleable.CustomTypeface, defStyleAttr, 0);
        try {
            return applyTypedArray(textView, typedArray) || applied;
        } finally {
            typedArray.recycle();
        }
    }

    private boolean applyTypedArray(TextView textView, TypedArray typedArray) {
        boolean ignoreParents = typedArray.getBoolean(
                R.styleable.CustomTypeface_customTypefaceIgnoreParents, false);
        String typefaceName = typedArray.getString(
                R.styleable.CustomTypeface_customTypeface);

        if (ignoreParents && typefaceName == null) {
            return true;
        } else  if (typefaceName != null) {
            Typeface typeface = getTypeface(typefaceName);
            if (typeface != null) {
                textView.setTypeface(typeface);
            }
            return true;
        } else {
            return false;
        }
    }

    private static class SingletonHolder {
        public static final CustomTypeface instance = new CustomTypeface();

        static {
            instance.registerDefaultAttrStyle(TextView.class, android.R.attr.textViewStyle);
            instance.registerDefaultAttrStyle(EditText.class, android.R.attr.editTextStyle);
            instance.registerDefaultAttrStyle(Button.class, android.R.attr.buttonStyle);
            instance.registerDefaultAttrStyle(AutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            instance.registerDefaultAttrStyle(CheckBox.class, android.R.attr.checkboxStyle);
            instance.registerDefaultAttrStyle(RadioButton.class, android.R.attr.radioButtonStyle);
            instance.registerDefaultAttrStyle(ToggleButton.class, android.R.attr.buttonStyleToggle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                instance.registerDefaultAttrStyle(CheckedTextView.class, android.R.attr.checkedTextViewStyle);
            }
        }
    }
}
