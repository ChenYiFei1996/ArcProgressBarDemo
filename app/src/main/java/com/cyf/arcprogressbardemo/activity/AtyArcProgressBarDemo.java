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
 */
public class AtyArcProgressBarDemo extends AppCompatActivity implements View.OnClickListener {
    private ActivityArcProgressBarDemoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_arc_progress_bar_demo);
        mBinding.setOnClick(this);
        mBinding.setArcProgressStr("0");
        mBinding.setTickProgressStr("0");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.arc_progress_bar_minus: {
                int progress = mBinding.getArcProgress();
                if (progress > 0) {
                    progress -= 1;
                }
                mBinding.setArcProgress(progress);
                mBinding.setArcProgressStr(String.valueOf(progress));
                break;
            }
            case R.id.arc_progress_bar_add: {
                int progress = mBinding.getArcProgress();
                if (progress < mBinding.arcProgressBar.getMax()) {
                    progress += 1;
                }
                mBinding.setArcProgress(progress);
                mBinding.setArcProgressStr(String.valueOf(progress));
                break;
            }
            case R.id.tick_progress_bar_minus: {
                int progress = mBinding.getTickProgress();
                if (progress > 0) {
                    progress -= 1;
                }
                mBinding.setTickProgress(progress);
                mBinding.setTickProgressStr(String.valueOf(progress));
                break;
            }
            case R.id.tick_progress_bar_add: {
                int progress = mBinding.getTickProgress();
                if (progress < mBinding.tickProgressBar.getMax()) {
                    progress += 1;
                }
                mBinding.setTickProgress(progress);
                mBinding.setTickProgressStr(String.valueOf(progress));
                break;
            }
        }
    }
}