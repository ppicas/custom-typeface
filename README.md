Custom Typeface
===============

Android library to apply custom typefaces directly from layouts, styles or themes. With
this library, apply different typefaces on your layouts it's easy as:

```xml

  ...

  <Button
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:customTypeface="permanent-marker"/>

  ...

```

Please notice the `app:customTypeface` attribute on the `Button` tag. This attribute will
let you specify any custom font typeface that it's available in TTF format. The only requirement
is that you have to copy the TTF file into the assets folder in your app.

Install
-------

Open `build.gradle` file and include the following dependency:

```
dependencies {
    compile 'cat.ppicas.customtypeface:library:2.0.0'
}
```

Usage
-----

Here is an example of the use of this library. First you should register the `Typeface` that you will use
from the XML layouts. A good place to do this is in the `Application` `onCreate` method.

```java
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
```

The next step is set `CustomTypefaceFactory` as the `Factory` for the `LayoutInflater` of each
`Activity`. It's important to call `LayoutInflater#setFactory` *before* calling
`super.Activity#onCreate`, otherwise the parent `Activity` could call `LayoutInflater#setFactory`
before you. If this happens, you will not be able to set your `Factory` because `LayoutInflater`
only accepts the `Factory` to be set once.

For you convenience you can create a base `Activity` class that overrides `Activity#onCreate`
and calls `setFactory`. This will enable you to extend this class and avoid copy-paste the same
line on each `Activity`.

```java
public class MainActivity extends Activity {

    // ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set CustomTypeface as LayoutInflater.Factory before call super.onCreate
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // ...

}
```

Now all the templates inflated in the context of this `Activity` will have applied a
custom `Typeface` if it's defined in the XML. Check the following layout file.

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    ...
    />

  ...

  <Button
    android:text="Permanent maker"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:textSize="22sp"
    app:customTypeface="permanent-marker"
    tools:ignore="MissingPrefix"/>

  ...

</LinearLayout>
```

In the previous sample you can see the use of attribute `tools:ignore="MissingPrefix"`.
This is because sometimes you will get a warning from lint that you are applying an
attribute with an invalid namespace. In this cases the ignore MissingPrefix will hide this
warnings.

Also you can use the `customTypeface` attribute in your styles, themes and
textAppearances as well. You can find some examples of this in the **sample** project.

### AppCompat compatibility

Some libraries rely on setting a `Factory` on the `LayoutInflater` to provide some additional
features. For instance if your `Activity` extends `FragmentActivity`, it could not work if
you first set `CustomTypefaceFactory` as a `Factory`. This is because `FragmentActivity`
will try to also set its own `Factory`, but it will not be able since we already set
one. A similar problem will also happen when extending `ActionBarActivity` from AppCompat library.

To solve this kind of issues, `CustomTypefaceFactory` accepts a `Factory` as third parameter
to delegate the `View` creation to another class. So it will let you to specify the `Factory`
implementation from the framework or library to no loose any functionally provided. For instance
in the case of `FragmentActivity`, we will have to pass the instance of the same `Activity`,
since is the one that implements the `Factory` interface.

Let's see an example:

```java
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 'CustomTypefaceFactory' accepts a 'LayoutInflater.Factory' as a third optional parameter.
        // Please use this parameter if you want 'CustomTypefaceFactory' to first delegate the
        // 'View' creation to an specific factory. In this case we are passing 'this' as third
        // parameter because we want 'FragmentActivity' from v4 support library to do their own
        // magic to support fragments on old devices.
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                        this, CustomTypeface.getInstance(), this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
```

### Custom views extending `TextView`

If you have a custom view with a default style defined in theme, then you must register
this theme attribute in `CustomTypeface`. To do that you can use the method
`registerAttributeForDefaultStyle`. This is because `CustomTypeface` doesn't have
a way to know what is a default style of a view, and this is why must be registered before.
Here is a sample code to register a custom view default style attribute.

```java
CustomTypeface.getInstance().registerAttributeForDefaultStyle(
    CustomTextView.class, R.attr.customTextViewStyle);
```

License
-------

    Copyright 2014 Pau Picas Sans <pau.picas@gmail.com>

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
