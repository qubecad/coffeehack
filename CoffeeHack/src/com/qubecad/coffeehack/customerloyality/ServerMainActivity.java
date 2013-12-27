package com.qubecad.coffeehack.customerloyality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class ServerMainActivity extends Activity {
	private ServerSocket serverSocket;

	Handler updateConversationHandler;

	Thread serverThread = null;
private Context ctxt;
	

	public static final int SERVERPORT = 6000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_main);
		ctxt=this;
		updateConversationHandler = new Handler();
		
		 
		
		        this.serverThread = new Thread(new ServerThread());
		
		        this.serverThread.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_main, menu);
		return true;
	}
	
	class ServerThread implements Runnable {

		public void run() {
			Socket socket = null;
			try {
				serverSocket = new ServerSocket(SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {

				try {

					socket = serverSocket.accept();

					CommunicationThread commThread = new CommunicationThread(socket);
					new Thread(commThread).start();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	class CommunicationThread implements Runnable {

		private Socket clientSocket;

		private BufferedReader input;

		public CommunicationThread(Socket clientSocket) {

			this.clientSocket = clientSocket;

			try {

				this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				try {

					String read = input.readLine();

					updateConversationHandler.post(new updateUIThread(read));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	class updateUIThread implements Runnable {
		private String msg;

		public updateUIThread(String str) {
			this.msg = str;
		}

		@Override
		public void run() {
			//text.setText(text.getText().toString()+"Client Says: "+ msg + "\n");
			
			Log.d("server",msg);
			//PlayInDeezer(msg,ctxt);
			
			MusicPlayerHelper.SearchDeezerAndPlay(msg, ctxt);
		}
	}

	
	public static void PlayInDeezer(String deezerID, Context context) {

        String uri = "http://www.deezer.com/track/" + deezerID;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName("deezer.android.app", "com.deezer.android.ui.activity.LauncherActivity"));
        intent.setData(Uri.parse(uri));
        if (isCallable(intent, context)) {
            context.startActivity(intent);
        }

    }
	
	 private static boolean isCallable(Intent intent, Context context) {
	        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);
	        if (list.size() > 0) {


	            return true;
	        } else
	            return false;

	    }
}
