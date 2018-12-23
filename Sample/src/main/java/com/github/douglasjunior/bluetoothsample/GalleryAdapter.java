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

import com.github.douglasjunior.bluetoothsample.helper.ItemTouchHelperAdapter;
import com.github.douglasjunior.bluetoothsample.helper.OnStartDragListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private List<File> galleryList;
    private Context context;
    private final OnStartDragListener mDragStartListener;
    private Activity activity;

    public GalleryAdapter(Context context, List<File> galleryList, OnStartDragListener dragStartListener, Activity activity) {
        mDragStartListener = dragStartListener;
        this.galleryList = galleryList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder viewHolder, int i) {
        final String path = galleryList.get(i).getPath();
        viewHolder.title.setText(galleryList.get(i).getName());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageURI(Uri.parse(galleryList.get(i).getPath()));
        viewHolder.img.setOnClickListener(v -> {
            CharSequence colors[] = new CharSequence[]{"crop", "remove", "cancel"};

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Select");
            builder.setItems(colors, (dialog, which) -> {
                if (which == 0) {
                    Uri uri = Uri.fromFile(new File(path));
                    UCrop.of(uri, uri)
                            .withAspectRatio(200, 100)
                            .withMaxResultSize(2000, 1000)
                            .start(activity);
                } else if(which == 1){
                    galleryList.remove(viewHolder.getLayoutPosition());
                    notifyItemRemoved(viewHolder.getLayoutPosition());
                } else {
                    return;
                }
            });
            builder.setCancelable(false);
            builder.show();
        });
    }

    @Override
    public void onItemDismiss(int position) {
        galleryList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(galleryList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
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
