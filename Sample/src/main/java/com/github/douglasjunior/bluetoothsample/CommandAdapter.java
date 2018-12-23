package com.github.douglasjunior.bluetoothsample;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothsample.Protocol.Command;
import com.github.douglasjunior.bluetoothsample.Protocol.CommandType;
import com.github.douglasjunior.bluetoothsample.Protocol.DeviceCommandConverter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.ViewHolder> {
    private List<File> galleryList;
    private Activity activity;
    private Context context;
    private BluetoothService bluetoothService;
    private static DeviceCommandConverter deviceCommandConverter = new DeviceCommandConverter();

    public CommandAdapter(Context context, List<File> galleryList, Activity activity, BluetoothService bluetoothService) {
        this.galleryList = galleryList;
        this.activity = activity;
        this.context = context;
        this.bluetoothService = bluetoothService;
    }

    @Override
    public CommandAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommandAdapter.ViewHolder viewHolder, int i) {
        final String name = galleryList.get(i).getName();
        viewHolder.title.setText(galleryList.get(i).getName());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageURI(Uri.parse(galleryList.get(i).getPath()));
        viewHolder.img.setOnClickListener(v -> {
            CharSequence colors[] = new CharSequence[]{"choose", "cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Select");
            builder.setItems(colors, (dialog, which) -> {
                if (which == 0) {
                    try {
                        Command command = new Command();
                        command.setType(CommandType.IMAGE);
                        Map<String, String> map = new HashMap<>();
                        map.put("name", name);
                        command.setArgs(map);
                        this.bluetoothService.write(deviceCommandConverter.deviceCommandToBytes(command));
                    } catch (JsonProcessingException e) {
                        Toast toast = Toast.makeText(this.context ,e.getMessage(), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    return;
                }
            });
            builder.setCancelable(false);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
