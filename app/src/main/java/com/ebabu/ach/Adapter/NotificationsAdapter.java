package com.ebabu.ach.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.ebabu.ach.Beans.Notifications;
import com.ebabu.ach.Beans.ShippingAddress;
import com.ebabu.ach.R;
import com.ebabu.ach.constants.IKeyConstants;
import com.ebabu.ach.customview.CustomTextView;

import java.util.List;

/**
 * Created by HacKy on 2/25/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Context context;
    private List<Notifications> notificationsList;
    private static final String TAG = "TAG";

    public NotificationsAdapter(Context context, List<Notifications> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_item_notifications, parent, false);
        NotificationsAdapter.ViewHolder vh = new NotificationsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Notifications notifications = notificationsList.get(position);
        if (notifications.getTitle() != null && !notifications.getTitle().isEmpty())
            holder.tvTitle.setText(notifications.getTitle());
        else
            holder.tvTitle.setText(IKeyConstants.NA);
        if (notifications.getMsg() != null && !notifications.getMsg().isEmpty())
            holder.tvMessage.setText(notifications.getMsg());
        else
            holder.tvTitle.setText(IKeyConstants.NA);
        if (notifications.getImage() != null && !notifications.getImage().isEmpty())
            new AQuery(context).id(holder.ivImage).image(notifications.getImage());
    }


    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView tvTitle, tvMessage;
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (CustomTextView) itemView.findViewById(R.id.txt_title);
            tvMessage = (CustomTextView) itemView.findViewById(R.id.txt_message);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        }
    }
}
