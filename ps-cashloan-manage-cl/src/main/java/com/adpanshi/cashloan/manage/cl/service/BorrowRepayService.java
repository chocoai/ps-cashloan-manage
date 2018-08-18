package com.adpanshi.cashloan.manage.cl.service;


import com.adpanshi.cashloan.manage.cl.model.Borrow;
import com.adpanshi.cashloan.manage.cl.model.BorrowMain;
import com.adpanshi.cashloan.manage.cl.model.BorrowRepay;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowRepayModel;
import com.adpanshi.cashloan.manage.cl.pojo.OverDueUser;
import com.github.pagehelper.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Vic Tang
 * @Description: 还款计划service
 * @date 2018/7/31 17:26
 */
public interface BorrowRepayService {
    /**
     * 查询还款计划列表
     * @param params
     * @param currentPage
     * @param pageSize
     * @return Page<BorrowRepayModel>
     * @throws
     * @author Vic Tang
     * @date 2018/7/31 20:38
     */
    Page<BorrowRepayModel> listModel(Map<String, Object> params, Integer currentPage, Integer pageSize);
    /**
     *  立即还款
     * @param param
     * @return void
     * @throws
     * @author Vic Tang
     * @date 2018/7/31 20:57
     * */
    void confirmRepayNew(Map<String, Object> param)throws Exception;
    /**
     * 文件解析批量还款
     * @param repayFile
     * @param type
     * @param userId
     * @return List<List<String>>
     * @throws Exception
     * @author Vic Tang
     * @date 2018/8/1 11:10
     * */
    List<List<String>> fileBatchRepay(MultipartFile repayFile, String type, Long userId) throws Exception;
    /**
     *  根据主键查询还款计划
     * @param id
     * @return BorrowRepay
     * @throws
     * @author Vic Tang
     * @date 2018/8/1 19:02
     * */
    BorrowRepay getById(Long id);

    /**
     * 逾期未入催
     * @param params
     * @param currentPage
     * @param pageSize
     * @return
     */
    Page<BorrowModel> listModelNotUrge(Map<String, Object> params,
                                             int currentPage, int pageSize);
    /**
     * 根据分期账单生成还款计划
     * @param borrowList
     * @return
     */
    void genRepayPlans(List<Borrow> borrowList, BorrowMain borrowMain);
    /**
     * 根据分期账单生成还款计划(模拟环境调用)
     * @param borrowList
     * @param borrowMain
     * @param isMockData 是否为模拟数据
     * @return
     */
    void genRepayPlans(List<Borrow> borrowList, BorrowMain borrowMain, Boolean isMockData);
    /**
     * 导出文件查询
     * @param params
     * @return
     */
    List listExport(Map<String, Object> params);

    List<BorrowRepay> listSelective(Map<String, Object> paramMap);

    int updateLate(BorrowRepay br);
    /**
     * 查询所有逾期未还款用户
     * @return
     */
    List<OverDueUser> listOverDueUser();
}
