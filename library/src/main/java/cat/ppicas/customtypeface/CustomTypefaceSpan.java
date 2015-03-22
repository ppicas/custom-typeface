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

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import java.util.HashMap;
import java.util.Map;

public class CustomTypefaceSpan extends MetricAffectingSpan {

    private static final Map<Typeface, CustomTypefaceSpan> INSTANCE_MAP
            = new HashMap<Typeface, CustomTypefaceSpan>();

    private final Typeface mTypeface;

    CustomTypefaceSpan(Typeface typeface) {
        mTypeface = typeface;
    }

    public static CustomTypefaceSpan getInstance(Typeface typeface) {
        if (INSTANCE_MAP.containsKey(typeface)) {
            return INSTANCE_MAP.get(typeface);
        } else {
            CustomTypefaceSpan instance = new CustomTypefaceSpan(typeface);
            INSTANCE_MAP.put(typeface, instance);
            return instance;
        }
    }

    public static CharSequence createText(CharSequence charSequence, Typeface typeface) {
        SpannableString spannable = SpannableString.valueOf(charSequence);
        spannable.setSpan(getInstance(typeface), 0, spannable.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    @Override
    public void updateDrawState(TextPaint drawState) {
        apply(drawState);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        apply(paint);
    }

    private void apply(Paint paint) {
        Typeface oldTypeface = paint.getTypeface();
        int oldStyle = oldTypeface != null ? oldTypeface.getStyle() : 0;
        int fakeStyle = oldStyle &~ mTypeface.getStyle();

        if ((fakeStyle & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fakeStyle & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(mTypeface);
    }
}
