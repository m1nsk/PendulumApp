package com.github.douglasjunior.bluetoothsample.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StorageImpl implements Storage {
    private File storage;
    private static final String IMAGE_FILE = "images";
    private static final String PARAM_FILE = "parameters";
    private ObjectMapper objectMapper;

    public StorageImpl(File storage) {
        this.storage = storage;
    }

    @Override
    public List<File> loadData() throws IOException {
        File file = new File(storage.getPath() + "/" + IMAGE_FILE);
        try {
            List<String> list = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, String[].class)));
            return list.stream().map(File::new).collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    @Override
    public void storeData(List<File> list) throws IOException {
        String imgDir = storage.getPath() + "/" + IMAGE_FILE;
        for (File item : list) {
            FileUtils.copyFile(item.getPath(),  imgDir + "/" + item.getName());
        }
        List<String> paths = list.stream().map(File::getName).collect(Collectors.toList());
        objectMapper.writeValue(new File(imgDir), paths);
    }
}
