package com.minsk.BigFuckingStick.ImageUtils;

//package com.day.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {

    public static File resizeImageAndConvertToFile(File file, Integer width, Integer height) throws IOException {
        Bitmap image = BitmapFactory.decodeFile(file.getPath());
        Matrix matrix = new Matrix();
        matrix.postScale(width, height);
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0,
                image.getWidth(), image.getHeight(), matrix, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        File outputFile = new File(file.getParent() + "/_" + file.getName());
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try(OutputStream outputStream = new FileOutputStream(outputFile)) {
            baos.writeTo(outputStream);
        }
        return outputFile;
    }
}