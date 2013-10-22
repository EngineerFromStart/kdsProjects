package com.parallellogic.test;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class arrayAdapter extends ArrayAdapter<Comment>{
	private Context context;
	private List<Comment> values;

	public arrayAdapter(Context context, int resource,List<Comment> values) {
		super(context, resource, values);
		this.context = context;
		this.values = values;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View rowView = convertView;//used for performace issues
		ViewHolder holder = null;//used for performace issues
		if (rowView == null){
			LayoutInflater inflater = (LayoutInflater) context
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.list_item, parent, false);
			holder = new ViewHolder();
			holder.index = (TextView) rowView.findViewById(R.id.index);
			holder.name = (TextView) rowView.findViewById(R.id.name);
			holder.int1 = (TextView) rowView.findViewById(R.id.int1);
			holder.int2 = (TextView) rowView.findViewById(R.id.int2);
			holder.float1 = (TextView) rowView.findViewById(R.id.float1);
			holder.float2 = (TextView) rowView.findViewById(R.id.float2);
			rowView.setTag(holder);
		}else{
			holder = (ViewHolder) rowView.getTag();
		}
		Comment comment = values.get(position);
		holder.index.setText(String.valueOf(position));
		holder.name.setText(comment.Name);
		holder.int1.setText(comment.Int1);
		holder.int2.setText(comment.Int2);
		holder.float1.setText(comment.Float1);
		holder.float2.setText(comment.Float2);
		
		return rowView;
	}
	private static class ViewHolder{
		private TextView index;
		private TextView name;
		private TextView int1;
		private TextView int2;
		private TextView float1;
		private TextView float2;
		ViewHolder(){
			
		}
	}
}
