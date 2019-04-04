/*
 * MIT License
 *
 * Copyright (c) 2015 Douglas Nassif Roma Junior
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.minsk.BigFuckingStick;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.douglasjunior.bluetoothsample.R;
import com.minsk.BigFuckingStick.device.Device;

import java.io.IOException;

public class PropertiesActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final Integer LED_PER_METER = 144;

    private Device device;
    private SeekBar seekBarLedNum;
    private SeekBar seekOffsetNum;
    private SeekBar seekBrightness;
    private TextView ledValue;
    private TextView offsetValue;
    private TextView brightnessValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        device = Device.getInstance();
        device.setStorage(getFilesDir());

        seekBarLedNum = (SeekBar) findViewById(R.id.seek_led_num);
        seekBarLedNum.setProgress(device.getLedNum());

        seekOffsetNum = (SeekBar) findViewById(R.id.seek_offset_num);
        seekOffsetNum.setProgress(device.getLedNum());

        seekBrightness = (SeekBar) findViewById(R.id.seek_brightness);
        seekBrightness.setProgress(device.getBrightness());

        ledValue = (TextView) findViewById(R.id.l_value);
        ledValue.setText(String.valueOf(seekBarLedNum.getProgress() * LED_PER_METER));
        offsetValue = (TextView) findViewById(R.id.o_value);
        offsetValue.setText(String.valueOf(seekOffsetNum.getProgress() * LED_PER_METER));
        brightnessValue = (TextView) findViewById(R.id.b_value);

        String bValue = Double.valueOf((double)seekBrightness.getProgress() / seekBrightness.getMax()).toString();
        brightnessValue.setText(bValue);

        seekBrightness.setOnSeekBarChangeListener(this);
        seekBarLedNum.setOnSeekBarChangeListener(this);
        seekOffsetNum.setOnSeekBarChangeListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            device.saveToStorage();
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "storage failure", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        device.setLedNum(seekBarLedNum.getProgress());
        device.setBrightness(seekBrightness.getProgress() / seekBrightness.getMax());
        device.setOffset(seekOffsetNum.getProgress());
        String lValue = String.valueOf(seekBarLedNum.getProgress() * LED_PER_METER);
        ledValue.setText(lValue);
        String bValue = Double.valueOf((double)seekBrightness.getProgress() / seekBrightness.getMax()).toString();
        brightnessValue.setText(bValue);
        String oValue = String.valueOf(seekOffsetNum.getProgress() * LED_PER_METER);
        offsetValue.setText(oValue);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
