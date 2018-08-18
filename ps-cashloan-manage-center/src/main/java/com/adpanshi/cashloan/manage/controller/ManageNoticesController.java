package com.adpanshi.cashloan.manage.controller;


import com.adpanshi.cashloan.manage.arc.service.SysDictDetailService;
import com.adpanshi.cashloan.manage.cl.model.Notices;
import com.adpanshi.cashloan.manage.cl.service.NoticesService;
import com.adpanshi.cashloan.manage.core.common.context.Constant;
import com.adpanshi.cashloan.manage.core.common.util.RdPage;
import com.adpanshi.cashloan.manage.core.common.util.StringUtil;
import com.adpanshi.cashloan.manage.pojo.ResultModel;
import com.github.pagehelper.Page;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 消息管理 Controller
 * @version 2.0.0
 * @date: 2017-12-24 18:11
 * @author: minge
 */
@Controller
@Scope("prototype")
@RequestMapping("/manage/noticesManage/")
public class ManageNoticesController extends ManageBaseController {

	@Resource
	private NoticesService noticesService;
	@Resource
	private SysDictDetailService sysDictDetailService;

    /**
     * @auther lifei
     * @description 获取消息列表
     * @data 2017-12-16
     */
	@RequestMapping(value = "list.htm", method = {RequestMethod.POST})
	public ResponseEntity<ResultModel> noticesList(
			@RequestParam(value = "searchParams", required = false) String searchParams,
			@RequestParam(value = "current") Integer current,
			@RequestParam(value = "pageSize") Integer pageSize
	) throws Exception {
		Map<String, Object> params = parseToMap(searchParams,true);
		Page<Notices> page = noticesService.queryNoticesList(params,current,pageSize);
		Map result = new HashMap(16);
		result.put(Constant.RESPONSE_DATA, page);
		result.put(Constant.RESPONSE_DATA_PAGE, new RdPage(page));
		return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
	}

    /**
     * @auther lifei
     * @description 添加消息列表
     * @data 2017-12-16
     */
	@RequestMapping(value = "saveOrUpdate.htm", method = {RequestMethod.POST})
	public ResponseEntity<ResultModel> saveOrUpdate(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "content", required = false) String content,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "state", required = false) String state
	) throws Exception {
		Notices notices = new Notices();
		notices.setId(id);
		notices.setUserId(-1L);
		notices.setTitle(title);
		notices.setContent(content);
		notices.setType(type);
		notices.setCreateTime(new Date());
		if(StringUtil.isBlank(id)){
			notices.setState("10");
			noticesService.insert(notices);
		}else{
			notices.setState(state);
			noticesService.updateById(notices);
		}

		return new ResponseEntity<>(ResultModel.ok(), HttpStatus.OK);
	}

	/**
	* @Description:
	* @Param: [id]
	* @return: org.springframework.http.ResponseEntity<com.fentuan.cashloan.manage.model.ResultModel>
	* @Author: Mr.Wange
	* @Date: 2018/7/24
	*/
	@RequestMapping(value = "info.htm", method = {RequestMethod.POST})
	public ResponseEntity<ResultModel> info(@RequestParam(value = "id") Long id){
		Notices notices = noticesService.findByPrimary(id);
		return new ResponseEntity<>(ResultModel.ok(notices), HttpStatus.OK);
	}

	@RequestMapping(value = "noticesTypeList.htm", method = {RequestMethod.POST})
	public ResponseEntity<ResultModel> configDropDownList() {
		this.logger.info("------系统管理-参数配置-角色查询下拉列表查询------");
		//读库标志
		Map<String, Object> data = new HashMap<>(4);
		data.put("typeCode", "NOTICES_TYPE");
		Map<String, Object> result = new HashMap<>(4);
		result.put("typeList", sysDictDetailService.listByTypeCode(data));
		return new ResponseEntity<>(ResultModel.ok(result), HttpStatus.OK);
	}

}
