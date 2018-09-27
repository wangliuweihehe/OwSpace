package com.wlw.admin.owspace.view.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author admin
 */
public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyHolder> {
    private List<Item> artList;

    public DailyAdapter(List<Item> artList) {
        this.artList = artList == null ? new ArrayList<>() : artList;
    }

    @NonNull
    @Override
    public DailyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DailyHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_daily, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DailyHolder dailyHolder, int i) {
        Item item = artList.get(i);
        String[] showTime = TimeUtil.getCalendarShowTime(item.getUpdate_time());
        if (showTime != null && showTime.length == 3) {
            dailyHolder.monthTv.setText(showTime[1] + " , " + showTime[2]);
            dailyHolder.yearTv.setText(showTime[0]);
        }
        dailyHolder.yearTv.setText(item.getCreate_time());
        Glide.with(dailyHolder.itemView.getContext()).load(item.getThumbnail()).into(dailyHolder.calendarIv);
    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    public void setArtList(List<Item> artList) {
        this.artList = artList;
        notifyDataSetChanged();
    }

    public String getLastItemId() {
        if (artList.size() == 0) {
            return "0";
        }
        return artList.get(artList.size() - 1).getId();
    }

    public String getLastItemCreateTime() {
        if (artList.size() == 0) {
            return "0";
        }
        return artList.get(artList.size() - 1).getCreate_time();
    }

    class DailyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.month_tv)
        TextView monthTv;
        @BindView(R.id.year_tv)
        TextView yearTv;
        @BindView(R.id.calendar_iv)
        ImageView calendarIv;

        public DailyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
