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
import com.geekband.huzhouapp.adapter.AdviceGridAdapter;
import com.geekband.huzhouapp.vo.ClassInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12
 */
public class AdviceFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<ClassInfo> classList ;
    private GridView mAdvice_class_gridView;
    private AdviceGridAdapter mAdviceGridAdapter;

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

        mAdviceGridAdapter = new AdviceGridAdapter(classList,getActivity());
        mAdvice_class_gridView.setAdapter(mAdviceGridAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        RelativeLayout class_layout = (RelativeLayout) view.findViewById(R.id.class_layout);
//        class_layout.setBackgroundColor(getResources().getColor(R.color.title_background));
        mAdvice_class_gridView.setSelector(R.color.blue_background);
        Toast.makeText(getActivity(),"功能还未开放",Toast.LENGTH_SHORT).show();
    }
}
