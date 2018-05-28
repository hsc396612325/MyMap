package com.example.heshu.mymap.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heshu.mymap.R;
import com.example.heshu.mymap.adapter.CommentExpandAdapter;
import com.example.heshu.mymap.bean.CommentBean;
import com.example.heshu.mymap.bean.CommentDetailBean;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by heshu on 2018/5/27.
 */

public class ShowCommentActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mLinearLayout;
    private TextView bt;
    private CommentExpandAdapter adapter;
    private List<CommentDetailBean> mCommentsList;
    private ExpandableListView expandableListView;
    private CommentBean commentBean;
    private BottomSheetDialog dialog;
    private static final String TAG = "MainActivity";

    private String testJson = "{\n" +
            "\t\"code\": 1000,\n" +
            "\t\"message\": \"查看评论成功\",\n" +
            "\t\"data\": {\n" +
            "\t\t\"total\": 3,\n" +
            "\t\t\"list\": [{\n" +
            "\t\t\t\t\"id\": 42,\n" +
            "\t\t\t\t\"nickName\": \"程序猿\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"时间是一切财富中最宝贵的财富。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"三分钟前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\t\"commentId\": \"42\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"一个小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 41,\n" +
            "\t\t\t\t\"nickName\": \"设计狗\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"这世界要是没有爱情，它在我们心中还会有什么意义！这就如一盏没有亮光的走马灯。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 1,\n" +
            "\t\t\t\t\"createDate\": \"一天前\",\n" +
            "\t\t\t\t\"replyList\": [{\n" +
            "\t\t\t\t\t\"nickName\": \"沐風\",\n" +
            "\t\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\t\"commentId\": \"41\",\n" +
            "\t\t\t\t\t\"content\": \"时间总是在不经意中擦肩而过,不留一点痕迹.\",\n" +
            "\t\t\t\t\t\"status\": \"01\",\n" +
            "\t\t\t\t\t\"createDate\": \"三小时前\"\n" +
            "\t\t\t\t}]\n" +
            "\t\t\t},\n" +
            "\t\t\t{\n" +
            "\t\t\t\t\"id\": 40,\n" +
            "\t\t\t\t\"nickName\": \"产品喵\",\n" +
            "\t\t\t\t\"userLogo\": \"http://ucardstorevideo.b0.upaiyun.com/userLogo/9fa13ec6-dddd-46cb-9df0-4bbb32d83fc1.png\",\n" +
            "\t\t\t\t\"content\": \"笨蛋自以为聪明，聪明人才知道自己是笨蛋。\",\n" +
            "\t\t\t\t\"imgId\": \"xcclsscrt0tev11ok364\",\n" +
            "\t\t\t\t\"replyTotal\": 0,\n" +
            "\t\t\t\t\"createDate\": \"三天前\",\n" +
            "\t\t\t\t\"replyList\": []\n" +
            "\t\t\t}\n" +
            "\t\t]\n" +
            "\t}\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);
        initView();
    }

    private void initView(){


        expandableListView = findViewById(R.id.detail_page_lv_comment);
        mCommentsList = generateTestData();
        initExpandableListView(mCommentsList);

        mLinearLayout = (LinearLayout) findViewById(R.id.main_layout);
    }


    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);

        //默认展开所以回复
        adapter = new CommentExpandAdapter(this,commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i < commentList.size();i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: 当前评论id>>>" + mCommentsList.get(groupPosition).getId());
                showReplyDialog(v,groupPosition);

                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if(childPosition>=3){
                    Toast.makeText(ShowCommentActivity.this,"点击了加载更多",Toast.LENGTH_SHORT).show();
                }else {
                    showReplyReplyDialog(v,groupPosition, childPosition);
                }
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand: "+"展开第"+groupPosition+"分组");

            }
        });
    }
    /**
     * func:生成测试数据
     * @return 评论数据
     */
    private List<CommentDetailBean> generateTestData(){
        Gson gson = new Gson();
        commentBean = gson.fromJson(testJson, CommentBean.class);
        List<CommentDetailBean> commentList = commentBean.getData().getList();
        return commentList;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * func:弹出回复框
     */
    private void showReplyDialog(View parent, final int position){
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View contentView = layoutInflater.inflate(R.layout.activity_show_comment_dialog_layout, null);

        final EditText commentText = (EditText) contentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) contentView.findViewById(R.id.dialog_comment_bt);
        final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation((View) parent.getParent(), Gravity.BOTTOM, 0, 0);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){
                    mPopupWindow.dismiss();
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    CommentDetailBean.ReplyDetailBean detailBean = new CommentDetailBean.ReplyDetailBean("小红",replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(ShowCommentActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ShowCommentActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void showReplyReplyDialog(View parent,final int position,final int childPosition){
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View contentView = layoutInflater.inflate(R.layout.activity_show_comment_dialog_layout, null);

        final EditText commentText = (EditText) contentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) contentView.findViewById(R.id.dialog_comment_bt);
        final PopupWindow mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);


        ColorDrawable cd = new ColorDrawable(0x000000);
        mPopupWindow.setBackgroundDrawable(cd);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAtLocation((View) parent.getParent(), Gravity.BOTTOM, 0, 0);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    mPopupWindow.dismiss();
                    CommentDetailBean.ReplyDetailBean detailBean = new CommentDetailBean.ReplyDetailBean("小红",replyContent);
                    detailBean.setReplyname(mCommentsList.get(position).getReplyList().get(childPosition).getNickName());
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);
                    Toast.makeText(ShowCommentActivity.this,"回复成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ShowCommentActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}

