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

public class QuestionDetailListAdapter extends BaseAdapter
{
	private Context context;
	private Question question;
	private BitmapUtils bitmapUtils;
	
	public QuestionDetailListAdapter(Context context, ListView listView, Question question)
	{
		this.context = context;
		bitmapUtils = BitmapHelper.getBitmapUtils(context, listView, R.drawable.head_default, R.drawable.head_default);
		this.question = question;
	}
	
	public Question getQuestion()
	{
		return question;
	}
	
	@Override
	public int getCount()
	{
		if(question==null) return 0;
		else return question.getAnswerInfoList().size()+1;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		if(position==0) return 0;
		else return 1;
	}
	
	@Override
	public int getViewTypeCount()
	{
		return 2;
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
		int type = getItemViewType(position);
		
		if(convertView == null)
		{
			vh = new ViewHolder();
			switch(type)
			{
				case 0:
				{
					convertView = LayoutInflater.from(context).inflate(R.layout.item_questiondetail_ask, parent, false);
					vh.iv_ask_askerIcon = (ImageView) convertView.findViewById(R.id.iv_questiondetail_ask_askerIcon);
					vh.tv_ask_content = (TextView) convertView.findViewById(R.id.tv_questiondetail_ask_content);
					vh.tv_ask_time = (TextView) convertView.findViewById(R.id.tv_questiondetail_ask_time);
					vh.tv_ask_answerNum = (TextView) convertView.findViewById(R.id.tv_questiondetail_ask_answerNum);
					vh.vg_ask_divider = (ViewGroup) convertView.findViewById(R.id.vg_questiondetail_ask_divider);
				}break;
				
				case 1:
				{
					convertView = LayoutInflater.from(context).inflate(R.layout.item_questiondetail_answer, parent, false);
					vh.iv_answer_replierIcon = (ImageView) convertView.findViewById(R.id.iv_questiondetail_answer_replierIcon);
					vh.tv_answer_replierName = (TextView) convertView.findViewById(R.id.tv_questiondetail_answer_replierName);
					vh.tv_answer_content = (TextView) convertView.findViewById(R.id.tv_questiondetail_answer_content);
					vh.tv_answer_time = (TextView) convertView.findViewById(R.id.tv_questiondetail_answer_time);
				}break;
			}
			convertView.setTag(vh);
		}
		else
		{
			vh = (ViewHolder) convertView.getTag();
		}
		
		switch(type)
		{
			case 0:
			{
				String headIconUrl = "";
				if(question.getAskerInfo().getAccessaryFileUrlList()!=null && question.getAskerInfo().getAccessaryFileUrlList().size()!=0 )
				{
					headIconUrl = question.getAskerInfo().getAccessaryFileUrlList().get(0);
				}
				bitmapUtils.display(vh.iv_ask_askerIcon, headIconUrl);
				
				vh.tv_ask_content.setText(question.getAskInfo().getField(EnquiryTable.FIELD_CONTENT));
				vh.tv_ask_time.setText(question.getAskInfo().getField(EnquiryTable.FIELD_APPLYTIME));
				vh.tv_ask_answerNum.setText(""+question.getAnswerInfoList().size());
				
				if(getCount()>1) vh.vg_ask_divider.setVisibility(View.VISIBLE);
				else vh.vg_ask_divider.setVisibility(View.GONE);
			}break;
			
			case 1:
			{
				String headIconUrl = "";
				if(question.getAnswerInfoList().get(position-1).getReplierInfo().getAccessaryFileUrlList()!=null && question.getAnswerInfoList().get(position-1).getReplierInfo().getAccessaryFileUrlList().size()!=0 )
				{
					headIconUrl = question.getAnswerInfoList().get(position-1).getReplierInfo().getAccessaryFileUrlList().get(0);
				}
				bitmapUtils.display(vh.iv_answer_replierIcon, headIconUrl);
				
				vh.tv_answer_replierName.setText(question.getAnswerInfoList().get(position-1).getReplierInfo().getField(UserTable.FIELD_USERNAME));
				vh.tv_answer_content.setText(question.getAnswerInfoList().get(position-1).getAnswerInfo().getField(ReplyTable.FIELD_CONTENT));
				vh.tv_answer_time.setText(question.getAnswerInfoList().get(position-1).getAnswerInfo().getField(ReplyTable.FIELD_T_TIME));
			}break;
		}
		
		return convertView;
	}
	
	private class ViewHolder
	{
		private ImageView iv_ask_askerIcon;
		private TextView tv_ask_content;
		private TextView tv_ask_time;
		private TextView tv_ask_answerNum;
		private ViewGroup vg_ask_divider;
		
		private ImageView iv_answer_replierIcon;
		private TextView tv_answer_replierName;
		private TextView tv_answer_content;
		private TextView tv_answer_time;
	}
}
