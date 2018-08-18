package com.adpanshi.cashloan.manage.cl.model.moxie;

/**
 * @author 8470
 * @description FaceBook好友信息
 * @create 2018-07-26 16:06
 **/

public class FaceBookFriendsInfo {

    /**
     * 好友用户名
     */
    private String nickname;

    /**
     * 好友的朋友数量
     */
    private String numoffriends;

    /**
     * 共同好友数量
     */
    private String mutualfriends;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNumoffriends() {
        return numoffriends;
    }

    public void setNumoffriends(String numoffriends) {
        this.numoffriends = numoffriends;
    }

    public String getMutualfriends() {
        return mutualfriends;
    }

    public void setMutualfriends(String mutualfriends) {
        this.mutualfriends = mutualfriends;
    }
}
