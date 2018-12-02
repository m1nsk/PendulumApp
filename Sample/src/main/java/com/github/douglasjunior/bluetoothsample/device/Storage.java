package com.github.douglasjunior.bluetoothsample.device;

import java.io.IOException;

public interface Storage {
    void loadData() throws IOException;
    void storeData(DeviceData data) throws IOException;
}
