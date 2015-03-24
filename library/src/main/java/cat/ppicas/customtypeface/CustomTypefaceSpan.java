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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

import java.util.HashMap;
import java.util.Map;

/**
 * Changes the typeface of the text to which the span is attached. You can attach this object
 * as a markup inside an {@link Spannable} class, to modify the typeface of the selected part
 * of the text.
 *
 * @see android.text.Spannable
 * @see android.text.SpannableString
 * @see android.text.SpannableStringBuilder
 */
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

    /**
     * Creates a new {@link Spanned} {@link CharSequence} that has applied
     * {@link CustomTypefaceSpan} along the whole string.
     *
     * @param charSequence a {@code CharSequence} containing the text that you want stylize
     * @param typeface     the {@code Typeface} that you want to be applied on the text
     * @return a new {@code CharSequence} with the {@code CustomTypefaceSpan} applied from the
     * beginning to the end
     * @see Spannable#setSpan
     */
    public static CharSequence createText(CharSequence charSequence, Typeface typeface) {
        Spannable spannable = new SpannableString(charSequence);
        spannable.setSpan(getInstance(typeface), 0, spannable.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    /**
     * Applies a {@link CustomTypefaceSpan} along the whole string to the passed
     * {@link CharSequence}.
     *
     * <p>
     * If {@code charSequence} implements {@link Spannable}, the same object will be modified
     * an returned as result. Otherwise a new object will be created and returned as result.
     * This behavior can be used to modify existing {@code CharSequence} without the need
     * of creating a new object.
     * </p>
     *
     * @param charSequence a {@code CharSequence} to apply the styles
     * @param typeface     the {@code Typeface} that you want to be applied on the text
     * @return the existing {@code CharSequence}, if the passed object implements {@link Spannable}.
     * Otherwise a new object is returned with the {@code CustomTypefaceSpan} applied from the
     * beginning to the end.
     * @see Spannable#setSpan
     */
    public static CharSequence applyToText(CharSequence charSequence, Typeface typeface) {
        return applyToText(charSequence, typeface, 0, charSequence.length());
    }

    /**
     * This method does the same as {@link #applyToText(CharSequence, Typeface)}, but let you
     * specify the start and end index where the span will be applied.
     *
     * @param charSequence a {@code CharSequence} to apply the styles
     * @param typeface     the {@code Typeface} that you want to be applied on the text
     * @param start        the start index where to apply the span
     * @param end          the end index where to apply the span
     * @return the existing {@code CharSequence}, if the passed object implements {@link Spannable}.
     * Otherwise a new object is returned with the {@code CustomTypefaceSpan} applied from
     * {@code start} to {@code end}.
     * @see Spannable#setSpan
     */
    public static CharSequence applyToText(CharSequence charSequence, Typeface typeface,
            int start, int end) {
        Spannable spannable;
        if (charSequence instanceof Spannable) {
            spannable = (Spannable) charSequence;
        } else {
            spannable = SpannableString.valueOf(charSequence);
        }
        spannable.setSpan(getInstance(typeface), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
