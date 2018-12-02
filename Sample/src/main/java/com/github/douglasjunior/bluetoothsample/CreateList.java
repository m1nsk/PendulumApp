package com.github.douglasjunior.bluetoothsample;

import android.net.Uri;

public class CreateList {

    private String image_title;
    private Uri image_location;

    public CreateList(String image_title, Uri image_location) {
        this.image_title = image_title;
        this.image_location = image_location;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }

    public Uri getImage_location() {
        return image_location;
    }

    public void setImage_location(Uri image_location) {
        this.image_location = image_location;
    }
}
