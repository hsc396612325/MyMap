package com.example.heshu.mymap.bean;

import java.util.List;

/**
 * Created by heshu on 2018/5/21.
 */

public class CommentMessageBean {
    private int id;
    private String nickName;
    private String userLogo;
    private String content;
    private String imgId;
    private int replyTotal;
    private String createDate;
    private List<ReplyDetailBean> replyList;

    public CommentMessageBean(String nickName, String content, String createDate) {
        this.nickName = nickName;
        this.content = content;
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setReplyTotal(int replyTotal) {
        this.replyTotal = replyTotal;
    }

    public int getReplyTotal() {
        return replyTotal;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setReplyList(List<ReplyDetailBean> replyList) {
        this.replyList = replyList;
    }

    public List<ReplyDetailBean> getReplyList() {
        return replyList;
    }


    public static class ReplyDetailBean {
        private String nickName;
        private String userLogo;
        private int id;
        private String commentId;
        private String content;
        private String status;
        private String createDate;
        private String Replyname;

        public ReplyDetailBean(String nickName, String content) {
            this.nickName = nickName;
            this.content = content;
        }

        public String getReplyname() {
            return Replyname;
        }
        public void setReplyname(String replyname) {
            Replyname = replyname;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }

        public String getUserLogo() {
            return userLogo;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateDate() {
            return createDate;
        }

    }
}
