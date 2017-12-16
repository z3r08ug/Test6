package com.example.chris.test6.view.sdscan;


import com.example.chris.test6.util.BasePresenter;
import com.example.chris.test6.util.BaseView;

/**
 * Created by Admin on 11/29/2017.
 */

public interface SdScanContract
{
    //methods for main activity
    interface View extends BaseView
    {
        void showProgress(String progress);
    }

    interface Presenter extends BasePresenter<View>
    {
    
    }
}
