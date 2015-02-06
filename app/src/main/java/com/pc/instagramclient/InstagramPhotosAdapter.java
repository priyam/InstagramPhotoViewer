package com.pc.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by priyam on 2/5/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    //What data do we need from the activity ??
    //Context, Data Siyrce
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //what our item looks like
    //Use the template to display each photo


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for the position
        InstagramPhoto photo = getItem(position);
        //check if we are using the recycled view, if not we need to inflate
        if (convertView == null)
        {
            //create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }
        //lookup the views for populating the data (image, caption)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        //insert the model data into each of the view item
        tvCaption.setText(photo.caption);
        //clear out the image view
        ivPhoto.setImageResource(0);
        //insert the image using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        //return the created item as a view
        return convertView;
    }
}
