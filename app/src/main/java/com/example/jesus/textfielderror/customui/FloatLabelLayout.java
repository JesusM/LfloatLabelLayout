/* Copyright (C) 2014 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jesus.textfielderror.customui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jesus.textfielderror.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Layout which an {@link android.widget.EditText} to show a floating label when the hint is hidden
 * due to the user inputting text.
 *
 * @see <a href="https://dribbble.com/shots/1254439--GIF-Mobile-Form-Interaction">Matt D. Smith on Dribble</a>
 * @see <a href="http://bradfrostweb.com/blog/post/float-label-pattern/">Brad Frost's blog post</a>
 */

public class FloatLabelLayout extends LinearLayout {


    private static final long ANIMATION_DURATION = 150;

    @InjectView(R.id.edit_text)
    EditText editText;
    @InjectView(R.id.textfield_error_container)
    View errorContainer;
    @InjectView(R.id.textFieldLabel)
    TextView fieldLabel;
    @InjectView(R.id.edittext_field_bottom_line)
    View bottomLine;
    @InjectView(R.id.textFieldErrorIcon)
    ImageView errorIcon;

    private TextView label;

    public FloatLabelLayout(Context context) {
        this(context, null);
    }

    public FloatLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.float_label_textfield, this, true);
        ButterKnife.inject(this);
        initEditText();
        initLabel();
        setOrientation(VERTICAL);
    }

    private void initEditText() {
        editText.setTypeface(getRegularTypeFace());
        initFocusListener();
        initTextWatcher();
    }

    private void initFocusListener() {
        // Add focus listener to the EditText so that we can notify the label that it is activated.
        // Allows the use of a ColorStateList for the text color on the label
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                label.setActivated(focused);
            }
        });
    }

    private void initTextWatcher() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (!TextUtils.isEmpty(charSequence)) {
                    clearError();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                handleLabelStateForTextChanged(editable);
            }

            private void handleLabelStateForTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    // The text is empty, so hide the label if it is visible
                    if (label.getVisibility() == View.VISIBLE) {
                        hideLabel();
                    }
                } else {
                    // The text is not empty, so show the label if it is not visible
                    if (label.getVisibility() != View.VISIBLE) {
                        showLabel();
                    }
                }
            }
        });
    }

    private void initLabel() {
        label = new TextView(getContext());
        label.setVisibility(INVISIBLE);
        label.setTextColor(getContext().getResources().getColor(R.color.float_label));
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimensionPixelSize(R.dimen.edit_text_label_text_size));
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(label, 0, layoutParams);

    }

    public void setError(CharSequence error, Drawable icon) {
        if (icon != null) {
            errorIcon.setImageDrawable(getColoredDrawable(icon,
                    getResources().getColor(R.color.float_label_error)));
        }
        showErrorLabel(error);
    }

    public void setError(CharSequence error) {
        showErrorLabel(error);
    }

    public void clearError() {
        bottomLine.setBackgroundColor(getResources().getColor(R.color.float_label));
        hideErrorLabel();
    }

    public void setHint(String hint) {
        editText.setHint(hint);
        label.setText(editText.getHint());
    }

    /**
     * Show the label using an animation
     */
    private void showLabel() {
        label.setVisibility(View.VISIBLE);
        label.setAlpha(0f);
        label.setTranslationY(label.getHeight());
        label.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(ANIMATION_DURATION)
                .setListener(null).start();
    }

    /**
     * Hide the label using an animation
     */
    private void hideLabel() {
        label.setAlpha(1f);
        label.setTranslationY(0f);
        label.animate()
                .alpha(0f)
                .translationY(label.getHeight())
                .setDuration(ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        label.setVisibility(View.INVISIBLE);
                    }
                }).start();
    }

    /**
     * Show the label using an animation
     */
    private void showErrorLabel(final CharSequence error) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(errorContainer, "alpha", 0f, 1f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(errorContainer, "translationY",
                -fieldLabel.getHeight(), 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(alpha, translationY);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                errorContainer.setVisibility(VISIBLE);
                fieldLabel.setText(error);
                bottomLine.setBackgroundColor(getResources().getColor(R.color.float_label_error));
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();


    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        editText.setEnabled(enabled);
        bottomLine.setEnabled(enabled);
        if (!enabled) {
            hideErrorLabel();
        }
    }

    /**
     * Hide the label using an animation
     */
    private void hideErrorLabel() {
        errorContainer.setAlpha(1f);
        errorContainer.setTranslationY(0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(errorContainer, "alpha", 0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(errorContainer, "translationY",
                -fieldLabel.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                bottomLine.setBackgroundResource(
                        R.drawable.fieldtext_bottomline_color);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                errorContainer.setVisibility(INVISIBLE);
                fieldLabel.setText(null);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(alpha, translationY);
        animatorSet.start();

    }

    public Drawable getColoredDrawable(Drawable drawable, int targetColor) {
        ColorFilter filter = new LightingColorFilter(targetColor, 0);
        drawable.mutate().setColorFilter(filter);
        return drawable;
    }

    public Typeface getRegularTypeFace() {
        return Typeface.createFromAsset(getContext().getAssets(), "Roboto-Regular.ttf");
    }

}