package com.github.douglasjunior.bluetoothsample.device;

import java.io.File;
import java.util.List;

public interface ImageData {
    void setStorage(Storage storage);

    List<File> getImageList();
    void setImageList(List<File> list);

    void addToImageList(List<File> list);

    void refreshData();

    void saveToStorage();
}
