package com.github.douglasjunior.bluetoothsample.device;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Device implements ImageData, PropsData {
    Storage storage;
    DeviceData deviceData;

    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
        this.deviceData = DeviceData.getInstance();
        refreshData();
    }

    @Override
    public List<File> getImageList() {
        return deviceData.getImages();
    }

    @Override
    public void setImageList(List<File> list) {
        deviceData.setImages(list);
    }

    @Override
    public void addToImageList(List<File> list) {
        deviceData.addToImageList(list);
    }

    @Override
    public void setLedNum(Integer ledNum) {
        deviceData.setLedNum(ledNum);
    }

    @Override
    public void setBrightness(Integer percent) {
        deviceData.setBrightness(percent);
    }

    @Override
    public Integer getLedNum() {
        return deviceData.getLedNum();
    }

    @Override
    public Integer getBrightness() {
        return deviceData.getBrightness();
    }

    @Override
    public void refreshData() {
        try {
            deviceData.clear();
            storage.loadData();
        } catch (IOException e) {
            deviceData.clear();
        }
    }

    @Override
    public void saveToStorage() {
        try {
            storage.storeData(deviceData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
