package com.adpanshi.cashloan.manage.report.domain;

import com.adpanshi.cashloan.data.common.enums.ChannelBizType;
import com.adpanshi.cashloan.data.common.enums.ChannelType;
import com.adpanshi.cashloan.manage.report.bo.ManageReportResponseBo;

/**
 * 信审接口
 * Created by zsw on 2018/8/6 0006.
 */
public interface AuditDomain {

    /**
     * 获取信审信息
     * @param userDataId    用户数据ID
     * @param channelType   渠道类型
     * @param channelBizType    渠道业务类型
     */
    ManageReportResponseBo getAuditInfo(Integer userDataId, ChannelType channelType, ChannelBizType channelBizType);
}
