package com.example.chris.test6.view.sdscan;


import javax.inject.Inject;

/**
 * Created by Admin on 11/29/2017.
 */

public class SdScanPresenter implements SdScanContract.Presenter
{
    SdScanContract.View view;
    public static final String TAG = SdScanPresenter.class.getSimpleName() + "_TAG";
    
    @Inject
    public SdScanPresenter()
    {
    
    }
    
    @Override
    public void attachView(SdScanContract.View view)
    {
        this.view = view;
    }

    @Override
    public void detachView()
    {
        this.view = null;
    }
}
