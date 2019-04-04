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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Button;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.github.douglasjunior.bluetoothsample.R;
import com.minsk.BigFuckingStick.device.Device;
import com.minsk.BigFuckingStick.helper.OnStartDragListener;
import com.minsk.BigFuckingStick.helper.SimpleItemTouchHelperCallback;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity implements OnStartDragListener {
    private List<File> imgList;
    private ItemTouchHelper mItemTouchHelper;
    private GalleryAdapter adapter;
    private RecyclerView recyclerView;
    private Button addImagesBtn;

    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        device = Device.getInstance();
        imgList = device.getImageList();

        setContentView(R.layout.activity_gallery);

        recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GalleryAdapter(getApplicationContext(), imgList, this, GalleryActivity.this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        addImagesBtn = (Button) findViewById(R.id.btn_add_images);
        addImagesBtn.setOnClickListener(v -> start());
    }

    public void start() {
        ImagePicker.create(this)
                .folderMode(true)
                .limit(15)
                .showCamera(false)
                .start();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onRemove(RecyclerView.ViewHolder viewHolder) {
        viewHolder.getItemId();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
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
        }
        recyclerView.setAdapter(adapter);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        device.setImageList(imgList);
        try {
            device.saveToStorage();
        } catch (IOException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "storage failure", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


}
