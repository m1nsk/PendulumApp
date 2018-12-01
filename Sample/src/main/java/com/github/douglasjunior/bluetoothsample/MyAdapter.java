package com.github.douglasjunior.bluetoothsample;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.douglasjunior.bluetoothsample.helper.ItemTouchHelperAdapter;
import com.github.douglasjunior.bluetoothsample.helper.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<CreateList> galleryList;
    private Context context;
    private final OnStartDragListener mDragStartListener;

    public MyAdapter(Context context, ArrayList<CreateList> galleryList, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder viewHolder, int i) {
        final int imgId = i;
        viewHolder.title.setText(galleryList.get(i).getImage_title());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageURI((galleryList.get(i).getImage_location()));
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CropActivity.class);
                intent.putExtra("image", galleryList.get(imgId).getImage_location().toString());
                context.startActivity(intent);
            }
        });
        viewHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        v);
                // start dragging the item touched
                mDragStartListener.onStartDrag(viewHolder);
                return true;
            }
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
