package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
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
import com.wiatec.bplay.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements Common {

    private ActivityMainBinding binding;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
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
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_radio_music, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_btv, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_premium, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_movies, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_radio_music, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_btv, binding.viewPager, false));
        return viewList;
    }

    private void initViewPager(){
        binding.viewPager.setOffscreenPageLimit(6);
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
                Intent intent1 = new Intent(MainActivity.this, ChannelTypeActivity2.class);
                intent1.putExtra("type", Constant.key.btv);
                startActivity(intent1);
                break;
            case 2:
                startActivity(new Intent(MainActivity.this, ChannelTypeActivity.class));
                break;
            case 3:
                startActivity(new Intent(MainActivity.this, MoviesActivity.class));
                break;
            case 4:
                Intent intent = new Intent(MainActivity.this, ChannelTypeActivity2.class);
                intent.putExtra("type", Constant.key.radio_music);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivMain, R.drawable.img_hold4,
                    R.drawable.img_hold4);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
