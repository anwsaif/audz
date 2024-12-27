package com.audz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import android.util.Log;
import android.widget.Toast;
import android.content.Intent;
import android.os.Looper;
import android.os.Handler;
import android.os.Build;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import androidx.core.app.NotificationCompat;

import androidx.room.Room;

public class MyAccessibilityService extends AccessibilityService {
    private AppDatabase db;
    private TextDao textDao;
    private Set<String> blockedKeywordsList = new HashSet<>();

    private final String[] igViewIdToLookFor = {
        "clips_tab", 
        "search_tab",
        "feed_tab",
    };

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Audz")
                .setContentText("Audzu billahi mina asy syaithonirrojiim")
                .setSmallIcon(R.drawable.ic_notification)
                // .setPriority(NotificationCompat.PRIORITY_LOW)
                // .setSilent(true) // Ensures no sound/vibration
                .build();

        startForeground(1, notification);

        // Hide the notification
        // NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // if (manager != null) {
        //     manager.cancel(1); // Cancel the notification with the same ID
        // }

        createNotificationChannel();

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "my_database")
                .build();
        textDao = db.textDao();

        new Thread(() -> {
            List<Text> words = textDao.getTexts();
            for (Text word : words){
                // if (blockedKeywordsList.contains(word))
                blockedKeywordsList.add(word.text);
            }
        }).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                "channel_id",
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }    

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        IntentFilter filter = new IntentFilter("DATABASE_UPDATED");
        registerReceiver(databaseUpdateReceiver, filter);
    }

    private final BroadcastReceiver databaseUpdateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent){
            String operation = intent.getStringExtra("operation");
            if("insert".equals(operation)){
                blockedKeywordsList.clear();
                new Thread(() -> {
                    List<Text> words = textDao.getTexts();
                    for (Text word : words){
                        // if (blockedKeywordsList.contains(word))
                        blockedKeywordsList.add(word.text);
                    }
                }).start();
            } else if ("delete_all".equals(operation)){
                blockedKeywordsList.clear();
            }
        }
    };

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "Unknown";
        // AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        // Log.d("MyAccessibilityService", "tdk snng " + isBrowser(packageName) + " lianna >> " + packageName);
        // Log.d("MyAccessibilityService", "is this " + blockedKeywordsList);
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (isBrowser(packageName) && blockedKeywordsList != null){
            String browserViewId = packageName.contains("chrome") ? 
                                    "com.android.chrome:id/url_bar" : 
                                    packageName.contains("mi.globalbrowser") ? 
                                    "com.mi.globalbrowser:id/url" : "";

            if (!blockedKeywordsList.isEmpty() && rootNode != null){
                for (String word : blockedKeywordsList){
                
                    List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(browserViewId);
                
                    for (AccessibilityNodeInfo node : nodes) {
                        if(!node.isFocused() && node.getText().toString().toLowerCase().contains(word)){
                            performGlobalAction(GLOBAL_ACTION_BACK);
                            performGlobalAction(GLOBAL_ACTION_BACK);
                            performGlobalAction(GLOBAL_ACTION_BACK);
                        }
                    }
                }
            }
        } else if (isInstagram(packageName) && rootNode != null){
            List<AccessibilityNodeInfo> profileTab = rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/profile_tab");
            for (String viewId : igViewIdToLookFor){
                List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId("com.instagram.android:id/" + viewId);
                for (AccessibilityNodeInfo node : nodes) {
                    if (!profileTab.isEmpty() && node.isSelected()){
                        profileTab.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }

            
        } else if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            

            if (event.getClassName() != null){
                String className = event.getClassName().toString();
            
            closeByClassName(className, "com.google.android.apps.youtube.app.watchwhile.MainActivity");
            
            closeByText(className, "com.miui.appmanager.ApplicationsDetailsActivity", "Audz", rootNode);
            closeByClassName(className, "com.miui.optimizemanage.memoryclean.LockAppManageActivity");
            detectText("wa ga na wa Shadow🥶🥶🥶", rootNode);
            Log.d("MyAccessibilityService", "tuh bs" + rootNode);    
            if (rootNode != null){
                
                List<AccessibilityNodeInfo> adminKh = rootNode.findAccessibilityNodeInfosByViewId("com.android.settings:id/admin_warning");
                List<AccessibilityNodeInfo> deactKh = rootNode.findAccessibilityNodeInfosByViewId("com.android.settings:id/uninstall_button");

                Log.d("MyAccessibilityService", "kelas king HH " + deactKh);    
                if (!adminKh.isEmpty() && deactKh.isEmpty()){
                    for (AccessibilityNodeInfo node : adminKh) {
                        performGlobalAction(GLOBAL_ACTION_HOME);
                        
                    }
                }
            }
            }
        }
        // AccessibilityNodeInfo testNode = getRootInActiveWindow();
        // if (testNode != null) {
        //     logAllNodes(testNode);
        // }
    }

    @Override
    public void onInterrupt() {
        Log.d("MyAccessibilityService", "Service interrupted.");
    }

    private void logAllNodes(AccessibilityNodeInfo node) {
        if (node == null) return;
    
        // Log this node's details (e.g., text, contentDescription)
        Log.d("NodeLogger", "Node: " + node);
        // Log.d("NodeLogger", "Node ContentDescription: " + node.getContentDescription());
    
        // Recursively traverse the child nodes
        for (int i = 0; i < node.getChildCount(); i++) {
            logAllNodes(node.getChild(i));
        }
    }

    public void closeByText( String currentClass, String targetClass, String textToLookFor, AccessibilityNodeInfo rootNode){
        if (currentClass.equals(targetClass)) {
            // Launch your lock screen or terminate the app.
            // performGlobalAction(GLOBAL_ACTION_HOME); // Example: Take the user to Home.
            detectText(textToLookFor, rootNode);
        }
    }

    public void closeByClassName( String currentClass, String targetClass){
        if (currentClass.equals(targetClass)) {
            performGlobalAction(GLOBAL_ACTION_HOME);
        }
    }

    public void detectText(String text, AccessibilityNodeInfo rootNode){
        if(rootNode != null) {
            List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText(text);
            // Log.d("MyAccessibilityService", root);
            for (AccessibilityNodeInfo node : nodes) {
                // Log.d("MyAccessibilityService", "teksse: " + node.getText() + " idne: " + node.getViewIdResourceName());
                performGlobalAction(GLOBAL_ACTION_HOME);
            }
        }
    }

    private boolean isBrowser(String packageName){
        final String[] browsers = {
            "com.android.chrome",
            "com.mi.globalbrowser",
        };
        return Arrays.asList(browsers).contains(packageName);
    }

    private boolean isInstagram(String packageName){
        return packageName.equals("com.instagram.android");
    }
    
}

