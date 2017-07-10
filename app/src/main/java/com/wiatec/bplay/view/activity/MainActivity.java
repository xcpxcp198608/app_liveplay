package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.MainViewPagerAdapter;
import com.wiatec.bplay.adapter.MainViewPagerTransform;
import com.wiatec.bplay.databinding.ActivityMainBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.presenter.SplashPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<SplashPresenter> implements Splash {

    private ActivityMainBinding binding;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        presenter.loadAdImage();
        initViewPager();
    }

    private List<View> createViewList() {
        List<View> viewList = new ArrayList<>();
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_movies, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_btv, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_premium, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_movies, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_btv, binding.viewPager, false));
        return viewList;
    }

    private void initViewPager(){
        binding.viewPager.setOffscreenPageLimit(5);
        binding.viewPager.setPageMargin(100);
        binding.viewPager.setPageTransformer(true, new MainViewPagerTransform());
        final List<View> viewList = createViewList();
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(viewList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(2);
        adapter.setOnItemPageClick(new MainViewPagerAdapter.OnItemPageClick() {
            @Override
            public void onClick(View view, int position) {
               launchShortcut(position);
            }
        });
        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                presenter.loadAdImage();
                if(position == 0){
                    binding.viewPager.setCurrentItem(viewList.size()-2);
                }else if(position == viewList.size() -1){
                    binding.viewPager.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void launchShortcut(int position){
        switch (position){
            case 1:
                AppUtil.launchApp(MainActivity.this, Constant.packageName.btv);
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, ChannelTypeActivity.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, MoviesActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivMain, R.drawable.img_ld_gold,
                    R.drawable.img_ld_gold);
        }
    }

    //ignore this
    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {

    }
}
