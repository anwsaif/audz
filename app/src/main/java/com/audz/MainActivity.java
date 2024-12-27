package com.audz;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.os.Handler;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;

import android.widget.CompoundButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.content.DialogInterface;

import androidx.room.Room;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private TextDao textDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initialize Room Database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "my_database")
                .build();
        textDao = db.textDao();

        // Get UI References
        // EditText categoryInput = findViewById(R.id.categoryInput);
        // MaterialSwitch detectFullButton = findViewById(R.id.detectFullUrl);
        EditText textInput = findViewById(R.id.textInput);
        Button saveButton = findViewById(R.id.saveButton);
        Button timerButton = findViewById(R.id.timerButton);
        TextView displayTexts = findViewById(R.id.displayTexts);

        Context appContext = getApplicationContext();

        // Save Button Logic
        saveButton.setOnClickListener(v -> {
            // category = detectFullButton.isChecked() ? "full" : "domain";
            // String category = categoryInput.getText().toString().trim();
            String text = textInput.getText().toString().trim();

            if (!text.isEmpty()) {
                // Save to database
                new Thread(() -> {
                    try {
                        textDao.insert(new Text(text));
                        runOnUiThread(() -> {
                            textInput.getText().clear();
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                        runOnUiThread(() -> {
                            Handler handler = new Handler();
                            handler.post(() -> {
                                new MaterialAlertDialogBuilder(MainActivity.this, R.style.Widget_App_Dialog)
                                    .setTitle("Error")
                                    .setMessage("Item already Exists!")
                                    .setNeutralButton("Okay",(dialog,which)->{})
                                    .show();
                            });
                        });

                    }
                    
                    Log.d("Room", "Saved: " + text);

                    Intent intent = new Intent("DATABASE_UPDATED");
                    intent.putExtra("operation", "insert");
                    appContext.sendBroadcast(intent);

                    // Fetch and display all texts for this category
                    List<Text> texts = textDao.getTexts();
                    // textDao.deleteAll();
                    runOnUiThread(() -> {
                        // Log.d("Room", texts.get(0).text + " nn " + texts.get(0).category);
                        StringBuilder display = new StringBuilder();
                        
                        for (Text t : texts) {
                            display.append("Text: ").append(t.text).append("\n");
                        }
                        // Log.d("Room", display.toString());
                        displayTexts.setText(display.toString());
                    });
                }).start();
            }
        });

        // timerButton.setOnClickListener(v -> {
        //     LongTermTimerWorker.scheduleTimer(appContext, 1);
        // });
        // deleteButton.setOnClickListener(v -> {
        //     // String category = "categoryInput.getText().toString().trim()";
        //     // String category = "domain";
        //     new Thread(() -> {
        //         // Fetch and display all texts for this category
        //         textDao.deleteAll();

        //         Intent intent = new Intent("DATABASE_UPDATED");
        //         intent.putExtra("operation", "delete_all");
        //         appContext.sendBroadcast(intent);

        //         List<Text> texts = textDao.getTexts();
        //         runOnUiThread(() -> {
        //             // Log.d("Room", texts.get(0).text + " nn " + texts.get(0).category);
        //             StringBuilder display = new StringBuilder();
                    
        //             for (Text t : texts) {
        //                 display.append("Text: ").append(t.text).append("\n");
        //             }
        //             // Log.d("Room", display.toString());
        //             displayTexts.setText(display.toString());
        //         });
        //     }).start();
        // });

    // final TextView helloTextView = (TextView) findViewById(R.id.text_view_id);
    // helloTextView.setText(R.string.user_greeting);

    // MaterialSwitch materialSwitch = findViewById(R.id.materialSwitch);

        // Set listener for switch toggle
    // materialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    //     @Override
    //     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    //         if (isChecked) {
    //             Toast.makeText(MainActivity.this, "Switch ON", Toast.LENGTH_SHORT).show();
    //         } else {
    //             Toast.makeText(MainActivity.this, "Switch OFF", Toast.LENGTH_SHORT).show();
    //         }
    //     }
    // });

    // ComponentName adminComponent = new ComponentName(this, MyAdminReceiver.class);
    // DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);

    // // Check if already a Device Admin
    // if (!dpm.isAdminActive(adminComponent)) {
    //     Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
    //     intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent);
    //     intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "This app needs admin access to prevent uninstallation.");
    //     startActivity(intent);
    // }


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


