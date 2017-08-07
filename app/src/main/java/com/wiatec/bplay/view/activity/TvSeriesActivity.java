package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelAdapter;
import com.wiatec.bplay.adapter.TvSeriousAdapter;
import com.wiatec.bplay.databinding.ActivityTvSeriesBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.TvSeriesInfo;
import com.wiatec.bplay.presenter.TvSeriesPresenter;

import java.util.List;

public class TvSeriesActivity extends BaseActivity<TvSeriesPresenter> implements TvSeries {

    private ActivityTvSeriesBinding binding;

    @Override
    protected TvSeriesPresenter createPresenter() {
        return new TvSeriesPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tv_series);
        presenter.loadTvSeries();
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {

    }

    @Override
    public void loadTvSeries(boolean execute, final List<TvSeriesInfo> tvSeriesList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            binding.tvLoading.setText(getString(R.string.data_load_error));
            binding.btRetry.setVisibility(View.VISIBLE);
            binding.btRetry.requestFocus();
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        TvSeriousAdapter tvSeriousAdapter = new TvSeriousAdapter(tvSeriesList);
        binding.rcvTvSeries.setAdapter(tvSeriousAdapter);
        binding.rcvTvSeries.setLayoutManager(new GridLayoutManager(this ,5));
        tvSeriousAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn10to11(view);
                }else{
                    Zoom.zoomOut11to10(view);
                }
            }
        });
        tvSeriousAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TvSeriesInfo tvSeriesInfo = tvSeriesList.get(position);
                Intent intent = new Intent(TvSeriesActivity.this, ChannelActivity.class);
                intent.putExtra(Constant.key.channel_type, tvSeriesInfo.getTag());
                startActivity(intent);
            }
        });
    }


}
