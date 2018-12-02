package com.github.douglasjunior.bluetoothsample.device;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageDataImpl implements ImageData {
    private static ImageDataImpl instance;
    List<File> imageData = new ArrayList<>();
    Storage storage;

    private ImageDataImpl() {
    }

    public static ImageData getInstance(){
        if(instance == null)
            instance = new ImageDataImpl();
        return instance;
    }

    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
        refreshData();
    }

    @Override
    public List<File> getImageList() {
        return imageData;
    }

    @Override
    public void setImageList(List<File> list) {
        imageData = list;
    }

    @Override
    public void addToImageList(List<File> list) {
        imageData.addAll(list);
    }

    @Override
    public void refreshData() {
        try {
            imageData.clear();
            imageData.addAll(storage.loadData());
        } catch (IOException e) {
            imageData = new ArrayList<>();
        }
    }

    @Override
    public void saveToStorage() {
        try {
            storage.storeData(imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
