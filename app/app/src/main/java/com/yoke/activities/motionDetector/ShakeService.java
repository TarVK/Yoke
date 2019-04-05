package com.yoke.activities.motionDetector;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.yoke.connection.client.MultiClientConnection;

import static android.support.constraint.Constraints.TAG;

public class ShakeService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int shakeCount;
    private float gravity[] = new float[3];
    private long firstMoveTime;
    private long secondMoveTime;
    private long currentTime;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //registers the motion sensor

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        register();
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    //Calculate the maximum acceleration value on the three axis
    private float calcMaxAcceleration(SensorEvent event) {
        gravity[0] = calcGravityForce(event.values[0], 0);
        gravity[1] = calcGravityForce(event.values[1], 1);
        gravity[2] = calcGravityForce(event.values[2], 2);

        float accX = event.values[0] - gravity[0];
        float accY = event.values[1] - gravity[1];
        float accZ = event.values[2] - gravity[2];

        float max1 = Math.max(accX, accY);
        return Math.max(max1, accZ);
    }

    private float calcGravityForce(float currentVal, int index) {
        return 0.8F * gravity[index] + (1 - 0.8F) * currentVal;
    }

    //detect the motion change and do disconnection action if a shake condition is met
    @Override
    public void onSensorChanged(SensorEvent event) {
       float maxAcc = calcMaxAcceleration(event);
       Log.w(TAG, "onSensorChanged: " + shakeCount);
//       Log.w(TAG, "shake " + shakeCount + " acc " + maxAcc + " first " + firstMoveTime + " second " + secondMoveTime +" third " + currentTime);
//       Log.w(TAG, "diff1 " + (secondMoveTime-firstMoveTime) +  " diff2 " + (currentTime - secondMoveTime));

        //first shake
       if (maxAcc > 10) {
           if (shakeCount == 0) {
               shakeCount = 1;
               firstMoveTime = System.currentTimeMillis();
               return;
//               Log.w(TAG, " shake count " + shakeCount) ;
           }


       }

       //second shake
       if (shakeCount == 1) {
           if (maxAcc > 8) {
               secondMoveTime = System.currentTimeMillis();
               long interval = Math.abs(secondMoveTime - firstMoveTime);
               if (interval > 300 && interval < 1000) {
                   shakeCount = 2;
               } if (interval > 1000) {
                   reset();
                   return;
               }
           }
       }

       //second shake
       if (shakeCount == 2) {
           if (maxAcc > 8) {
               currentTime = System.currentTimeMillis();
               long interval = Math.abs(currentTime - secondMoveTime);
               if (interval > 400 && interval < 1000) {
                   shakeCount = 3;
                   Log.w(TAG, "onSensorChanged: disconnect");
                   Toast.makeText(this, "shaked three times", Toast.LENGTH_SHORT).show();
                   terminate();
                   return;

               } if (interval > 1000) {
                   reset();
                   return;
               }
           }
       }


//       if (shakeCount >= 1) {
//           if (maxAcc > 8) {
//               long currentTime = System.currentTimeMillis();
//               if (currentTime - firstMoveTime < 500) {
//                   shakeCount++;
//               } else if (currentTime - firstMoveTime > 500) {
//                   reset();
//                   return;
//               }
//               Log.w(TAG, "onSensorChanged: " + shakeCount );
//
//               if (shakeCount == 3) {
//                   reset();
//                   Toast.makeText(this, "disconnect", Toast.LENGTH_SHORT).show();
//               }
//           }
//               //check if the current time and last shaking time has an interval less than 500
//       }
    }

    //reset the count and measured time
    private void reset() {
        Log.w(TAG, "reset: resetting the count from" + shakeCount );
        shakeCount = 0;
        firstMoveTime = 0;
        secondMoveTime = 0;
        currentTime = 0;
        Log.w(TAG, "reset: count is resetted to :" + shakeCount);
    }

    public void deregister() {

        mSensorManager.unregisterListener(this);
    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL, new Handler());
    }

    //if shaked three times, disconnect and terminate the application
    private void terminate() {
        MultiClientConnection.destroyInstance();
//        System.exit(1);
        android.os.Process.killProcess(android.os.Process.myPid());
    }







}