package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.pojo.BorrowAndRepayModel;

import java.util.Date;
import java.util.List;

/**
 * @author yecy
 * @date 2017/12/20 15:24
 */
public interface BorrowRepayModelService {

    List<BorrowAndRepayModel> findExpireToRepay();

    List<BorrowAndRepayModel> findExpireToRepay(String state);

    List<BorrowAndRepayModel> findExpireToRepay(String state, Date afterRepayTime);

    List<BorrowAndRepayModel> findExpireToRepay(String state, Date beforeRepayTime, Date afterRepayTime);
}
