/**
 * Copyright (C) 2014 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package cat.ppicas.customtypeface;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * An implementation of {@link LayoutInflater.Factory} that creates a {@link View} and applies
 * on it a custom {@link Typeface} calling {@link CustomTypeface#applyTypeface(View, AttributeSet)}.
 *
 * <p>
 * This class can also accept another {@code LayoutInflater.Factory} implementation to delegate
 * the view creation first. If the delegated {@code LayoutInflater.Factory} returns
 * {@code null} on the view creation, this class will then use the internal
 * {@code View} factory. Please use {@link #CustomTypefaceFactory(Context, CustomTypeface,
 * LayoutInflater.Factory)} if you want this class to use a delegate {@code LayoutInflater.Factory}.
 * </p>
 *
 * <p>
 * Please, take in account that {@link LayoutInflater} only accepts the
 * {@link LayoutInflater.Factory} to be set once. If you call {@link LayoutInflater#setFactory}
 * on a {@code LayoutInflater} that already has one, it will throw a {@code RuntimeException}.
 * This is why we recommend you to set the {@code Factory} before calling
 * super.{@link Activity#onCreate onCreate} when you are overriding {@code Activity.onCreate}
 * on your own {@code Activity}. Please, check the following example:
 * </p>
 *
 * <pre><code>
 * public class MainActivity extends Activity {
 *
 *     // ...
 *
 *     {@literal @Override}
 *     protected void onCreate(Bundle savedInstanceState) {
 *         getLayoutInflater().setFactory(new CustomTypefaceFactory(
 *                 this, CustomTypeface.getInstance()));
 *
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_main);
 *     }
 *
 *     // ...
 *
 * }
 * </code></pre>
 *
 * <p>
 * For more info on how to use this class in combination with {@link CustomTypeface} please
 * check the {@link CustomTypeface documentation} of that class.
 * </p>
 *
 * @see CustomTypeface
 * @see LayoutInflater.Factory
 */
public class CustomTypefaceFactory implements LayoutInflater.Factory {

    private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class[] {
            Context.class, AttributeSet.class};

    private static final HashMap<String, Constructor<? extends View>> CONSTRUCTOR_MAP =
            new HashMap<String, Constructor<? extends View>>();

    private final Object[] mConstructorArgs = new Object[2];

    private final Context mContext;

    private final CustomTypeface mCustomTypeface;

    private LayoutInflater.Factory mFactory;

    public CustomTypefaceFactory(Context context, CustomTypeface customTypeface) {
        this(context, customTypeface, null);
    }

    public CustomTypefaceFactory(Context context, CustomTypeface customTypeface,
            LayoutInflater.Factory factory) {
        mContext = context;
        mCustomTypeface = customTypeface;
        mFactory = factory;
    }

    public LayoutInflater.Factory getFactory() {
        return mFactory;
    }

    public void setFactory(LayoutInflater.Factory factory) {
        mFactory = factory;
    }

    /**
     * Implements {@link LayoutInflater.Factory} interface. Inflate the {@link View} for the
     * specified tag name and apply custom {@link Typeface} if is required. This
     * method first will delegate to the {@code LayoutInflater.Factory} if it was specified on the
     * constructor. When the {@code View} is created, this method will call {@link
     * CustomTypeface#applyTypeface(View, AttributeSet)} on it.
     *
     * <p>
     * This method can be used to delegate the implementation of {@link
     * LayoutInflater.Factory#onCreateView}
     * or {@link LayoutInflater.Factory2#onCreateView}.
     * </p>
     *
     * @param name    Tag name to be inflated.
     * @param context The context the view is being created in.
     * @param attrs   Inflation attributes as specified in XML file.
     * @return Newly created view.
     * @see LayoutInflater.Factory
     * @see CustomTypeface#applyTypeface(View, AttributeSet)
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        String prefix = null;
        if (name.indexOf('.') == -1) {
            prefix = "android.widget.";
        }
        try {
            View view = null;
            if (mFactory != null) {
                view = mFactory.onCreateView(name, context, attrs);
            }
            if (view == null) {
                view = createView(name, prefix, context, attrs);
            }
            mCustomTypeface.applyTypeface(view, attrs);
            return view;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Low-level function for instantiating a view by name. This attempts to
     * instantiate a view class of the given <var>name</var> found in this
     * LayoutInflater's ClassLoader.
     *
     * <p>
     * There are two things that can happen in an error case: either the
     * exception describing the error will be thrown, or a null will be
     * returned. You must deal with both possibilities -- the former will happen
     * the first time createView() is called for a class of a particular name,
     * the latter every time there-after for that class name.
     *
     * @param name    The full name of the class to be instantiated.
     * @param context The Context in which this LayoutInflater will create its
     *                Views; most importantly, this supplies the theme from which the default
     *                values for their attributes are retrieved.
     * @param attrs   The XML attributes supplied for this instance.
     * @return View The newly instantiated view, or null.
     */
    private View createView(String name, String prefix, Context context, AttributeSet attrs)
            throws ClassNotFoundException, InflateException {

        Constructor<? extends View> constructor = CONSTRUCTOR_MAP.get(name);
        Class<? extends View> clazz = null;

        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                clazz = mContext.getClassLoader().loadClass(
                        prefix != null ? (prefix + name) : name).asSubclass(View.class);
                constructor = clazz.getConstructor(CONSTRUCTOR_SIGNATURE);
                constructor.setAccessible(true);
                CONSTRUCTOR_MAP.put(name, constructor);
            }

            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;

            return constructor.newInstance(mConstructorArgs);
        } catch (NoSuchMethodException e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;

        } catch (ClassCastException e) {
            // If loaded class is not a View subclass
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Class is not a View "
                    + (prefix != null ? (prefix + name) : name));
            ie.initCause(e);
            throw ie;
        } catch (ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            throw e;
        } catch (Exception e) {
            InflateException ie = new InflateException(attrs.getPositionDescription()
                    + ": Error inflating class "
                    + (clazz == null ? "<unknown>" : clazz.getName()));
            ie.initCause(e);
            throw ie;
        }
    }
}
