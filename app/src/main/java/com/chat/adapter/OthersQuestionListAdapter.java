package com.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chat.adapter.pojo.Question;
import com.database.pojo.EnquiryTable;
import com.database.pojo.ReplyTable;
import com.database.pojo.UserTable;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.utils.BitmapHelper;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

public class OthersQuestionListAdapter extends BaseAdapter
{
	private Context context;
	private List<Question> questionList;
	private BitmapUtils bitmapUtils;
	
	public OthersQuestionListAdapter(Context context, ListView listView)
	{
		this.context = context;
		questionList = new ArrayList<>();
		bitmapUtils = BitmapHelper.getBitmapUtils(context, listView, R.drawable.head_default, R.drawable.head_default);
	}
	
	public List<Question> getQuestionList()
	{
		return questionList;
	}
	
	@Override
	public int getCount()
	{
		return questionList.size();
	}

	@Override
	public Question getItem(int position)
	{
		return questionList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder vh;
		if(convertView==null)
		{
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_othersquestion_question, parent, false);
			vh.iv_askerIcon = (ImageView) convertView.findViewById(R.id.iv_othersquestion_askerIcon);
			vh.tv_askerName = (TextView) convertView.findViewById(R.id.tv_othersquestion_askerName);
			vh.tv_askContent = (TextView) convertView.findViewById(R.id.tv_othersquestion_askContent);
			vh.tv_replierName = (TextView) convertView.findViewById(R.id.tv_othersquestion_replierName);
			vh.tv_answerContent = (TextView) convertView.findViewById(R.id.tv_othersquestion_answerContent);
			vh.tv_replierNum = (TextView) convertView.findViewById(R.id.tv_othersquestion_replierNum);
			vh.vg_answer = (ViewGroup) convertView.findViewById(R.id.vg_othersquestion_answer);
			convertView.setTag(vh);
		}
		else
		{
			vh = (ViewHolder) convertView.getTag();
		}
		
		String headIconUrl = "";
		if(getItem(position).getAskerInfo().getAccessaryFileUrlList()!=null && getItem(position).getAskerInfo().getAccessaryFileUrlList().size()!=0 )
		{
			headIconUrl = getItem(position).getAskerInfo().getAccessaryFileUrlList().get(0);
		}
		bitmapUtils.display(vh.iv_askerIcon, headIconUrl);
		
		vh.tv_askerName.setText(getItem(position).getAskerInfo().getField(UserTable.FIELD_USERNAME));
		vh.tv_askContent.setText(getItem(position).getAskInfo().getField(EnquiryTable.FIELD_CONTENT));
		vh.tv_replierNum.setText(""+getItem(position).getAnswerInfoList().size());
		if(getItem(position).getAnswerInfoList().size()!=0)
		{
			vh.vg_answer.setVisibility(View.VISIBLE);
			vh.tv_replierName.setText(getItem(position).getAnswerInfoList().get(0).getReplierInfo().getField(UserTable.FIELD_USERNAME));
			vh.tv_answerContent.setText(getItem(position).getAnswerInfoList().get(0).getAnswerInfo().getField(ReplyTable.FIELD_CONTENT));
		}
		else
		{
			vh.vg_answer.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	private class ViewHolder
	{
		private ImageView iv_askerIcon;
		private TextView tv_askerName;
		private TextView tv_askContent;
		private TextView tv_replierNum;
		private TextView tv_replierName;
		private TextView tv_answerContent;
		private ViewGroup vg_answer;
	}
	
	class ImageLoader
	{
		
	}
}
