package cat.picas.customtypeface;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
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
 * This class can be used to automatically apply custom {@link Typeface} to views inflated from
 * any XML.
 * <p>
 * This class is intended to be used from a {@link LayoutInflater.Factory#onCreateView} method,
 * or from {@link LayoutInflater.Factory2#onCreateView}. From one of this methods
 * just call to {@link #createView} and it will take care to delegate the view creation to the
 * system {@link LayoutInflater}, and after the creation try to apply a custom {@code Typeface}
 * if the new created view is an instance of {@link TextView}, or any other derived class.
 * <p>
 * Here is an example of the use of this class. First you should register the {@code Typeface}
 * that you will use from the XML layouts. A good place to do this is in the {@code Application}
 * {@code onCreate} method.
 * </p>
 * <pre><code>
 * public class App extends Application {
 *     {@literal @Override}
 *     public void onCreate() {
 *         super.onCreate();
 *
 *         // Register a Typeface creating first the object, and then registering the object
 *         // with a name.
 *         Typeface typeface = Typeface.createFromAsset(getAssets(), "permanent-marker.ttf");
 *         CustomTypeface.getInstance().registerTypeface("permanent-marker", typeface);
 *
 *         // Also you can directly use this shortcut to let CustomTypeface to create the
 *         // Typeface object for you.
 *         CustomTypeface.getInstance().registerTypeface("audiowide", getAssets(), "audiowide.ttf");
 *     }
 * }
 * </code></pre>
 * <p>
 * The next step is override the {@link Activity#onCreateView} method and delegate the view
 * creation to {@code CustomTypeface}.
 * </p>
 * <pre><code>
 * public class MainActivity extends Activity {
 *
 *     // ...
 *
 *     {@literal @Override}
 *     protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_main);
 *     }
 *
 *     {@literal @Override}
 *     public View onCreateView(String name, Context context, AttributeSet attrs) {
 *         return CustomTypeface.getInstance().createView(name, context, attrs);
 *     }
 *
 *     // ...
 *
 * }
 * </code></pre>
 * <p>
 * Now all the templates inflated in the context of this {@code Activity} will apply a
 * custom {@code Typeface} if it's defined in the XML. Check following the layout file.
 * </p>
 * <pre>{@code
 * <LinearLayout
 *     xmlns:android="http://schemas.android.com/apk/res/android"
 *     xmlns:tools="http://schemas.android.com/tools"
 *     xmlns:app="http://schemas.android.com/apk/res-auto"
 *     ...
 *     />
 *
 *     ...
 *
 *     <Button
 *         android:text="Permanent maker"
 *         android:layout_width="match_parent"
 *         android:layout_height="wrap_content"
 *         android:textSize="22sp"
 *         app:customTypeface="permanent-marker"
 *         tools:ignore="MissingPrefix"/>
 *
 *     ...
 *
 * </LinearLayout>
 * }</pre>
 * <p>
 * In the previous sample you see the use of attribute {@code tools:ignore="MissingPrefix"}.
 * This is because sometimes the you will get a warning from lint that you are applying an
 * attribute with an invalid namespace. In this cases the ignore MissingPrefix will hide this
 * warnings.
 * </p>
 * <p>
 * Also you can use the {@code customTypeface} attribute in your styles, themes and
 * textAppearances as well. You can find some examples of this in the {@code Samples} project.
 * </p>
 * <p>
 * <strong>Custom views extending {@code TextView}</strong>
 * </p>
 * <p>
 * If you have a custom view with a default style defined in theme, then you must register
 * this theme attribute int {@code CustomTypeface}. To do that you can use the method
 * {@link #registerAttributeOfDefaultStyle}. This is because {@code CustomTypeface} doesn't have
 * a way to know what is a default style of a view, and this is why must be registered before.
 * Here is a sample code to register a custom view default style attribute.
 * </p>
 * <pre><code>
 *     CustomTypeface.getInstance().registerAttributeOfDefaultStyle(
 *         CustomTextView.class, R.attr.customTextViewStyle);
 * </code></pre>
 */
public class CustomTypeface {

    private Map<Class<?>, Integer> mDefStyleAttrs = new HashMap<Class<?>, Integer>();
    private Map<String, Typeface> mTypefaces = new HashMap<String, Typeface>();

    public static CustomTypeface getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Register the theme attributes with the default style for all the views extending
     * {@link TextView} defined in the SDK. You can use this method if you create an
     * instance of {@code CustomTypeface}, and you want to configure with the default
     * styles for the default android widgets.
     *
     * @see #registerAttributeOfDefaultStyle(Class, int)
     * @param instance an instance of {@code CustomTypeface} to register the attributes
     */
    public static void registerAttributesOfDefaultStyles(CustomTypeface instance) {
        instance.registerAttributeOfDefaultStyle(TextView.class, android.R.attr.textViewStyle);
        instance.registerAttributeOfDefaultStyle(EditText.class, android.R.attr.editTextStyle);
        instance.registerAttributeOfDefaultStyle(Button.class, android.R.attr.buttonStyle);
        instance.registerAttributeOfDefaultStyle(AutoCompleteTextView.class,
                android.R.attr.autoCompleteTextViewStyle);
        instance.registerAttributeOfDefaultStyle(CheckBox.class, android.R.attr.checkboxStyle);
        instance.registerAttributeOfDefaultStyle(RadioButton.class, android.R.attr.radioButtonStyle);
        instance.registerAttributeOfDefaultStyle(ToggleButton.class,
                android.R.attr.buttonStyleToggle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            instance.registerAttributeOfDefaultStyle(CheckedTextView.class,
                    android.R.attr.checkedTextViewStyle);
        }
    }

    /**
     * Register the theme attribute that reference the default style to be used by a view.
     * This attribute will be used in the cases where you define a view in a layout XML,
     * and you don't define a {@code customTypeface} or {@code style} specifying a
     * {@code customTypeface}. In this cases {@code CustomTypeface} will try to apply
     * the {@code customTypeface} found in the default style of that view.
     *
     * @param clazz          a {@code Class} of a view extending {@code TextView}
     * @param themeAttribute an integer with the number of the theme attribute
     */
    public void registerAttributeOfDefaultStyle(Class<? extends TextView> clazz, int themeAttribute) {
        mDefStyleAttrs.put(clazz, themeAttribute);
    }

    /**
     * Returns the {@link Typeface} that was registered with the specified name.
     *
     * @param typefaceName a {@code String} with the name of the registered {@code TypeFace}
     * @return a {@link Typeface} or null if not found
     */
    public Typeface getTypeface(String typefaceName) {
        return mTypefaces.get(typefaceName);
    }

    /**
     * Register a {@link Typeface} with the specified name. This name will be able to be referenced
     * using a {@code customTypeface} attribute in the layout files, in order to apply the
     * registered {@code Typeface} to a a view.
     *
     * @param typefaceName a name that will identify this {@code Typeface}
     * @param typeface     a {@link Typeface} instance to register
     */
    public void registerTypeface(String typefaceName, Typeface typeface) {
        mTypefaces.put(typefaceName, typeface);
    }

    /**
     * This is a shortcut to let {@code CustomTypeface} create directly a {@link Typeface} from a
     * file located in the assets directory. For more information see the {@link
     * #registerTypeface(String, Typeface)} method.
     *
     * @param typefaceName a name that will identify this {@code Typeface}
     * @param assets       a instance of {@link AssetManager}
     * @param filePath     a path to a TTF file located inside the assets folder
     * @see #registerTypeface(String, Typeface)
     */
    public void registerTypeface(String typefaceName, AssetManager assets, String filePath) {
        Typeface typeface = Typeface.createFromAsset(assets, filePath);
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
            registerAttributesOfDefaultStyles(instance);
        }
    }
}
