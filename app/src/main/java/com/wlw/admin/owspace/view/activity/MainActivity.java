package com.wlw.admin.owspace.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wlw.admin.owspace.R;
import com.wlw.admin.owspace.app.OwspaceApplication;
import com.wlw.admin.owspace.di.components.DaggerMainComponent;
import com.wlw.admin.owspace.di.modules.MainModule;
import com.wlw.admin.owspace.model.entity.Event;
import com.wlw.admin.owspace.model.entity.Item;
import com.wlw.admin.owspace.presenter.MainContract;
import com.wlw.admin.owspace.presenter.MainPresenter;
import com.wlw.admin.owspace.utils.AppUtils;
import com.wlw.admin.owspace.utils.Constant;
import com.wlw.admin.owspace.utils.PreferenceUtils;
import com.wlw.admin.owspace.utils.RxBus;
import com.wlw.admin.owspace.utils.TimeUtil;
import com.wlw.admin.owspace.view.adapter.VerticalPagerAdapter;
import com.wlw.admin.owspace.view.fragment.LeftMenuFragment;
import com.wlw.admin.owspace.view.fragment.RightMenuFragment;
import com.wlw.admin.owspace.view.widget.LunarDialog;
import com.wlw.admin.owspace.view.widget.VerticalViewPager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * @author admin
 */
public class MainActivity extends BaseActivity implements MainContract.View {
    @BindView(R.id.view_pager)
    VerticalViewPager viewPager;
    private SlidingMenu slidingMenu;
    private LeftMenuFragment leftMenu;
    private RightMenuFragment rightMenu;
    private String deviceId;

    @Inject
    MainPresenter presenter;

    private VerticalPagerAdapter pageAdapter;
    private int page = 1;
    private boolean isLoading = true;
    private long mLastClickTime;
    private Subscription subscription;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initMenu();
        initPage();
        deviceId = AppUtils.getDeviceId(this);
        String format = "yyyyMMdd";
        String getLunar = PreferenceUtils.getPrefString(this, Constant.GET_LUNAR, "");
        if (!TimeUtil.getDate(format).equals(getLunar)) {
            loadRecommend();
        }
        loadData(1, 0, "0", "0");
    }

    private void loadRecommend() {
        presenter.getRecommend(deviceId);
    }

    private void initMenu() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        // 设置触摸屏幕的模式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.setFadeEnabled(true);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setMenu(R.layout.left_menu);

        leftMenu = new LeftMenuFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.left_menu, leftMenu).commit();

        slidingMenu.setSecondaryMenu(R.layout.right_menu);
        rightMenu = new RightMenuFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.right_menu, rightMenu).commit();
        subscription = RxBus.getInstance().toObservable(Event.class)
                .subscribe(event -> slidingMenu.showContent());

    }

    private void initPage() {
        pageAdapter = new VerticalPagerAdapter(getSupportFragmentManager());
        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .netComponent(OwspaceApplication.get(this).getNetComponent())
                .build()
                .inject(this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int step = 2;
                if (pageAdapter.getCount() <= i + step && !isLoading) {
                    loadData(page, 0, pageAdapter.getLastItemId(), pageAdapter.getLastItemCreateTime());
                }
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing() || slidingMenu.isSecondaryMenuShowing()) {
            slidingMenu.showContent();
        } else {
            if (System.currentTimeMillis() - mLastClickTime <= 2000) {
                super.onBackPressed();
            } else {
                mLastClickTime = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadData(int page, int mode, String pageId, String createTime) {
        isLoading = true;
        presenter.getListByPage(page, mode, pageId, deviceId, createTime);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showNoMore() {
        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateListUI(List<Item> itemList) {
        isLoading = false;
        pageAdapter.setDataList(itemList);
        page++;
    }

    @Override
    public void showOnFailure() {
        isLoading = false;
        Toast.makeText(this, "加载数据失败,请检查您的网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLunar(String content) {
        PreferenceUtils.setPrefString(this, Constant.GET_LUNAR, TimeUtil.getDate("yyyyMMdd"));
        LunarDialog lunarDialog = new LunarDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_lunar, null);
        ImageView imageView = view.findViewById(R.id.image_iv);
        Glide.with(this).load(content).into(imageView);
        lunarDialog.setContentView(view);
        lunarDialog.show();
    }

    @Override
    protected void onDestroy() {
        subscription.unsubscribe();
        bind.unbind();
        super.onDestroy();

    }

    @OnClick({R.id.Left_slide, R.id.right_slide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Left_slide:
                slidingMenu.showMenu();
                leftMenu.startAnim();
                break;
            case R.id.right_slide:
                slidingMenu.showSecondaryMenu();
                rightMenu.startAnim();
                break;
            default:
                break;
        }
    }
}
