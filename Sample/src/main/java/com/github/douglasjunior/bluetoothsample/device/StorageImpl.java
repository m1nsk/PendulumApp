package com.github.douglasjunior.bluetoothsample.device;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageImpl implements Storage {
    DeviceData deviceData = DeviceData.getInstance();
    private File storage;
    private static final String DEVICE = "device";
    private ObjectMapper objectMapper;

    public StorageImpl(File storage) {
        this.storage = storage;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void loadData() throws IOException {
        File imagesFile = new File(storage.getPath() + "/" + DEVICE + "/images.txt");
        File propsFile = new File(storage.getPath() + "/" + DEVICE + "/props.txt");
        List<String> imageList = new ArrayList<>(Arrays.asList(objectMapper.readValue(imagesFile, String[].class)));
        Map<String, String> props = objectMapper.readValue(propsFile, new TypeReference<Map<String, String>>(){});
        List<File> images = imageList.stream().map(File::new).collect(Collectors.toList());
        deviceData.setImages(images);
        deviceData.setProps(props);

    }

    @Override
    public void storeData(DeviceData data) throws IOException {
        saveProps(data.getProps());
        saveImages(data.getImages());
    }

    private void saveProps(Map<String, String> props) throws IOException {
        String imgDir = storage.getPath() + "/" + DEVICE;
        File file = new File(imgDir + "/props.txt");
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, props);
    }


    private void saveImages(List<File> images) throws IOException {
        String imgDir = storage.getPath() + "/" + DEVICE;
        new File(imgDir).mkdir();
        for (File item : images) {
            File file = new File(imgDir + "/" + item.getName());
            copy(item, file);
        }
        List<String> paths = images.stream().map(File::getPath).collect(Collectors.toList());
        File file = new File(imgDir + "/images.txt");
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(file, paths);
    }

    private void copy(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }
}
