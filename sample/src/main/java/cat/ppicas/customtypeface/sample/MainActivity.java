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

package cat.ppicas.customtypeface.sample;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;

import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import cat.ppicas.customtypeface.CustomTypefaceSpan;

/**
 * Sample activity that shows how to use CustomTypeface combined with the AppCompat library.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configure the activity 'LayoutInflater' to use 'CustomTypefaceFactory' as
        // 'LayoutInflater.Factory'. It's important to call this method before 'super.onCreate()',
        // or you will not be able to set the factory because it could be already set by the
        // parent 'onCreate'.
        //
        // 'CustomTypefaceFactory' accepts a 'LayoutInflater.Factory' as a third optional parameter.
        // Please use this parameter if you want 'CustomTypefaceFactory' to first delegate the
        // 'View' creation to an specific factory. In this case we are passing 'this' as third
        // parameter because we want 'ActionBarActivity' from appcompat to do their own magic
        // to support material design on old devices.
        //
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance(), this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyCustomTypefaceToTitle();

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void applyCustomTypefaceToTitle() {
        String typefaceName = getString(R.string.typeface_audiowide);
        Typeface typeface = CustomTypeface.getInstance().getTypeface(typefaceName);

        // This is the only way to change the Typeface of a title of an ActionBar. This is
        // because the TextViews created for the ActionBars are not inflated using LayoutInflater.
        setTitle(CustomTypefaceSpan.createText(getTitle(), typeface));
    }
}
