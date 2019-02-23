package com.pcchin.uiplayground.gamedata;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.pcchin.uiplayground.R;

public class CustomSeekBar extends Preference implements OnSeekBarChangeListener {
    private int mProgress;

    public CustomSeekBar(Context context) {
        super(context);
        setLayoutResource(R.layout.layout_slider);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.layout_slider);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.layout_slider);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        SeekBar mSeekBar = view.findViewById(R.id.seekbar);
        mSeekBar.setProgress(mProgress);
        setSummary(Integer.toString(mProgress) + "%");
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setSummary(Integer.toString(progress) + "%");
        setValue(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // not used
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // not used
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mProgress) : (Integer) defaultValue);
    }

    private void setValue(int value) {
        if (shouldPersist()) {
            persistInt(value);
        }

        if (value != mProgress) {
            mProgress = value;
            callChangeListener(value);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }
}
