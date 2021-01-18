package com.cyf.arcprogressbardemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.cyf.arcprogressbardemo.R;
import com.cyf.arcprogressbardemo.databinding.ActivityArcProgressBarDemoBinding;

/**
 * Description: 圆形进度条demo界面
 * Author: cyf
 * Version: 1.0
 * UpdateUser: cyf 2021/1/18 18:19
 * UpdateRemark: 界面显示进度最大值
 * Version: 1.1
 */
public class AtyArcProgressBarDemo extends AppCompatActivity implements View.OnClickListener {
    private ActivityArcProgressBarDemoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_arc_progress_bar_demo);
        mBinding.setOnClick(this);
        mBinding.setArcProgressStr("0/" + mBinding.arcProgressBar.getMax());
        mBinding.setTickProgressStr("0/" + mBinding.tickProgressBar.getMax());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.arc_progress_bar_minus: {//圆形进度条减进度
                int progress = mBinding.getArcProgress();
                if (progress > 0) {
                    progress -= 1;
                }
                mBinding.setArcProgress(progress);
                mBinding.setArcProgressStr(progress + "/" + mBinding.arcProgressBar.getMax());
                break;
            }
            case R.id.arc_progress_bar_add: {//圆形进度条加进度
                int progress = mBinding.getArcProgress();
                if (progress < mBinding.arcProgressBar.getMax()) {
                    progress += 1;
                }
                mBinding.setArcProgress(progress);
                mBinding.setArcProgressStr(progress + "/" + mBinding.arcProgressBar.getMax());
                break;
            }
            case R.id.tick_progress_bar_minus: {//刻度进度条减进度
                int progress = mBinding.getTickProgress();
                if (progress > 0) {
                    progress -= 1;
                }
                mBinding.setTickProgress(progress);
                mBinding.setTickProgressStr(progress + "/" + mBinding.tickProgressBar.getMax());
                break;
            }
            case R.id.tick_progress_bar_add: {//刻度进度条加进度
                int progress = mBinding.getTickProgress();
                if (progress < mBinding.tickProgressBar.getMax()) {
                    progress += 1;
                }
                mBinding.setTickProgress(progress);
                mBinding.setTickProgressStr(progress + "/" + mBinding.tickProgressBar.getMax());
                break;
            }
        }
    }
}