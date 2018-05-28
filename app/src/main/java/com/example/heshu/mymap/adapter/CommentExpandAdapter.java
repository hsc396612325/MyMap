package com.example.heshu.mymap.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.heshu.mymap.R;
import com.example.heshu.mymap.bean.CommentDetailBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by heshu on 2018/5/21.
 */

public class CommentExpandAdapter extends BaseExpandableListAdapter {
    private List<CommentDetailBean> mCommentBeanList;
    private Context mContext;
    private static final String TAG = "CommentExpandAdapter";

    public CommentExpandAdapter(Context context, List<CommentDetailBean> commentBeanList){
        this.mContext = context;
        this.mCommentBeanList = commentBeanList;
    }

    @Override
    public int getGroupCount() {  //返回group分组的数量，在当前需求中指代评论的数量。
        return mCommentBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) { //返回所在group中child的数量，这里指代当前评论对应的回复数目
        if(mCommentBeanList.get(groupPosition).getReplyList() == null){
            return  0;
        }else {
            if(mCommentBeanList.get(groupPosition).getReplyList().size()>3){
                return 4;
            }else if(mCommentBeanList.get(groupPosition).getReplyList().size()>0){
                return mCommentBeanList.get(groupPosition).getReplyList().size();
            }else {
                return 0;
            }

        }

    }

    @Override
    public Object getGroup(int groupPosition) {//返回group的实际数据，这里指的是当前评论数据
        return mCommentBeanList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {  //返回group中某个child的实际数据，这里指的是当前评论的某个回复数据。
        if(mCommentBeanList.get(groupPosition).getReplyList().size()>3){
            return 4;
        }else if(mCommentBeanList.get(groupPosition).getReplyList().size()>0){
            return mCommentBeanList.get(groupPosition).getReplyList().size();
        }else {
            return 0;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {  //返回分组的id，一般将当前group的位置传给它。
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {  //返回分组中某个child的id，一般也将child当前位置传给它
        return getCombinedChildId(groupPosition,childPosition);
    }

    @Override
    public boolean hasStableIds() { //表示分组和子选项是否持有稳定的id，这里返回true即可。
        return true;
    }

    @Override  //即返回group的视图，一般在这里进行一些数据和视图绑定的工作，一般为了复用和高效，可以自定义ViewHolder
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder groupHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_show_comment_item_layout,parent,false);
            groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder)convertView.getTag();
        }
        Glide.with(mContext).load(R.drawable.user_logo)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.mipmap.ic_launcher)
                .centerCrop()
                .into(groupHolder.logo);

        groupHolder.tv_name.setText(mCommentBeanList.get(groupPosition).getNickName());
        groupHolder.tv_time.setText(mCommentBeanList.get(groupPosition).getCreateDate());
        groupHolder.tv_content.setText(mCommentBeanList.get(groupPosition).getContent());

        return convertView;
    }

    @Override  //返回分组中child子项的视图，比较容易理解，第一个参数是当前group所在的位置，第二个参数是当前child所在位置。
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_show_comment_reply_item_layout,parent,false);
            childHolder = new ChildHolder(convertView);
            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildHolder)convertView.getTag();
        }

        if(childPosition==3){
            childHolder.tv_name.setText("共"+mCommentBeanList.get(groupPosition).getReplyList().size()+"条回复");
            childHolder.tv_content.setText("");
        }else if(childPosition<3) {

            String replyUser = mCommentBeanList.get(groupPosition).getReplyList().get(childPosition).getNickName();
            String replyReplyName = mCommentBeanList.get(groupPosition).getReplyList().get(childPosition).getReplyname();
            if (!TextUtils.isEmpty(replyUser)) {
                if (replyReplyName != null) {
                    childHolder.tv_name.setText(replyUser + "回复" + replyReplyName + ":");

                } else {
                    childHolder.tv_name.setText(replyUser + ":");
                }
            } else {
                if (replyReplyName != null) {
                    childHolder.tv_name.setText("无名" + "回复" + replyReplyName + ":");

                } else {
                    childHolder.tv_name.setText("无名" + ":");
                }
            }
            childHolder.tv_content.setText(mCommentBeanList.get(groupPosition).getReplyList().get(childPosition).getContent());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {  //表示分组中的child是否可以选中，这里返回true。
        return true;
    }

    private class GroupHolder{
        private CircleImageView logo;
        private TextView tv_name, tv_content, tv_time;
        public GroupHolder(View view) {
            logo =  view.findViewById(R.id.comment_item_logo);
            tv_content = view.findViewById(R.id.comment_item_content);
            tv_name = view.findViewById(R.id.comment_item_userName);
            tv_time = view.findViewById(R.id.comment_item_time);
        }
    }

    private class ChildHolder{
        private TextView tv_name, tv_content;
        public ChildHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.reply_item_user);
            tv_content = (TextView) view.findViewById(R.id.reply_item_content);
        }
    }

    /**
     * 评论成功后插入一条数据
     */
    public void addTheCommentData(CommentDetailBean commentDetailBean){
        if(commentDetailBean!=null){

            mCommentBeanList.add(commentDetailBean);
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("评论数据为空!");
        }

    }

    public void  addTheReplyData(CommentDetailBean.ReplyDetailBean replyDetailBean ,int groupPosition){
        if(replyDetailBean != null){
            Log.d(TAG, "addTheReplyData: >>>>该刷新回复列表了:"+replyDetailBean.toString() );
            if(mCommentBeanList.get(groupPosition).getReplyList()!=null){
                mCommentBeanList.get(groupPosition).getReplyList().add(replyDetailBean);
            }else {
                List<CommentDetailBean.ReplyDetailBean> replyList = new ArrayList<>();
                replyList.add(replyDetailBean);
                mCommentBeanList.get(groupPosition).setReplyList(replyList);
            }
            notifyDataSetChanged();
        }else {
            throw new IllegalArgumentException("回复数据为空");
        }
    }

    /**
     * by moos on 2018/04/20
     * func:添加和展示所有回复
     * @param replyBeanList 所有回复数据
     * @param groupPosition 当前的评论
     */
    private void addReplyList(List<CommentDetailBean.ReplyDetailBean> replyBeanList, int groupPosition){
        if(mCommentBeanList.get(groupPosition).getReplyList() != null ){
            mCommentBeanList.get(groupPosition).getReplyList().clear();
            mCommentBeanList.get(groupPosition).getReplyList().addAll(replyBeanList);
        }else {

            mCommentBeanList.get(groupPosition).setReplyList(replyBeanList);
        }

        notifyDataSetChanged();
    }
}
