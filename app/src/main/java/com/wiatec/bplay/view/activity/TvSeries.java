package com.wiatec.bplay.view.activity;

import com.wiatec.bplay.pojo.TvSeriesInfo;

import java.util.List;

/**
 * Created by patrick on 07/08/2017.
 * create time : 3:40 PM
 */

public interface TvSeries extends Common {
    void loadTvSeries (boolean execute, List<TvSeriesInfo> tvSeriesList);
}
