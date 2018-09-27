package com.wlw.admin.owspace.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.utils.Constant;
import com.wlw.admin.owspace.utils.Log;
import com.wlw.admin.owspace.view.activity.AudioDetailActivity;
import com.wlw.admin.owspace.view.activity.DetailActivity;
import com.wlw.admin.owspace.view.activity.VideoDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author admin
 */
public class MainFragment extends Fragment {

    private Unbinder bind;
    String title;

    @BindView(R.id.image_iv)
    AppCompatImageView imageIv;
    @BindView(R.id.type_container)
    ConstraintLayout typeContainer;
    @BindView(R.id.comment_tv)
    AppCompatTextView commentTv;
    @BindView(R.id.like_tv)
    AppCompatTextView likeTv;
    @BindView(R.id.read_count_tv)
    AppCompatTextView readCountTv;
    @BindView(R.id.title_tv)
    AppCompatTextView titleTv;
    @BindView(R.id.content_tv)
    AppCompatTextView contentTv;
    @BindView(R.id.author_tv)
    AppCompatTextView authorTv;
    @BindView(R.id.type_tv)
    AppCompatTextView typeTv;
    @BindView(R.id.time_tv)
    AppCompatTextView timeTv;
    @BindView(R.id.image_type)
    AppCompatImageView imageType;
    @BindView(R.id.download_start_white)
    AppCompatImageView downloadStartWhite;
    @BindView(R.id.home_advertise_iv)
    AppCompatImageView homeAdvertiseIv;
    @BindView(R.id.pager_content)
    ConstraintLayout pagerContent;


    public static Fragment newInstance(Item item) {
        Fragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.KEY_ITEM, item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        bind = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Item item = null;
        if (getArguments() != null) {
            item = getArguments().getParcelable(Constant.KEY_ITEM);
        }
        if (item == null) {
            return;
        }
        int model = Integer.valueOf(item.getModel());
        int typeVisible = 5;
        if (model == typeVisible) {
            pagerContent.setVisibility(View.GONE);
            homeAdvertiseIv.setVisibility(View.VISIBLE);
            Glide.with(this).load(item.getThumbnail()).apply(new RequestOptions().centerCrop()).into(homeAdvertiseIv);
        } else {
            pagerContent.setVisibility(View.VISIBLE);
            homeAdvertiseIv.setVisibility(View.GONE);
            title = item.getTitle();
            Glide.with(this).load(item.getThumbnail()).apply(new RequestOptions().centerCrop()).into(imageIv);
            contentTv.setText(item.getComment());
            likeTv.setText(item.getComment());
            readCountTv.setText(item.getView());
            titleTv.setText(title);
            contentTv.setText(item.getExcerpt());
            authorTv.setText(item.getAuthor());
            typeTv.setText(item.getCategory());
            switch (model) {
                case 2:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setImageResource(R.mipmap.library_video_play_symbol);
                    break;
                case 3:
                    imageType.setVisibility(View.VISIBLE);
                    downloadStartWhite.setVisibility(View.VISIBLE);
                    imageType.setImageResource(R.mipmap.library_voice_play_symbol);
                    break;
                default:
                    downloadStartWhite.setVisibility(View.GONE);
                    imageType.setVisibility(View.GONE);
                    break;
            }
        }
        Item finalItem = item;
        typeContainer.setOnClickListener(v ->
                onItemClick(finalItem,model)
        );
    }

    private void onItemClick(Item finalItem, int model) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity == null) {
            return;
        }
        Intent intent = new Intent();
        switch (model) {
            case 5:
                Uri uri = Uri.parse(finalItem.getHtml5());
                intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(activity, AudioDetailActivity.class);
                intent.putExtra(Constant.KEY_ITEM, finalItem);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(activity, VideoDetailActivity.class);
                intent.putExtra(Constant.KEY_ITEM, finalItem);
                startActivity(intent);
                break;
            case 1:
                intent.setClass(activity, DetailActivity.class);
                intent.putExtra(Constant.KEY_ITEM, finalItem);
                startActivity(intent);
                break;
            default:
                intent.setClass(activity, DetailActivity.class);
                intent.putExtra(Constant.KEY_ITEM, finalItem);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
