package com.example.chris.test6;

import android.app.Application;

import com.example.chris.test6.di.app.AppComponent;
import com.example.chris.test6.di.app.AppModule;
import com.example.chris.test6.di.app.DaggerAppComponent;
import com.example.chris.test6.di.sdscan.SdScanComponent;
import com.example.chris.test6.di.sdscan.SdScanModule;

/**
 * Created by chris on 12/15/2017.
 */

public class SdScanApplication extends Application
{
    private AppComponent appComponent;
    private SdScanComponent sdScanComponent;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
    
        AppModule appModule = new AppModule();
        
        appComponent = DaggerAppComponent.builder()
                .appModule(appModule)
                .build();
    }
    
    public SdScanComponent getSdScanComponent()
    {
        sdScanComponent = appComponent.add(new SdScanModule());
        return sdScanComponent;
    }
    
    public void clearSdScanComponent()
    {
        sdScanComponent = null;
    }
}
