package com.qubecad.coffeehack.customerloyality;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final String TAG = this.getClass().getName();
	private RatingBar rBar;
	private Socket socket;

	private static final int SERVERPORT = 6000;

	private static final String SERVER_IP = "192.168.1.108";
	private String trackInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button logtoken = (Button) findViewById(R.id.button1);

		logtoken.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				runScanner();

			}
		});

		updateLoyalityPoint();

	}

	private void updateLoyalityPoint() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		int loyalityPoints = sharedPrefs.getInt("points", 0);
		trackInfo = sharedPrefs.getString("trackInfo", "devin townsend ki");
		rBar = (RatingBar) findViewById(R.id.ratingBar1);
		rBar.setNumStars(loyalityPoints);

	}

	private void saveLoyalityPoints(int value) {

		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor editor = sharedPrefs.edit();
		sharedPrefs.edit().putInt("points", value);
		editor.commit();
		new Thread(new ClientThread()).start();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {

		super.onResume();

		updateLoyalityPoint();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void runScanner() {

		IntentIntegrator.initiateScan(this);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {

			String contents = intent.getStringExtra("SCAN_RESULT");
			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

			if (contents.matches("123456789")) {

				Log.d(TAG, "scanner result= " + contents);
				int points = (int) (rBar.getRating() + 1);
				rBar.setRating(points);
				saveLoyalityPoints(points);
				
				
				if (points >= 5) {
					Toast.makeText(this, "5 points you can claim a free coffee !!",
							Toast.LENGTH_LONG).show();
				} 
				sendMessage(trackInfo);

			} else if (contents.matches("987654321")) {
				rBar.setRating(0);
				saveLoyalityPoints(0);
			}
			

		}
	}
	
	private void sendMessage (String str){
		PrintWriter out=null;
		try {
			
			OutputStream outsock=socket.getOutputStream();
			
			out = new PrintWriter(new BufferedWriter(
					
					                    new OutputStreamWriter(outsock)),
					
					                    true);
			
			if(out!=null){
			out.println(str);
			}else {
				Log.d(TAG,"out=null");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
				
				           

	}
	class ClientThread implements Runnable {

		 
        @Override

        public void run() {

 

            try {

                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

 

                socket = new Socket(serverAddr, SERVERPORT);

 

            } catch (UnknownHostException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();

            }

 

        }

	
	}

}


