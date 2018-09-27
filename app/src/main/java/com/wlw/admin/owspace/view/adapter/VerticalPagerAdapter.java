package com.wlw.admin.owspace.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.view.fragment.MainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 */
public class VerticalPagerAdapter extends FragmentStatePagerAdapter {

    private List<Item> dataList = new ArrayList<>();

    public VerticalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return MainFragment.newInstance(dataList.get(i));
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void setDataList(List<Item> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }
    public String getLastItemId(){
        if(dataList.size()==0){
            return "0";
        }
        return dataList.get(dataList.size()-1).getId();
    }
    public String getLastItemCreateTime(){
        if (dataList.size()==0){
            return "0";
        }
        Item item = dataList.get(dataList.size()-1);
        return item.getCreate_time();
    }
}
