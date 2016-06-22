package com.geekband.huzhouapp.fragment.advice;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.chat.activity.ExpertActivity;
import com.chat.activity.OthersQuestionActivity;
import com.chat.activity.PersonalQuestionActivity;
import com.chat.adapter.QuestionMenuItemListAdapter;
import com.chat.adapter.pojo.QuestionMenuItem;
import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.baseadapter.CommonAdapter;
import com.geekband.huzhouapp.baseadapter.ViewHolder;
import com.geekband.huzhouapp.vo.ClassInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12
 */
public class AdviceFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<ClassInfo> classList ;
    private GridView mAdvice_class_gridView;

    private ListView lv_questionMenuListView;
    private QuestionMenuItemListAdapter questionMenuItemListAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);


        mAdvice_class_gridView = (GridView) view.findViewById(R.id.advice_class_gridView);
        mAdvice_class_gridView.setOnItemClickListener(this);

//        QuestionMenuFragment qmf = new QuestionMenuFragment();
//        getFragmentManager().beginTransaction().replace(R.id.container, qmf).commit();

        //初始化分类列表
        initClassList();


        lv_questionMenuListView = (ListView) view.findViewById(R.id.lv_questionmenu_menuItemList);
        initVar();
        initView();
        initListener();

        return view;
    }

    private void initClassList() {
        classList = new ArrayList<>();
        classList.add(new ClassInfo(R.drawable.law,"法律"));
        classList.add(new ClassInfo(R.drawable.criminal,"刑侦"));
        classList.add(new ClassInfo(R.drawable.traffic_police,"交警"));
        classList.add(new ClassInfo(R.drawable.firefighting,"消防"));
        classList.add(new ClassInfo(R.drawable.psychology,"心理"));
        classList.add(new ClassInfo(R.drawable.political,"政工"));
        classList.add(new ClassInfo(R.drawable.document,"公文写作"));
        classList.add(new ClassInfo(R.drawable.informatization,"信息化"));

        mAdvice_class_gridView.setAdapter(new CommonAdapter<ClassInfo>(getActivity(),classList,R.layout.item_class) {
            @Override
            public void convert(ViewHolder viewHolder, ClassInfo item) {
                viewHolder.setImage(R.id.class_imageView_item, item.getImageId());
                viewHolder.setText(R.id.class_title_item,item.getClassTitle());
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdvice_class_gridView.setSelector(R.color.blue_background);
        //跳转设置
        Intent intent = new Intent();
        intent.putExtra("class",position);
        intent.setClass(getActivity(),ExpertActivity.class);
        startActivity(intent);
    }



    private void initView()
    {
        lv_questionMenuListView.setAdapter(questionMenuItemListAdapter);
    }

    private void initVar()
    {
        questionMenuItemListAdapter = new QuestionMenuItemListAdapter(getActivity());

        questionMenuItemListAdapter.addItem(new QuestionMenuItem(R.drawable.app_icon_org_contacts, MENU_ITEM_1));
        questionMenuItemListAdapter.addItem(new QuestionMenuItem(R.drawable.app_icon_wddb, MENU_ITEM_2));
        questionMenuItemListAdapter.addItem(new QuestionMenuItem(R.drawable.app_icon_t, MENU_ITEM_3));
    }

    private void initListener()
    {
        lv_questionMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                switch(position)
                {
                    case 0:
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), PersonalQuestionActivity.class);
                        startActivity(intent);
                    }break;

                    case 1:
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ExpertActivity.class);
                        startActivity(intent);
                    }break;

                    case 2:
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), OthersQuestionActivity.class);
                        startActivity(intent);
                    }break;
                }
            }
        });
    }

    public static final String MENU_ITEM_1 = "我的提问";
    public static final String MENU_ITEM_2 = "咨询专家";
    public static final String MENU_ITEM_3 = "最新问答";
}
