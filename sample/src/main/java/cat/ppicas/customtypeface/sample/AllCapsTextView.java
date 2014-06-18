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

import android.content.Context;
import android.graphics.Rect;
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
