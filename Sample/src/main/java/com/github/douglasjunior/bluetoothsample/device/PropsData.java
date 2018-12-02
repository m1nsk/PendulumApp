package com.github.douglasjunior.bluetoothsample.device;

public interface PropsData {
    void setLedNum(Integer ledNum);
    void setBrightness(Integer percents);
    Integer getLedNum();
    Integer getBrightness();
    void refreshData();
    void saveToStorage();
}
