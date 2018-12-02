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

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.github.douglasjunior.bluetoothsample.device.ImageData;
import com.github.douglasjunior.bluetoothsample.device.ImageDataImpl;
import com.github.douglasjunior.bluetoothsample.helper.OnStartDragListener;
import com.github.douglasjunior.bluetoothsample.helper.SimpleItemTouchHelperCallback;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GalleryActivity extends AppCompatActivity implements OnStartDragListener {
    private List<File> imgList;
    private ItemTouchHelper mItemTouchHelper;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private ImageData imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageData = ImageDataImpl.getInstance();
        imgList = imageData.getImageList();

        setContentView(R.layout.activity_gallery);

        recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        final int spanCount = 2;
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        List<CreateList> createLists = prepareData();
        adapter = new MyAdapter(getApplicationContext(), new ArrayList<>(createLists), this, GalleryActivity.this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private List<CreateList> prepareData(){
        return imgList.stream()
                .map(img -> new CreateList(img.getName(), Uri.parse(img.getPath())))
                .collect(Collectors.toList());
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            recyclerView.setAdapter(adapter);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageData.setImageList(imgList);
        imageData.saveToStorage();
    }
}
