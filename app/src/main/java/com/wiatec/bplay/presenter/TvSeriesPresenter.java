package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.AdImageProvider;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.model.TvSeriousProvider;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.TvSeriesInfo;
import com.wiatec.bplay.view.activity.TvSeries;

import java.util.List;

/**
 * Created by patrick on 07/08/2017.
 * create time : 3:46 PM
 */

public class TvSeriesPresenter extends BasePresenter<TvSeries> {

    private AdImageProvider adImageProvider;
    private TvSeriousProvider tvSeriousProvider;
    private TvSeries tvSeries;

    public TvSeriesPresenter(TvSeries tvSeries) {
        this.tvSeries = tvSeries;
        adImageProvider = new AdImageProvider();
        tvSeriousProvider = new TvSeriousProvider();
    }

    public void loadAdImage(){
        adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
            @Override
            public void onLoad(boolean execute, ImageInfo imageInfo) {
                tvSeries.loadAdImage(execute, imageInfo);
            }
        });
    }

    public void loadTvSeries(){
        tvSeriousProvider.load(new LoadService.OnLoadListener<List<TvSeriesInfo>>() {
            @Override
            public void onLoad(boolean execute, List<TvSeriesInfo> tvSeriesInfos) {
                tvSeries.loadTvSeries(execute, tvSeriesInfos);
            }
        });
    }
}
