package com.adpanshi.cashloan.manage.cl.service.impl;


import com.adpanshi.cashloan.manage.cl.mapper.expand.RegisterStatisticMapper;
import com.adpanshi.cashloan.manage.cl.pojo.RegisterAuthStatistic;
import com.adpanshi.cashloan.manage.cl.service.RegisterStatisticService;
import com.adpanshi.cashloan.manage.core.common.util.DateUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Vic Tang
 * @Description: 注册统计serviceImpl
 * @date 2018/8/22 17:26
 */
@Service("registerStatisticService")
public class RegisterStatisticServiceImpl implements RegisterStatisticService {
    @Resource
    private RegisterStatisticMapper registerStatisticMapper;

    @Override
    public Map<String, Object> getStatisticByDistribution(String time) {
        Map<String, Object> rtMap = new HashMap<String, Object>();
        rtMap.put("age", registerStatisticMapper.getStatisticByAge(time));
        rtMap.put("gender", registerStatisticMapper.getStatisticByGender(time));
        return rtMap;
    }

    @Override
    public Map<String,Object> getStatisticByAuth(String time, String range) {
        String currentDate = DateUtil.getNowDate();
        List<String> dateList = new ArrayList<>();
        List<Integer> registerList = new ArrayList<>();
        List<Integer> totalList = new ArrayList<>();
        List<Integer> idAuthList = new ArrayList<>();
        List<Integer> bankAuthList = new ArrayList<>();
        List<Integer> contactAuthList = new ArrayList<>();
        List<Float> totalRatioList = new ArrayList<>();
        List<Float> idRatioList = new ArrayList<>();
        List<Float> bankRatioList = new ArrayList<>();
        List<Float> contactRatioList = new ArrayList<>();
        List<RegisterAuthStatistic> authList = registerStatisticMapper.getStatisticByAuth(time, range);
        long time1 = DateUtil.parse(authList.get(authList.size()-1).getDate(),DateUtil.DATEFORMAT_STR_002).getTime();
        long time2 = DateUtil.parse(authList.get(0).getDate(),DateUtil.DATEFORMAT_STR_002).getTime();
        range = String.valueOf((time1 - time2)/ (1000*3600*24));
        for(int i = Integer.parseInt(range);i >= 0;i--){
            String date =DateUtil.addDay(currentDate,-1*i);
            dateList.add(date);
        }
//        String[] dateArray = (String[]) dateList.toArray();
        for(int i = 0;i < dateList.size();i++){
            Iterator<RegisterAuthStatistic> it = authList.iterator();
            boolean flag = false;
            while(it.hasNext()){
                RegisterAuthStatistic auth = it.next();
                if(dateList.get(i).equals(auth.getDate())){
                    it.remove();
                    flag = true;
                    registerList.add(auth.getRegisterCount());
                    totalList.add(auth.getTotalCount());
                    totalRatioList.add(auth.getTotalRatio());
                    idAuthList.add(auth.getIdCount());
                    idRatioList.add(auth.getIdRatio());
                    bankAuthList.add(auth.getBankCount());
                    bankRatioList.add(auth.getBankRatio());
                    contactAuthList.add(auth.getContactCount());
                    contactRatioList.add(auth.getContactRatio());
                    break;
                }
            }
            if(!flag){
                registerList.add(0);
                totalList.add(0);
                totalRatioList.add((float) 0);
                idAuthList.add(0);
                idRatioList.add((float) 0);
                bankAuthList.add(0);
                bankRatioList.add((float) 0);
                contactAuthList.add(0);
                contactRatioList.add((float) 0);
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("registerList",registerList);
        map.put("totalRatioList",totalRatioList);
        map.put("idAuthList",idAuthList);
        map.put("idRatioList",idRatioList);
        map.put("bankAuthList",bankAuthList);
        map.put("bankRatioList",bankRatioList);
        map.put("contactAuthList",contactAuthList);
        map.put("contactRatioList",contactRatioList);
        map.put("totalList",totalList);
        return map;
    }

    @Override
    public Map<String, Object> getStatisticIndex(String time) {
        return registerStatisticMapper.getStatisticIndex(time);
    }
}
