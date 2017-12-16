package com.example.chris.test6.di.app;


import com.example.chris.test6.di.sdscan.SdScanComponent;
import com.example.chris.test6.di.sdscan.SdScanModule;

import dagger.Component;

/**
 * Created by chris on 12/7/2017.
 */

@Component(modules = AppModule.class)
public interface AppComponent
{
    SdScanComponent add(SdScanModule sdScanModule);
}
