 package com.wingman.beta;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class fillMsgService extends RemoteViewsService{
	@Override
	  public RemoteViewsFactory onGetViewFactory(Intent intent) {
	    return(new fillMsgAdapter(this.getApplicationContext(), intent));
	  }
}
