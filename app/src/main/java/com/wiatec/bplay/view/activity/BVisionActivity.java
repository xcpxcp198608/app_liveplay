package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.utils.AppUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelTypeAdapter;
import com.wiatec.bplay.adapter.ChannelTypeBVisionAdapter;
import com.wiatec.bplay.adapter.LDFamAdapter;
import com.wiatec.bplay.databinding.ActivityBvisionBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelTypeInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.LDFamInfo;
import com.wiatec.bplay.presenter.BVisionPresenter;
import com.wiatec.bplay.presenter.ChannelTypePresenter;
import com.wiatec.bplay.task.TokenTask;
import com.wiatec.bplay.view.custom_view.LDFamListView;

import java.util.List;

public class BVisionActivity extends BaseActivity<BVisionPresenter> implements BVision, View.OnClickListener  {

    private ActivityBvisionBinding binding;
    private LDFamAdapter ldFamAdapter;

    @Override
    protected BVisionPresenter createPresenter() {
        return new BVisionPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bvision);
        binding.llLdFam.setOnClickListener(this);
        presenter.loadChannelType(2+"");
        presenter.loadLDFam();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_ld_fam:
                Logger.d("sdf");
                binding.pbLdFam.setVisibility(View.VISIBLE);
                presenter.loadLDFam();
                break;
        }
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {

    }

    @Override
    public void onLoadChannelType(boolean execute, final List<ChannelTypeInfo> channelTypeInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            binding.tvLoading.setText(getString(R.string.data_load_error));
            binding.btRetry.setVisibility(View.VISIBLE);
            binding.btRetry.requestFocus();
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        ChannelTypeBVisionAdapter channelTypeBVisionAdapter = new ChannelTypeBVisionAdapter(channelTypeInfoList);
        binding.rcvChannelType.setAdapter(channelTypeBVisionAdapter);
        binding.rcvChannelType.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rcvChannelType.requestFocus();
        channelTypeBVisionAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Application.getExecutorService().execute(new TokenTask());
                ChannelTypeInfo channelTypeInfo = channelTypeInfoList.get(position);
                showChannel(channelTypeInfo);
            }
        });
    }

    private void showChannel(ChannelTypeInfo channelTypeInfo){
        if(channelTypeInfo.getFlag() == 1){
            Intent intent = new Intent(BVisionActivity.this, ChannelTypeActivity1.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else if(channelTypeInfo.getFlag() == 2){
            Intent intent = new Intent(BVisionActivity.this, ChannelTypeActivity2.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else {
            Intent intent = new Intent(BVisionActivity.this, ChannelActivity.class);
            intent.putExtra(Constant.key.channel_type, channelTypeInfo.getTag());
            startActivity(intent);
        }
    }

    @Override
    public void onLoadLDFam(boolean execute, List<LDFamInfo> ldFamInfoList) {
        binding.pbLdFam.setVisibility(View.GONE);
        if(ldFamAdapter == null){
            ldFamAdapter = new LDFamAdapter(this, ldFamInfoList);
        }
        binding.lvLdFam.setAdapter(ldFamAdapter);
        ldFamAdapter.notifyChange(ldFamInfoList);
        binding.lvLdFam.start();
        binding.lvLdFam.setOnScrollFinishedListener(new LDFamListView.OnScrollFinishedListener() {
            @Override
            public void onFinished(boolean isFinished, int position) {
                if(presenter != null){
                    presenter.loadLDFam();
                }
            }
        });
    }
}
