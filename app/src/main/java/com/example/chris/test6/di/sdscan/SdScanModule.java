package com.example.chris.test6.di.sdscan;

import com.example.chris.test6.view.sdscan.SdScanPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Admin on 11/29/2017.
 */

@Module
public class SdScanModule
{
    @Provides
    SdScanPresenter providerSdScanPresenter()
    {
        return new SdScanPresenter();
    }
}
