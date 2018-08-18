package com.adpanshi.cashloan.manage.cl.model.expand;

import com.adpanshi.cashloan.manage.cl.model.BorrowTemplate;

/**
 * @author Vic Tang
 * @Description: TODO
 * @date 2018/8/3 14:54
 */
public class BorrowTemplateModel extends BorrowTemplate {
    /**
     * 综合费用(费用详细信息累加)
     */
    private Boolean isOldUser;//是否为复借用户
    private Boolean isCutOpen;//是否需要使用砍头息
    private Boolean isMaxPenaltyOpen;//是否需要执行最高逾期


    public Boolean getOldUser() {
        return isOldUser;
    }

    public void setOldUser(Boolean oldUser) {
        isOldUser = oldUser;
    }

    public Boolean getCutOpen() {
        return isCutOpen;
    }

    public void setCutOpen(Boolean cutOpen) {
        isCutOpen = cutOpen;
    }

    public Boolean getMaxPenaltyOpen() {
        return isMaxPenaltyOpen;
    }

    public void setMaxPenaltyOpen(Boolean maxPenaltyOpen) {
        isMaxPenaltyOpen = maxPenaltyOpen;
    }

    /** 分期期数*/
    public Long getStage(){
        Long cycle = Long.parseLong(this.getCycle());
        Long timeLimit = Long.parseLong(this.getTimeLimit());
        return timeLimit / cycle;
    }
}
