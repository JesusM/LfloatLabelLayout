package com.example.jesus.textfielderror.customui;/*
 * Copyright (C) 2014 Chris Banes
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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

    @InjectView(R.id.textView)
    EditText mEditText;
    @InjectView(R.id.textfield_error_container)
    View errorContainer;
    @InjectView(R.id.textFieldLabel)
    TextView fieldLabel;
    @InjectView(R.id.edittext_field_bottom_line)
    View bottomLine;
    @InjectView(R.id.textFieldErrorIcon)
    ImageView errorIcon;


    public FloatLabelLayout(Context context) {
        this(context, null);
    }

    public FloatLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLabelLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.edittextfield, this, true);
        ButterKnife.inject(this);
        initTextWatcher();
        setOrientation(VERTICAL);
    }

    public void setError(CharSequence error, Drawable icon) {
        if (icon != null) {
            errorIcon.setImageDrawable(getColoredDrawable(icon,
                    getResources().getColor(R.color.float_label_error)));
        }

        showLabel(error);
    }

    public void setError(CharSequence error) {
        showLabel(error);
    }

    public void clearError() {
        hideLabel();
    }


    private void initTextWatcher() {
        getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkAfterTextChanged(editable);

            }
        });
    }


    protected void checkAfterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            // The text is empty, so hide the label if it is visible
            if (fieldLabel.getVisibility() == View.VISIBLE) {
                hideLabel();
            }
        } else {
            actionTextNotEmpty();
        }
    }

    protected void actionTextNotEmpty() {
        // The text is not empty, so show the label if it is not visible
        if (fieldLabel.getVisibility() != View.VISIBLE) {
            showLabel("");
        }

    }

    /**
     * @return the {@link android.widget.EditText} text input
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * @return the {@link android.widget.TextView} label
     */
    public TextView getLabel() {
        return fieldLabel;
    }

    /**
     * Show the label using an animation
     */
    private void showLabel(final CharSequence error) {
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
        mEditText.setEnabled(enabled);
        bottomLine.setEnabled(enabled);
        if (!enabled) {
            hideLabel();
        }
    }

    /**
     * Hide the label using an animation
     */
    private void hideLabel() {
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

                                }

        );
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.playTogether(alpha, translationY);
        animatorSet.start();

    }

    public Drawable getColoredDrawable(Drawable drawable, int targetColor) {
        ColorFilter filter = new LightingColorFilter(targetColor, 0);
        drawable.mutate().setColorFilter(filter);
        return drawable;
    }


}