package com.audz;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import android.util.Log;

public class MyAccessibilityService extends AccessibilityService {
  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {
    Log.d("MyAccessibilityService", "Event triggered: " + event.getEventType());

    String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "Unknown";
    if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
      Log.d("MyAccessibilityService", "Window changed in package: " + packageName);
    }

    if (packageName.equals("com.mi.globalbrowser")) {
      // Launch your lock screen or terminate the app.
      performGlobalAction(GLOBAL_ACTION_HOME); // Example: Take the user to Home.
    }
    Log.d("MyAccessibilityService", "Nih nama paketnya: " + packageName);
  }

  @Override
  public void onInterrupt() {
    Log.d("MyAccessibilityService", "Service interrupted.");
  }
}

