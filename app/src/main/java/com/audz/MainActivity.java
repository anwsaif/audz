package com.audz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Intent;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;

public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    final TextView helloTextView = (TextView) findViewById(R.id.text_view_id);
    helloTextView.setText(R.string.user_greeting);

    // Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
    // startActivity(intent);

  }
  // public boolean isAccessibilityServiceEnabled(Context context, Class<?> serviceClass) {
  //   AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
  //   if (am == null) return false;

  //   for (AccessibilityServiceInfo service : am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
  //     if (service.getId().contains(context.getPackageName() + "/" + serviceClass.getName())) {
  //       return true;
  //     }
  //   }
  //   return false;
  // }

}


