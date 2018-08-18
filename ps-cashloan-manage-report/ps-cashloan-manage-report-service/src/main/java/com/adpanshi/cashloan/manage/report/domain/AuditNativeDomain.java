package com.adpanshi.cashloan.manage.report.domain;

import com.adpanshi.cashloan.common.utils.DateUtil;
import com.adpanshi.cashloan.data.appdata.bo.AppApplicationDataBo;
import com.adpanshi.cashloan.data.appdata.bo.AppCallRecordDataBo;
import com.adpanshi.cashloan.data.appdata.bo.AppCommunicationDataBo;
import com.adpanshi.cashloan.data.appdata.domain.AppDataDomain;
import com.adpanshi.cashloan.data.common.enums.ChannelBizType;
import com.adpanshi.cashloan.data.common.enums.ChannelType;
import com.adpanshi.cashloan.data.thirdparty.equifax.bo.EquifaxReportDataBo;
import com.adpanshi.cashloan.data.thirdparty.equifax.domain.EquifaxCreditReportDomain;
import com.adpanshi.cashloan.data.thirdparty.moxie.bo.MoxieSIMBo;
import com.adpanshi.cashloan.data.thirdparty.moxie.bo.MoxieSNSBo;
import com.adpanshi.cashloan.data.thirdparty.moxie.domain.MoxieSIMDomain;
import com.adpanshi.cashloan.data.thirdparty.moxie.domain.MoxieSNSDomain;
import com.adpanshi.cashloan.data.user.bo.UserDataBo;
import com.adpanshi.cashloan.data.user.bo.UserMetaDataBo;
import com.adpanshi.cashloan.data.user.domain.UserDataDomain;
import com.adpanshi.cashloan.manage.report.bo.ManageReportResponseBo;
import com.adpanshi.cashloan.manage.report.enums.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tool.util.StringUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * 信审接口
 * Created by zsw on 2018/8/6 0006.
 */
@Service("auditDomain")
public class AuditNativeDomain implements AuditDomain {
    private static Logger logger = LoggerFactory.getLogger(AuditNativeDomain.class);

    @Resource
    private UserDataDomain userDataDomain;
    @Resource
    private EquifaxCreditReportDomain equifaxCreditReportDomain;
    @Resource
    private AppDataDomain appDataDomain;
    @Resource
    private MoxieSIMDomain moxieSIMDomain;
    @Resource
    private MoxieSNSDomain moxieSNSDomain;

    private ChannelBizType channelBizType;
    private ChannelType channelType;

    @Override
    public ManageReportResponseBo getAuditInfo(Integer userDataId, ChannelType channelType, ChannelBizType channelBizType) {
        this.channelType = channelType;
        this.channelBizType = channelBizType;
        UserDataBo userDataBo = userDataDomain.get(userDataId);
        List<UserMetaDataBo> userMetaDataBoList = userDataBo.getUserMetaDataList();
        descSort(userMetaDataBoList);
        Integer metaDataId = getMetaDataId(userMetaDataBoList);
        if (metaDataId == null) {
            logger.error("未获取到原始数据ID,userDataId={}, channelType={}", userDataId, channelType.getContent());
            return ManageReportResponseBo.error(StatusCode.NO_REPORT);
        }
        String metaData = getMetaData(metaDataId);
        if (StringUtil.isBlank(metaData)) {
            logger.error("未获取到信审信息,userDataId={}, channelType={}, channelBizType={}, metaDataId={}", userDataId, channelType.getContent(), channelBizType.getContent(), metaDataId);
            return ManageReportResponseBo.error(StatusCode.NO_REPORT);
        }
        return ManageReportResponseBo.success(metaData);
    }

    /**
     * 原始数据倒叙排列
     * @param userMetaDataBoList    原始数据集合
     */
    private void descSort(List<UserMetaDataBo> userMetaDataBoList){
        Collections.sort(userMetaDataBoList, new Comparator<UserMetaDataBo>(){
            @Override
            public int compare(UserMetaDataBo o1, UserMetaDataBo o2) {
                Date date2 = DateUtil.string2Date(o2.getCreateTime(), DateUtil.ymdhmsSSSFormat);
                Date date1 = DateUtil.string2Date(o1.getCreateTime(), DateUtil.ymdhmsSSSFormat);
                return date2.compareTo(date1);
            }
        });
    }

    /**
     * 获取原始数据ID
     * @param userMetaDataBoList    原始数据集合
     * @return  原始数据ID
     */
    private Integer getMetaDataId(List<UserMetaDataBo> userMetaDataBoList) {
        Integer metaDataId = null;
        for (UserMetaDataBo userMetaDataBo : userMetaDataBoList) {
            ChannelType userMetaDataBoChannelType = userMetaDataBo.getChannelType();
            ChannelBizType userMetaDataBoChannelBizType = userMetaDataBo.getChannelBizType();
            if (userMetaDataBoChannelType.getValue().equals(channelType.getValue()) &&
                    userMetaDataBoChannelBizType.getValue().equals(channelBizType.getValue())) {
                metaDataId = userMetaDataBo.getChannelDataId();
                break;
            }
        }
        return metaDataId;
    }

    /**
     * 获取原始数据
     * @param metaDataId    原始数据ID
     * @return  原始数据
     */
    private String getMetaData(Integer metaDataId) {
        // TODO 嵌套可以用适配器，有时间再改
        if (ChannelType.EQUIFAXREPORT.getValue().equals(channelType.getValue()) &&
                ChannelBizType.EQUIFAX_REPORT_ANALYZE.getValue().equals(channelBizType.getValue())) {
            EquifaxReportDataBo equifaxReportDataBo = equifaxCreditReportDomain.getMetaData(metaDataId);
            if (equifaxReportDataBo == null) {
                logger.error("未获取到原始数据,channelType={}, channelBizType={}", channelType.getContent(), channelBizType.getContent());
                return null;
            }
            return equifaxReportDataBo.getOriginalData();
        } else if (ChannelType.PSAPP.getValue().equals(channelType.getValue()) &&
                ChannelBizType.APP_APPLICATION.getValue().equals(channelBizType.getValue())) {
            AppApplicationDataBo appApplicationDataBo = appDataDomain.getAppApplicationData(metaDataId);
            if (appApplicationDataBo == null) {
                logger.error("未获取到原始数据,channelType={}, channelBizType={}", channelType.getContent(), channelBizType.getContent());
                return null;
            }
            return appApplicationDataBo.getOriginalData();
        } else if (ChannelType.PSAPP.getValue().equals(channelType.getValue()) &&
                ChannelBizType.APP_COMMUNICATION.getValue().equals(channelBizType.getValue())) {
            AppCommunicationDataBo appCommunicationDataBo = appDataDomain.getAppCommunicationData(metaDataId);
            if (appCommunicationDataBo == null) {
                logger.error("未获取到原始数据,channelType={}, channelBizType={}", channelType.getContent(), channelBizType.getContent());
                return null;
            }
            return appCommunicationDataBo.getOriginalData();
        } else if (ChannelType.MOXIESIMCARD.getValue().equals(channelType.getValue()) &&
                ChannelBizType.MOXIE_SIM_CARD_INFO.getValue().equals(channelBizType.getValue())) {
            MoxieSIMBo moxieSIMBo = moxieSIMDomain.getMoxieSIMData(metaDataId);
            if (moxieSIMBo == null) {
                logger.error("未获取到原始数据,channelType={}, channelBizType={}", channelType.getContent(), channelBizType.getContent());
                return null;
            }
            return moxieSIMBo.getMetaData();
        } else if (ChannelType.MOXIESNS.getValue().equals(channelType.getValue()) &&
                ChannelBizType.MOXIE_SNS_INFO.getValue().equals(channelBizType.getValue())) {
            MoxieSNSBo moxieSNSBo = moxieSNSDomain.getMetaData(metaDataId);
            if (moxieSNSBo == null) {
                logger.error("未获取到原始数据,channelType={}, channelBizType={}", channelType.getContent(), channelBizType.getContent());
                return null;
            }
            return moxieSNSBo.getMetaData();
        } else if (ChannelType.PSAPP.getValue().equals(channelType.getValue()) &&
                ChannelBizType.APP_CALLRECORDS.getValue().equals(channelBizType.getValue())) {
            AppCallRecordDataBo callRecordDataBo = appDataDomain.getAppCallRecordData(metaDataId);
            if(callRecordDataBo == null){
                logger.error("未获取到原始数据,channelType={}, channelBizType={}", channelType.getContent(), channelBizType.getContent());
                return null;
            }
            return callRecordDataBo.getOriginalData();
        }
        return null;
    }
}
