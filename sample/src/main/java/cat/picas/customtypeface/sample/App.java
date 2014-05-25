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

        // Register the theme attribute that reference the default style to be used with
        // AllCapsTextView widget. This is necessary because AllCapsTextView is a custom
        // widget defined in our project. But this also can be used if you want to style
        // any widget providing from any third party library.
        CustomTypeface.getInstance().registerAttributeForDefaultStyle(AllCapsTextView.class,
                R.attr.allCapsTextViewStyle);
    }
}
