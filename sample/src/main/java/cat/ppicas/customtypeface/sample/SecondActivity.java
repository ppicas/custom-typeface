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
