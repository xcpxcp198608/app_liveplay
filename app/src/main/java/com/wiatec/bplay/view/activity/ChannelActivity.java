package com.wiatec.bplay.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelAdapter;
import com.wiatec.bplay.adapter.LiveChannelAdapter;
import com.wiatec.bplay.databinding.ActivityChannelBinding;
import com.wiatec.bplay.entity.ResultInfo;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.pay.PayInfo;
import com.wiatec.bplay.pay.PayPalConfig;
import com.wiatec.bplay.pay.PayPalManager;
import com.wiatec.bplay.pay.PayResultInfo;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.LiveChannelInfo;
import com.wiatec.bplay.presenter.ChannelPresenter;
import com.wiatec.bplay.sql.HistoryChannelDao;

import java.util.List;

/**
 * channel activity
 */

public class ChannelActivity extends BaseActivity<ChannelPresenter> implements Channel,
        View.OnClickListener, PayPalManager.OnPayResultListener {

    private ActivityChannelBinding binding;
    private String type;
    private LiveChannelInfo mLiveChannelInfo;
    private String key;

    @Override
    protected ChannelPresenter createPresenter() {
        return new ChannelPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        PayPalConfig.startPayPalService(this);
        type = getIntent().getStringExtra(Constant.key.channel_type);
        if(type.equals("FAVORITE") || type.equals("SPORTS") || type.equals("SPORTS EVENT") ||
                type.equals("LATINO") || type.equals("USA") || type.equals("USA LOCAL NEWS") ||
                type.equals("CHINA") || type.equals("TAIWAN") || type.equals("KOREA") ||
                type.equals("JAPAN") || type.equals("ASIA") || type.equals("EUROPE") ||
                type.equals("AFRICA") || type.equals("MIDEAST") || type.equals("HISTORY")){
            binding.llSearch.setVisibility(View.GONE);
        }
        key = getIntent().getStringExtra(Constant.key.key_search);
        if(Constant.key.type_favorite.equals(type)){
            presenter.loadFavorite();
        }else if(Constant.key.type_history.equals(type)){
            presenter.loadHistory();
        }else if(Constant.key.type_search.equals(type)){
            presenter.loadSearch(key);
        }else if(Constant.key.type_live_channel.equals(type)){
            presenter.loadLiveChannel();
        }else {
            presenter.loadChannel(type);
        }
        binding.btRetry.setOnClickListener(this);
        binding.ibtSearch.setOnClickListener(this);
        binding.ibtHistory.setOnClickListener(this);
        binding.ibtCleanHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtSearch:
                String searchKey = binding.etSearch.getText().toString().trim();
                if(TextUtils.isEmpty(searchKey)){
                    return;
                }
                Intent intent1 = new Intent(ChannelActivity.this, ChannelActivity.class);
                intent1.putExtra(Constant.key.channel_type, Constant.key.type_search);
                intent1.putExtra(Constant.key.key_search, searchKey);
                startActivity(intent1);
                break;
            case R.id.ibtHistory:
                Intent intent = new Intent(ChannelActivity.this, ChannelActivity.class);
                intent.putExtra(Constant.key.channel_type, Constant.key.type_history);
                startActivity(intent);
                break;
            case R.id.ibtCleanHistory:
                showCleanDialog();
                break;
            case R.id.btRetry:
                binding.tvLoading.setText(getString(R.string.data_loading));
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.btRetry.setVisibility(View.GONE);
                if(Constant.key.type_favorite.equals(type)){
                    presenter.loadFavorite();
                }else if(Constant.key.type_history.equals(type)){
                    presenter.loadHistory();
                }else if(Constant.key.type_search.equals(type)){
                    presenter.loadSearch(key);
                }else if(Constant.key.type_live_channel.equals(type)){
                    presenter.loadLiveChannel();
                }else {
                    presenter.loadChannel(type);
                }
                break;
        }
    }

    private void showCleanDialog(){
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        if(window == null) return;
        dialog.setContentView(R.layout.dialog_update);
        TextView tvInfo = (TextView) window.findViewById(R.id.tv_info);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        tvInfo.setText(R.string.clean_history);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryChannelDao historyChannelDao = HistoryChannelDao.getInstance();
                historyChannelDao.deleteAll();
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PayPalConfig.stopPayPalService(this);
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            //
        }
    }

    @Override
    public void loadChannel(boolean execute, final List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    @Override
    public void loadFavorite(boolean execute, List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    @Override
    public void loadHistory(boolean execute, List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    @Override
    public void loadSearch(boolean execute, List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    private void load(boolean execute, final List<ChannelInfo> channelInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            if(Constant.key.type_favorite.equals(type)){
                binding.tvLoading.setText(getString(R.string.favorite_load_empty));
            }else if(Constant.key.type_history.equals(type)){
                binding.tvLoading.setText(getString(R.string.history_load_empty));
            }else if(Constant.key.type_search.equals(type)){
                binding.tvLoading.setText(getString(R.string.search_load_empty));
            }else {
                binding.tvLoading.setText(getString(R.string.data_load_error));
                binding.btRetry.setVisibility(View.VISIBLE);
                binding.btRetry.requestFocus();
            }
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        binding.tvTotal.setText(channelInfoList.size()+"");
        binding.tvSplit.setVisibility(View.VISIBLE);
        binding.tvPosition.setText(1+"");

        ChannelAdapter channelAdapter = new ChannelAdapter(channelInfoList);
        binding.rcvChannel.setAdapter(channelAdapter);
        binding.rcvChannel.setLayoutManager(new GridLayoutManager(this, 5));
        channelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int style = channelInfoList.get(position).getStyle();
                Application.setChannelInfoList(channelInfoList);
                if(style == 1){
                    launchFMPlay(channelInfoList, position);
                }else {
                    launchPlay(channelInfoList, position);
                }
            }
        });
        channelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    binding.tvPosition.setText((position + 1) + "");
                    Zoom.zoomIn10to11(view);
                    view.setSelected(true);
                }else{
                    Zoom.zoomOut11to10(view);
                    view.setSelected(false);
                }
            }
        });
    }

    @Override
    public void loadLiveChannel(boolean execute, final List<LiveChannelInfo> liveChannelInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            if(Constant.key.type_favorite.equals(type)){
                binding.tvLoading.setText(getString(R.string.favorite_load_empty));
            }else if(Constant.key.type_history.equals(type)){
                binding.tvLoading.setText(getString(R.string.history_load_empty));
            }else if(Constant.key.type_search.equals(type)){
                binding.tvLoading.setText(getString(R.string.search_load_empty));
            }else {
                binding.tvLoading.setText(getString(R.string.data_load_error));
                binding.btRetry.setVisibility(View.VISIBLE);
                binding.btRetry.requestFocus();
            }
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        binding.tvTotal.setText(liveChannelInfoList.size()+"");
        binding.tvSplit.setVisibility(View.VISIBLE);
        binding.tvPosition.setText(1+"");

        LiveChannelAdapter liveChannelAdapter = new LiveChannelAdapter(liveChannelInfoList);
        binding.rcvChannel.setAdapter(liveChannelAdapter);
        binding.rcvChannel.setLayoutManager(new GridLayoutManager(this, 5));
        liveChannelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mLiveChannelInfo = liveChannelInfoList.get(position);
                play(mLiveChannelInfo);
            }
        });
        liveChannelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    binding.tvPosition.setText((position + 1) + "");
                    Zoom.zoomIn10to11(view);
                    view.setSelected(true);
                }else{
                    Zoom.zoomOut11to10(view);
                    view.setSelected(false);
                }
            }
        });
    }

    private void play(LiveChannelInfo liveChannelInfo){
        if(liveChannelInfo.getPrice() <= 0){
            launchLivePlay(liveChannelInfo);
            return;
        }
        String payerName = UserContentResolver.get("userName");
        if(TextUtils.isEmpty(payerName)){
            EmojiToast.show("no sign in", EmojiToast.EMOJI_SAD);
            return;
        }
        presenter.verifyPay(payerName, liveChannelInfo.getUserId(), "");
    }

    @Override
    public void onPayVerify(boolean execute, ResultInfo<PayResultInfo> resultInfo) {
        if(execute && resultInfo != null){
            if(resultInfo.getCode() == 200){
                launchLivePlay(mLiveChannelInfo);
            }else{
                boolean alreadyPreview = (boolean) SPUtils.get("already_preview" +
                        mLiveChannelInfo.getUserId() + mLiveChannelInfo.getTitle(), false);
                if(alreadyPreview) {
                    showPayDialog(new PayInfo(mLiveChannelInfo.getPrice(), "USD", mLiveChannelInfo.getTitle()));
                }else {
                    showPreviewPayDialog(new PayInfo(mLiveChannelInfo.getPrice(), "USD", mLiveChannelInfo.getTitle()));
                }
                EmojiToast.showLong(resultInfo.getMessage(), EmojiToast.EMOJI_SMILE);
            }
        }else{
            EmojiToast.showLong("communication error", EmojiToast.EMOJI_SAD);
        }
    }

    private void showPreviewPayDialog(final PayInfo payInfo) {
        new MaterialDialog.Builder(ChannelActivity.this)
                .title(getString(R.string.notice))
                .content("You agree to pay $"+
                        payInfo.getPrice() + " "+
                        payInfo.getCurrency() + " to view " +
                        payInfo.getDescription() +". Without payment you may only view " +
                        payInfo.getDescription() + " for a 60 second preview." )
                .positiveText(getString(R.string.pay))
                .negativeText(getString(R.string.preview))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        PayPalManager.pay(ChannelActivity.this, payInfo);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(ChannelActivity.this , PlayLiveActivity.class);
                        intent.putExtra("isNeedPaid", true);
                        intent.putExtra("id", mLiveChannelInfo.getId()+"");
                        intent.putExtra("userId", mLiveChannelInfo.getUserId()+"");
                        intent.putExtra("title", mLiveChannelInfo.getTitle());
                        intent.putExtra("message", mLiveChannelInfo.getMessage());
                        intent.putExtra("playUrl", mLiveChannelInfo.getPlayUrl());
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showPayDialog(final PayInfo payInfo) {
        new MaterialDialog.Builder(ChannelActivity.this)
                .title(getString(R.string.notice))
                .content("You agree to pay $"+
                        payInfo.getPrice() + " "+
                        payInfo.getCurrency() + " to view " +
                        payInfo.getDescription())
                .positiveText(getString(R.string.confirm))
                .negativeText(getString(R.string.cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        PayPalManager.pay(ChannelActivity.this, payInfo);
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PayPalManager.payResult(requestCode, resultCode, data, this);
    }

    @Override
    public void paySuccess(String paymentId) {
        Logger.d(paymentId);
        String payerName = UserContentResolver.get("userName");
        if(TextUtils.isEmpty(payerName)){
            EmojiToast.show("no sign in", EmojiToast.EMOJI_SAD);
            return;
        }
        presenter.verifyPay(payerName, mLiveChannelInfo.getUserId(), paymentId);
    }

    @Override
    public void customerCancel(String error) {

    }

    private void launchPlay(List<ChannelInfo> channelInfoList, int position){
        Intent intent = new Intent(ChannelActivity.this , PlayActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void launchLivePlay(LiveChannelInfo liveChannelInfo){
        Intent intent = new Intent(ChannelActivity.this , PlayLiveActivity.class);
        intent.putExtra("id", liveChannelInfo.getId()+"");
        intent.putExtra("userId", liveChannelInfo.getUserId()+"");
        intent.putExtra("title", liveChannelInfo.getTitle());
        intent.putExtra("message", liveChannelInfo.getMessage());
        intent.putExtra("playUrl", liveChannelInfo.getPlayUrl());
        startActivity(intent);
    }

    private void launchFMPlay(List<ChannelInfo> channelInfoList, int position){
        Intent intent = new Intent(ChannelActivity.this , PlayFMActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
