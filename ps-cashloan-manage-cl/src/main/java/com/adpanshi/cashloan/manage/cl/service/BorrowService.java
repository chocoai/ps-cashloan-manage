package com.adpanshi.cashloan.manage.cl.service;



import com.adpanshi.cashloan.manage.cl.model.Borrow;
import com.adpanshi.cashloan.manage.cl.model.expand.BorrowModel;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map; /**
 * @author Vic Tang
 * @Description: 借款订单service
 * @date 2018/8/1 14:34
 */
public interface BorrowService {
    /**
     * 借款部分还款信息
     * @param map
     * @param current
     * @param pageSize
     * @return Page<BorrowModel>
     * @throws
     * @author Vic Tang
     * @date 2018/8/3 9:56
     * */
    Page<BorrowModel> listBorrowModel(Map<String, Object> map, Integer current, Integer pageSize);

    /**
     * 批量保存
     */
    int saveAll(List<Borrow> borrowList);
    /**
     * 导出借款贷后订单
     */
    List listBorrowOutModel(Map<String, Object> params);

    Borrow getById(Long borrowId);
    /**
     * 修改借款状态
     * @param id
     * @param state
     * @return
     */
    int modifyState(Long id, String state);

    /**
     * 查询
     * @param searchMap
     * @return
     */
    List<Borrow> findBorrowByMap(Map<String, Object> searchMap);
    /**
     * 借款详细信息
     * @param borrowId
     * @return
     */
    BorrowModel getModelByBorrowId(Long borrowId);
}
