 package com.wingman.beta;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class fillOptionService extends RemoteViewsService{
	@Override
	  public RemoteViewsFactory onGetViewFactory(Intent intent) {
	    return(new fillOptionAdapter(this.getApplicationContext(), intent));
	  }
}
