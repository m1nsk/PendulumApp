package com.github.douglasjunior.bluetoothsample.device;

import java.io.IOException;

public interface Storage<T> {
    T loadData() throws IOException;
    void storeData(T data) throws IOException;
}
