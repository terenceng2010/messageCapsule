package com.terence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootReceiver extends BroadcastReceiver{

	private static final String TAG = OnBootReceiver.class.getSimpleName();

	
	@Override
	public void onReceive(Context context, Intent intent){
		ReminderManager reminderMgr = new ReminderManager(context);
		reminderMgr.setReminder();
	}

}
