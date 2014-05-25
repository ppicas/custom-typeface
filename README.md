custom-typeface
===============

This library enables to apply custom typefaces with ease directly from Android layout XML files.

Install
-------

TODO...

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

The next step is override the `Activity.onCreateView()` method and delegate the view creation to
`CustomTypeface`.

```java
public class MainActivity extends Activity {

  // ...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public View onCreateView(String name, Context context, AttributeSet attrs) {
    return CustomTypeface.getInstance().createView(name, context, attrs);
  }

  // ...

}
```

Now all the templates inflated in the context of this `Activity` will apply a
custom `Typeface` if it's defined in the XML. Check following the layout file.

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

In the previous sample you see the use of attribute `tools:ignore="MissingPrefix"`.
This is because sometimes the you will get a warning from lint that you are applying an
attribute with an invalid namespace. In this cases the ignore MissingPrefix will hide this
warnings.

Also you can use the `customTypeface` attribute in your styles, themes and
textAppearances as well. You can find some examples of this in the *sample* project.

### Custom views extending `TextView`

If you have a custom view with a default style defined in theme, then you must register
this theme attribute int `CustomTypeface`. To do that you can use the method
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
