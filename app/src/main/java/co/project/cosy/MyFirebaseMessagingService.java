package co.project.cosy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import co.project.cosy.Constant.Constant;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public  int notifyID = 9001;
    public static final int NOTIFICATION_ID = 1;
    Random r = new Random();

    NotificationCompat.Builder builder;
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String msg = remoteMessage.getData().get("m");

        String msgArray = remoteMessage.getData().get(Constant.MSG_KEY) + " ";
        try {
            JSONObject jsonObjj = new JSONObject(msgArray);
            System.out.println("getting notification array-->" + jsonObjj);
            String notification_type = jsonObjj.getString("notification_type");

            if (notification_type.equals("text")) {
                System.out.println("text messges gets executed");
                sedTextNotification(msgArray);
            }

            else if(notification_type.equals("image")){

                System.out.println("image messges gets executed");
                sendNotificationImage(msgArray);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

       //    sendNotification(remoteMessage.getNotification().getBody());

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
//    private void scheduleJob() {
//        // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
//        // [END dispatch_job]
//    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, signin.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setSmallIcon(android.R.color.transparent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }



    private void sedTextNotification(String text)
    {
        String userName="";
        String message="";

        try {
            JSONObject jsonObj = new JSONObject(text);

            message=jsonObj.get("msg").toString();
            userName=jsonObj.get("title").toString();


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, Home.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("title",userName);
        resultIntent.putExtra("msg",message);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(userName)
                .setContentText(message)
                .setStyle(new NotificationCompat
                .BigTextStyle().bigText(message))
                 .setSmallIcon(android.R.color.transparent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);

        // Set pending intent
        mNotifyBuilder.setContentIntent(contentIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(message);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setSound(defaultSoundUri);
        // Post a notification
        notifyID= (int) Math.random();
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());


    }



        /* SEND NOTIFICATION IMAGE*/
    private void sendNotificationImage(String greetMsg) {

        String message="";
        String title="";
        String imageURL="";
        Bitmap remote_picture = null;

        try {
            JSONObject jsonObj = new JSONObject(greetMsg);
            message=jsonObj.getString("msg");
            title=jsonObj.getString("title");
            imageURL=jsonObj.getString("image");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText(message);

        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);

        Intent resultIntent = new Intent(this, Home.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
               .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(android.R.color.transparent)
                .setStyle(notiStyle);


        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);
        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentTitle(title);
        mNotifyBuilder.setContentText(message);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        notifyID= (int) Math.random();
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }



    private void sendNotificationsurvey(String text)
    {

        String userName="";
        String message="";
        String survey_id="";
        String survey_name11="";
        try {
            JSONObject jsonObj = new JSONObject(text);

            message=jsonObj.get("msg").toString();
            userName=jsonObj.get("title").toString();
            survey_id=jsonObj.get("surveyID").toString();
            survey_name11=jsonObj.get("survey_name").toString();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, Home.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("title",userName);
        resultIntent.putExtra("msg",message);
        resultIntent.putExtra("survey_id",survey_id);
        resultIntent.putExtra("name",survey_name11);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,resultIntent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(userName)
                .setContentText(message)
                .setStyle(new NotificationCompat
                        .BigTextStyle().bigText(message))
                .setSmallIcon(android.R.color.transparent)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);

        // Set pending intent
        mNotifyBuilder.setContentIntent(contentIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(message);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setSound(defaultSoundUri);
        // Post a notification
        notifyID= (int) Math.random();
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());


    }


}