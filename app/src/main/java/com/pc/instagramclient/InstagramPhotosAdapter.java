package com.pc.instagramclient;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private static Typeface font;

    private static class ViewHolder {
        TextView caption;
        TextView userName;
        TextView likesCount;
        TextView creationTime;
        ImageView photo;
        ImageView userPic;

    }
    //What data do we need from the activity ??
    //Context, Data Siyrce
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        // Create the TypeFace from the TTF asset
       font = Typeface.createFromAsset(getContext().getAssets(), "fonts/OpenSans-Regular.ttf");
    }

    //what our item looks like
    //Use the template to display each photo


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for the position
        InstagramPhoto photo = getItem(position);
        ViewHolder viewHolder;

        //check if we are using the recycled view, if not we need to inflate
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            //create a new view from template
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_photo, parent, false);
            //lookup the views for populating the data (image, caption) and set the viewHolder
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.photo = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.likesCount = (TextView) convertView.findViewById(R.id.tvLikes);
            viewHolder.creationTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.userPic = (ImageView) convertView.findViewById(R.id.ivUserPic);

            // Assign the typeface to the view
            viewHolder.caption.setTypeface(font);
            convertView.setTag(viewHolder);


        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        // insert the model data into each of the view item
        viewHolder.caption.setText(photo.getCaption());

        viewHolder.userName.setText(photo.getUsername());

        String formattedLikesText = "<b>"+photo.getLikesCount()+"</b>"+" <i>likes</i>";
        viewHolder.likesCount.setText(Html.fromHtml(formattedLikesText));
        String relativeTime = DateUtils.getRelativeTimeSpanString(photo.getCreatedTime() * 1000, System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL).toString();
        viewHolder.creationTime.setText(relativeTime);
        //clear out the image view
        viewHolder.photo.setImageResource(0);
        viewHolder.userPic .setImageResource(0);
        //insert the image using picasso
        Picasso.with(getContext()).load(photo.getImageUrl()).into(viewHolder.photo);

        //User roundedImageTransformation for displaying user pic
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getContext()).load(photo.getUserPicUrl()).transform(transformation).into(viewHolder.userPic);
        //Without rounded image
        //Picasso.with(getContext()).load(photo.getUserPicUrl()).into(viewHolder.userPic);

        //return the created item as a view
        return convertView;
    }


}
