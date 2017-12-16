package com.example.chris.test6.di.sdscan;

import com.example.chris.test6.view.sdscan.SdScanActivity;

import dagger.Subcomponent;

/**
 * Created by Admin on 11/29/2017.
 */

@Subcomponent(modules = SdScanModule.class)
public interface SdScanComponent
{
    void inject(SdScanActivity sdScanActivity);
}
