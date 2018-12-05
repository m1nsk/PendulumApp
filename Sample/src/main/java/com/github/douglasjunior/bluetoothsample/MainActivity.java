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

package com.github.douglasjunior.bluetoothsample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothDeviceDecorator;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;
import com.github.douglasjunior.bluetoothsample.device.Device;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BluetoothService.OnBluetoothScanCallback, BluetoothService.OnBluetoothEventCallback, DeviceItemAdapter.OnAdapterItemClickListener {

    public static final String TAG = "BluetoothExample";

    private ArrayList<Image> images = new ArrayList<>();
    private ProgressBar pgBar;
    private Button ipBtn;
    private Menu mMenu;
    private RecyclerView mRecyclerView;
    private DeviceItemAdapter mAdapter;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothService mService;
    private boolean mScanning;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pgBar = (ProgressBar) findViewById(R.id.pg_bar);
        pgBar.setVisibility(View.GONE);

        ipBtn = (Button) findViewById(R.id.ip_btn);
        ipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        ipBtn.setVisibility(View.VISIBLE);

        ipBtn = (Button) findViewById(R.id.gallery_btn);
        ipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GalleryActivity.class));
            }
        });
        ipBtn.setVisibility(View.VISIBLE);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(lm);

        mAdapter = new DeviceItemAdapter(this, mBluetoothAdapter.getBondedDevices());
        mAdapter.setOnAdapterItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mService = BluetoothService.getDefaultInstance();

        mService.setOnScanCallback(this);
        mService.setOnEventCallback(this);

        try {
            device = Device.getInstance();
            device.setStorage(getFilesDir());
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "storage failure", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mService.setOnEventCallback(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_scan) {
            startStopScan();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startStopScan() {
        if (!mScanning) {
            mService.startScan();
        } else {
            mService.stopScan();
        }
    }

    @Override
    public void onDeviceDiscovered(BluetoothDevice device, int rssi) {
        Log.d(TAG, "onDeviceDiscovered: " + device.getName() + " - " + device.getAddress() + " - " + Arrays.toString(device.getUuids()));
        BluetoothDeviceDecorator dv = new BluetoothDeviceDecorator(device, rssi);
        int index = mAdapter.getDevices().indexOf(dv);
        if (index < 0) {
            mAdapter.getDevices().add(dv);
            mAdapter.notifyItemInserted(mAdapter.getDevices().size() - 1);
        } else {
            mAdapter.getDevices().get(index).setDevice(device);
            mAdapter.getDevices().get(index).setRSSI(rssi);
            mAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void onStartScan() {
        Log.d(TAG, "onStartScan");
        mScanning = true;
        pgBar.setVisibility(View.VISIBLE);
        mMenu.findItem(R.id.action_scan).setTitle(R.string.action_stop);
    }

    @Override
    public void onStopScan() {
        Log.d(TAG, "onStopScan");
        mScanning = false;
        pgBar.setVisibility(View.GONE);
        mMenu.findItem(R.id.action_scan).setTitle(R.string.action_scan);
    }

    @Override
    public void onDataRead(byte[] buffer, int length) {
        Log.d(TAG, "onDataRead");
    }

    @Override
    public void onStatusChange(BluetoothStatus status) {
        Log.d(TAG, "onStatusChange: " + status);
        Toast.makeText(this, status.toString(), Toast.LENGTH_SHORT).show();

        if (status == BluetoothStatus.CONNECTED) {
            CharSequence colors[] = new CharSequence[]{"Try text", "Try picture"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select");
            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        startActivity(new Intent(MainActivity.this, DeviceActivity.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, BitmapActivity.class));
                    }
                }
            });
            builder.setCancelable(false);
            builder.show();
        }

    }

    @Override
    public void onDeviceName(String deviceName) {
        Log.d(TAG, "onDeviceName: " + deviceName);
    }

    @Override
    public void onToast(String message) {
        Log.d(TAG, "onToast");
    }

    @Override
    public void onDataWrite(byte[] buffer) {
        Log.d(TAG, "onDataWrite");
    }

    @Override
    public void onItemClick(BluetoothDeviceDecorator device, int position) {
        mService.connect(device.getDevice());
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            List<File> cachedImages = new ArrayList<>();

            images.forEach(image -> {
                try {
                    String location = getCacheDir().getPath() + image.getName();
                    FileUtils.copyFile(image.getPath(), location);
                    cachedImages.add(new File(location));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            device.addToImageList(cachedImages);
            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
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
}
