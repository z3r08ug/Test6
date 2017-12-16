package com.example.chris.test6.view.sdscan;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chris.test6.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SdScanActivity extends AppCompatActivity
{
    private static final String TAG = SdScanActivity.class.getSimpleName() + "_TAG";
    private List<File> fileList, biggestFiles;
    private File file;
    private ProgressBar progressBar;
    private Handler handler;
    private int progressStatus;
    private TextView tvProgress;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder mBuilder;
    private TextView tvBiggest;
    private TextView tvFreqExt;
    private TextView tvAvgSize;
    private boolean scan;
    private TextView tvFreqTitle;
    private TextView tvBiggestTitle;
    private TextView tvAvgTitle;
    private android.support.v7.widget.ShareActionProvider shareActionProvider;
    private MenuItem share;
    private String freqExts;
    private String avgSize;
    private String biggest;
    private View div2;
    private View div1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sd_scan);
        
        bindViews();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.menu, menu);
    
        // Locate MenuItem with ShareActionProvider
        share = menu.findItem(R.id.share);
        // Fetch and store ShareActionProvider
        shareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(share);
        // Return true to display menu
        return true;
    }
    
    // Call to update the share intent
    private void setShareIntent(Intent shareIntent)
    {
        if (shareActionProvider != null)
        {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }
    
    private Intent createShareIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                biggest + "\n" + avgSize + "\n" + freqExts);
        return shareIntent;
    }
    
    private void bindViews()
    {
        div1 = findViewById(R.id.div1);
        div2 = findViewById(R.id.div2);
        progressBar = findViewById(R.id.pbSdScan);
        tvProgress = findViewById(R.id.tvProgress);
        tvBiggest = findViewById(R.id.tvBiggestFiles);
        tvFreqExt = findViewById(R.id.tvFreqExtensions);
        tvAvgSize = findViewById(R.id.tvAvgFileSize);
        tvBiggestTitle = findViewById(R.id.tvBiggestTitle);
        tvFreqTitle = findViewById(R.id.tvFreqTitle);
        tvAvgTitle = findViewById(R.id.tvAvgTitle);
        
        handler = new Handler();
        
        fileList = new ArrayList<>();
        biggestFiles = new ArrayList<>();
        
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Sd Card File Scanner")
                .setContentText("Scan in progress")
                .setSmallIcon(R.drawable.ic_launcher_background);
        
        scan = false;
    }
    
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        scan = false;
        Toast.makeText(SdScanActivity.this, "Scan cancelled", Toast.LENGTH_SHORT).show();
        mBuilder.setContentText("Scan cancelled")
                .setProgress(0, 0, false);
        notificationManager.notify(1, mBuilder.build());
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getFileListfromSDCard();
                    progressBar.setVisibility(View.VISIBLE);
                    tvProgress.setVisibility(View.VISIBLE);
                    scanFiles();
                }
                else
                {
                    Toast.makeText(SdScanActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    
    private void scanFiles()
    {
        progressStatus = 0;
        
        // Update the progress bar and display the
//current value in the text view
        new Thread(new Runnable()
        {
            
            public void run()
            {
                if (scan)
                {
                    while (progressStatus < fileList.size())
                    {
                        if (scan)
                        {
                            progressStatus += 1;
                            // Update the progress bar and display the
                            //current value in the text view
                            handler.post(new Runnable()
                            {
                                public void run()
                                {
                                    progressBar.setProgress(progressStatus);
                                    mBuilder.setProgress(fileList.size(), progressStatus, false);
                                    notificationManager.notify(1, mBuilder.build());
                                    tvProgress.setText("Scanning files:\n" + progressStatus + "/" + fileList.size());
                                    
                                    if (progressStatus == fileList.size())
                                    {
                                        Toast.makeText(SdScanActivity.this, "Scan is complete", Toast.LENGTH_SHORT).show();
                                        mBuilder.setContentText("Scan complete")
                                                .setProgress(0, 0, false);
                                        notificationManager.notify(1, mBuilder.build());
                                        
                                        div1.setVisibility(View.VISIBLE);
                                        div2.setVisibility(View.VISIBLE);
                                        displayBiggestFiles();
                                        displayAvgFileSize();
                                        displayFreqExtensions();
                                        setShareIntent(createShareIntent());
                                    }
                                }
                            });
                            try
                            {
                                Thread.sleep(300);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }
    
    private void displayFreqExtensions()
    {
        //get list of extensions
        List<String> extensions = new ArrayList<>();
        for (File f : fileList)
        {
            extensions.add(f.getName().substring(f.getName().lastIndexOf(".") + 1, f.getName().length()));
        }
        
        //create hash map that contains each extension and frequency for that extension
        HashMap<String, Integer> extensionMap = new HashMap<>();
        
        for (String s : extensions)
        {
            if (extensionMap.containsKey(s))
            {
                //If an element is present, incrementing its count by 1
                extensionMap.put(s, extensionMap.get(s)+1);
            }
            else
            {
                //If an element is not present, put that element with 1 as its value
                extensionMap.put(s, 1);
            }
        }
        
        Log.d(TAG, "displayFreqExtensions: "+extensionMap);
        
        //convert hash map into list for sorting purposes
        List<Map.Entry<String, Integer>> extensionsWithFreq = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : extensionMap.entrySet())
        {
            extensionsWithFreq.add(entry);
        }
        
        //sort the list of extensions by their frequency
        Collections.sort(extensionsWithFreq, new Comparator<Map.Entry<String, Integer>>()
        {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                return o1.getValue() > o2.getValue() ? -1 : (o1.getValue() < o2.getValue()) ? 1 : 0;
            }
        });
        
        //only display the 5 most used extensions or all extensions if less than 5 are available
        freqExts = "";
        if (extensionsWithFreq.size() > 5)
        {
            for (int i = 0; i < 5; i++)
            {
                if (extensionsWithFreq.get(i).getValue() > 1)
                    freqExts += extensionsWithFreq.get(i).getKey() + "  --> " + extensionsWithFreq.get(i).getValue() + " occurrences\n";
                else
                    freqExts += extensionsWithFreq.get(i).getKey() + "  --> " + extensionsWithFreq.get(i).getValue() + " occurrence\n";
            }
        }
        else
        {
            for (Map.Entry<String, Integer> entry : extensionsWithFreq)
            {
                if (entry.getValue() > 1)
                    freqExts += entry.getKey() + "  --> " + entry.getValue() + " occurrences\n";
                else
                    freqExts += entry.getKey() + "  --> " + entry.getValue() + " occurrence\n";
            }
        }
        tvFreqTitle.setVisibility(View.VISIBLE);
        tvFreqExt.setVisibility(View.VISIBLE);
        tvFreqExt.setText(freqExts);
    }
    
    private void displayAvgFileSize()
    {
        double avg = 0.0;
        for (File f : fileList)
        {
            avg += f.length();
        }
        avg /= fileList.size();
        avgSize = avg + " bytes";
        tvAvgTitle.setVisibility(View.VISIBLE);
        tvAvgSize.setVisibility(View.VISIBLE);
        tvAvgSize.setText(avgSize);
        Log.d(TAG, "displayAvgFileSize: ");
    }
    
    private void displayBiggestFiles()
    {
        biggestFiles.clear();
        if (fileList.size() > 10)
        {
            for (int i = 0; i < 10; i++)
            {
                biggestFiles.add(fileList.get(i));
            }
        }
        else
        {
            for (File f : fileList)
            {
                biggestFiles.add(f);
            }
        }
    
        biggest = "";
        for (File f : biggestFiles)
        {
            biggest += (f.getName() + "   " + f.length() + " bytes\n");
        }
        tvBiggestTitle.setVisibility(View.VISIBLE);
        tvBiggest.setVisibility(View.VISIBLE);
        tvBiggest.setText(biggest);
    }
    
    private void getFileListfromSDCard()
    {
        fileList.clear();
        file = new File("/storage/0000-0000");
        Log.d(TAG, "onRequestPermissionsResult: " + file.list().toString());
        File list[] = file.listFiles();
        for (File f : list)
        {
            fileList.add(f);
        }
        
        Collections.sort(fileList, new Comparator<File>()
        {
            @Override
            public int compare(File f1, File f2)
            {
                return f1.length() > f2.length() ? -1 : (f1.length() < f2.length()) ? 1 : 0;
            }
        });
        
        progressBar.setMax(fileList.size());
    }
    
    public void onScanSdCard(View view)
    {
        ActivityCompat.requestPermissions(SdScanActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        scan = true;
    }
    
    public void onStopScan(View view)
    {
        scan = false;
        Toast.makeText(SdScanActivity.this, "Scan cancelled", Toast.LENGTH_SHORT).show();
        mBuilder.setContentText("Scan cancelled")
                .setProgress(0, 0, false);
        notificationManager.notify(1, mBuilder.build());
    }
}
