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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothWriter;
import com.minsk.BigFuckingStick.device.Device;

import java.io.File;
import java.util.List;

public class CommandActivity extends AppCompatActivity implements BluetoothService.OnBluetoothEventCallback {
    private List<File> imgList;
    private CommandAdapter adapter;
    private RecyclerView recyclerView;
    private BluetoothService mService;
    private BluetoothWriter mWriter;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = Device.getInstance();
        imgList = device.getImageList();

        setContentView(R.layout.activity_command);

        recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        mService = BluetoothService.getDefaultInstance();
        mWriter = new BluetoothWriter(mService);

        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommandAdapter(getApplicationContext(), imgList, CommandActivity.this, mService);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mService.setOnEventCallback(this);
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mService.disconnect();
    }


    @Override
    public void onDataRead(byte[] bytes, int i) {

    }

    @Override
    public void onStatusChange(BluetoothStatus bluetoothStatus) {

    }

    @Override
    public void onDeviceName(String s) {

    }

    @Override
    public void onToast(String s) {

    }

    @Override
    public void onDataWrite(byte[] bytes) {

    }
}
