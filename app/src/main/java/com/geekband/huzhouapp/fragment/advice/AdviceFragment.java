package com.geekband.huzhouapp.fragment.advice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);


        mAdvice_class_gridView = (GridView) view.findViewById(R.id.advice_class_gridView);
        mAdvice_class_gridView.setOnItemClickListener(this);
        mAdvice_class_gridView.setSelector(R.color.blue_background);

        //初始化分类列表
        initClassList();

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
        Toast.makeText(getActivity(),"功能还未开放",Toast.LENGTH_SHORT).show();
    }
}
