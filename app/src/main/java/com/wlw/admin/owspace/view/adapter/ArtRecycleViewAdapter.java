package com.wlw.admin.owspace.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wang.avi.AVLoadingIndicatorView;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.utils.Constant;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.view.activity.AudioDetailActivity;
import com.wlw.admin.owspace.view.activity.DetailActivity;
import com.wlw.admin.owspace.view.activity.VideoDetailActivity;
import com.wlw.admin.owspace.view.widget.OnReloadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int FOOTER_TYPE = 1001;
    private static final int CONTENT_TYPE = 1002;
    private List<Item> artList = new ArrayList<>();
    private Context context;
    private boolean hasMore = true;
    private boolean error = false;
    private OnReloadListener onReloadListener;

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArtRecycleViewAdapter(Context context) {
        this.context = context;
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == FOOTER_TYPE) {
            return new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_footer, viewGroup, false));
        } else {
            return new ArtHolder(LayoutInflater.from(context).inflate(R.layout.item_art, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i + 1 == getItemCount()) {
            bindFooterHolder(viewHolder, i);
        } else {
            bindContentHolder(viewHolder, i);
        }
    }

    private void bindContentHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (artList.size() == 0) {
            return;
        }
        if (viewHolder instanceof ArtHolder) {
            ArtHolder holder = (ArtHolder) viewHolder;
            Item item = artList.get(position);
            holder.authorTv.setText(item.getAuthor());
            holder.titleTv.setText(item.getTitle());
            Glide.with(context).load(item.getThumbnail()).apply(new RequestOptions().centerCrop()).into(holder.imageIv);
            holder.typeContainer.setOnClickListener(v -> {
                int model = Integer.parseInt(item.getModel());
                Intent intent = new Intent();
                switch (model) {
                    case 1:
                        intent = new Intent(context, DetailActivity.class);
                        break;

                    case 2:
                        intent = new Intent(context, VideoDetailActivity.class);
                        break;

                    case 3:
                        intent = new Intent(context, AudioDetailActivity.class);
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    intent.putExtra(Constant.KEY_ITEM, item);
                    context.startActivity(intent);
                }
            });
        }
    }

    private void bindFooterHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (artList.size() == 0) {
            return;
        }
        if (viewHolder instanceof FooterViewHolder) {
            FooterViewHolder holder = (FooterViewHolder) viewHolder;
            if (error) {
                error = false;
                holder.avi.setVisibility(View.GONE);
                holder.noMoreTv.setVisibility(View.GONE);
                holder.errorTv.setVisibility(View.VISIBLE);
                holder.errorTv.setOnClickListener(v -> {
                    if (onReloadListener != null) {
                        onReloadListener.onReLoad();
                    }
                });
            }
            if (hasMore) {
                holder.avi.setVisibility(View.VISIBLE);
                holder.noMoreTv.setVisibility(View.GONE);
                holder.errorTv.setVisibility(View.GONE);
            } else {
                holder.avi.setVisibility(View.GONE);
                holder.noMoreTv.setVisibility(View.VISIBLE);
                holder.errorTv.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        }
        return CONTENT_TYPE;
    }

    @Override
    public int getItemCount() {
        return artList.size() + 1;
    }

    public void setArtList(List<Item> artList) {
        int position = artList.size() - 1;
        this.artList.addAll(artList);
        notifyItemChanged(position);
    }

    public void replaceAllData(List<Item> artList) {
        this.artList.clear();
        this.artList.addAll(artList);
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

    static class ArtHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_iv)
        ImageView imageIv;
        @BindView(R.id.title_tv)
        TextView titleTv;
        @BindView(R.id.author_tv)
        TextView authorTv;
        @BindView(R.id.type_container)
        View typeContainer;

        public ArtHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.loading)
        AVLoadingIndicatorView avi;
        @BindView(R.id.no_more_tv)
        TextView noMoreTv;
        @BindView(R.id.error_tv)
        TextView errorTv;


        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
