package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * @author 8470
 * @description FaceBook发布信息
 * @create 2018-07-26 15:34
 **/

public class FaceBookPublishInfo {

    /**
     * 发帖所用名
     */
    private String nickname;

    /**
     * 页面显示的帖子发布时间
     */
    private String pagetime;

    /**
     * 帖子定位地点
     */
    private String site;

    /**
     * 点赞人
     */
    private String likenames;

    /**
     * 点赞数量
     */
    private String likenum;

    /**
     * 评论数量
     */
    private String commentsnum;


    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPagetime() {
        return pagetime;
    }

    public void setPagetime(String pagetime) {
        this.pagetime = pagetime;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLikenames() {
        return likenames;
    }

    public void setLikenames(String likenames) {
        this.likenames = likenames;
    }

    public String getLikenum() {
        return likenum;
    }

    public void setLikenum(String likenum) {
        this.likenum = likenum;
    }

    public String getCommentsnum() {
        return commentsnum;
    }

    public void setCommentsnum(String commentsnum) {
        this.commentsnum = commentsnum;
    }
}
