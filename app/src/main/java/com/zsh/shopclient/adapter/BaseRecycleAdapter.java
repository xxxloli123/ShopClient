package com.zsh.shopclient.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zsh.shopclient.R;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12 0012.
 *  RecyclerView.Adapter的优化与封装 http://blog.csdn.net/u013000152/article/details/51925509
 *  ListView和RecyclerView的使用和性能优化总结 http://blog.csdn.net/xz10561/article/details/43936421
 */

public abstract class BaseRecycleAdapter<Type> extends RecyclerView.Adapter{
    protected static final int TYPE_FOOTER = 1;
    private int layoutId;
    private Activity activity;
    private List<Type> list;
    private FootLoadListener footLoadListener;
    private boolean isEnd,isReplace;
    public abstract void update(List<Type> list,boolean isReplace);
    protected BaseRecycleAdapter(Activity activity, List<Type> list) {
        this.layoutId = 0;
        this.list = list;
        isEnd = false;
        this.activity = activity;
    }
    protected BaseRecycleAdapter(Activity activity, int layoutId, List<Type> list) {
        this.layoutId = layoutId;
        this.list = list;
        isEnd = false;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() > position)
            return super.getItemViewType(position);
        else if(null != footLoadListener && !isEnd)
            footLoadListener.loadMoreListener();
        return TYPE_FOOTER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FootViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.window_loading_progress_bar,parent,false));
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 :list.size()+1;
    }
    protected void setVisibility(FootViewHolder holder,int state){
        holder.load.setVisibility(state);
        holder.title.setText(state == View.GONE ? "<v_v>已经到底了" : "玩命加载中");
    }
    public void setLoadMoreListener(FootLoadListener footLoadListener) {
        this.footLoadListener = footLoadListener;
    }

    protected void setDatas(List<Type> datas) {
        isEnd = false;
        this.list = datas;
    }
    protected void addAll(List<Type> datas){
        if(null != datas) {
            this.list.addAll(datas);
            isEnd = false;
        }else
            isEnd = true;
    }
    protected int size(){
        return isNull() ?0: list.size();
    }
    protected Type getPosition(int position){return list.get(position);}
    protected Activity getActivity() {
        return activity;
    }

    protected boolean isEnd() {
        return isEnd;
    }
    protected void setEnd(boolean end) {
        isEnd = end;
    }

    protected boolean isReplace() {
        return isReplace;
    }
    protected void setReplace(boolean replace) {
        isReplace = replace;
    }

    protected boolean isNull(){
        return isEnd = null == list;
    }
    protected void setFootLoadListener(FootLoadListener footLoadListener) {
        this.footLoadListener = footLoadListener;
    }

    protected final class FootViewHolder extends RecyclerView.ViewHolder{
        ProgressBar load;
        TextView title;
        public FootViewHolder(View itemView) {
            super(itemView);
            load = (ProgressBar)itemView.findViewById(R.id.progress_bar);
            title = (TextView) itemView.findViewById(R.id.text_loading_title);
        }
    }
    public interface FootLoadListener {
        public void loadMoreListener();
    }
    protected interface ItemClickListener{
        public void onClickListener(View view, int id);
    }
}
