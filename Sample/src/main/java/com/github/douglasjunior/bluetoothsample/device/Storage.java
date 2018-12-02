package com.github.douglasjunior.bluetoothsample.device;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Storage {
    List<File> loadData() throws IOException;
    void storeData(List<File> list) throws IOException;
}
