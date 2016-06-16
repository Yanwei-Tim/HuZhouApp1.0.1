package com.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chat.adapter.pojo.Question;
import com.database.pojo.EnquiryTable;
import com.geekband.huzhouapp.R;

import java.util.ArrayList;
import java.util.List;


public class QuestionListAdapter extends BaseAdapter
{
	private Context context;
	private List<Question> questionList;
	
	public QuestionListAdapter(Context context)
	{
		this.context = context;
		questionList = new ArrayList<>();
	}
	
	public List<Question> getQuestionList()
	{
		return this.questionList;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_personalquestion_question, parent, false);
			vh.tv_title = (TextView) convertView.findViewById(R.id.tv_personalquestion_title);
			vh.tv_time = (TextView) convertView.findViewById(R.id.tv_personalquestion_time);
			vh.tv_answerNum = (TextView) convertView.findViewById(R.id.tv_personalquestion_answerNum);
			vh.tv_isReslove = (TextView) convertView.findViewById(R.id.tv_personalquestion_isResolved);
			convertView.setTag(vh);
		}
		else
		{
			vh = (ViewHolder) convertView.getTag();
		}
		
		vh.tv_title.setText(getItem(position).getAskInfo().getField(EnquiryTable.FIELD_CONTENT));
		vh.tv_time.setText(getItem(position).getAskInfo().getField(EnquiryTable.FIELD_APPLYTIME));
		vh.tv_answerNum.setText(""+getItem(position).getAnswerInfoList().size());
		
		return convertView;
	}
	
	private class ViewHolder
	{
		private TextView tv_title;
		private TextView tv_time;
		private TextView tv_answerNum;
		private TextView tv_isReslove;
	}
}
