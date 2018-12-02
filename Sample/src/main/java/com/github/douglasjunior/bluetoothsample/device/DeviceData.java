package com.github.douglasjunior.bluetoothsample.device;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceData{
    private static DeviceData instance;
    private static final Integer LED_NUM = 144;
    private static final Integer BRIGHTNESS = 100;
    private Map<String, String> props;
    private List<File> images = new ArrayList<>();

    private DeviceData() {
    }

    public static DeviceData getInstance(){
        if(instance == null)
            instance = new DeviceData();
        return instance;
    }

    public void addToImageList(List<File> images) {
        images.addAll(images);
    }

    public Integer getLedNum() {
        return Integer.parseInt(props.getOrDefault("ledNum", LED_NUM.toString()));
    }

    public void setLedNum(Integer value) {
        props.put("ledNum", value.toString());
    }

    public Integer getBrightness() {
        return Integer.parseInt(props.getOrDefault("brightness", BRIGHTNESS.toString()));
    }

    public void setBrightness(Integer value) {
        props.put("brightness", value.toString());
    }

    public void clear() {
        this.images.clear();
        props.put("ledNum", LED_NUM.toString());
        props.put("brightness", BRIGHTNESS.toString());
    }
}
