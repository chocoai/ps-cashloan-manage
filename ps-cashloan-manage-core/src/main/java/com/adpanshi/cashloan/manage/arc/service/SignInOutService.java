package com.adpanshi.cashloan.manage.arc.service;


import com.adpanshi.cashloan.manage.arc.model.SysUser;
import com.adpanshi.cashloan.manage.core.common.pojo.TokenModel;

/**
 * 登入登出
 * @author M
 *
 */
public interface SignInOutService {
	
	/**
	 * 登入
	 * @return SysUser
	 * @throws Exception
	 */
	SysUser platformSignIn(String username, String password) throws Exception;

	/**
	 * 创建token
	 * return TokenModel
	 * @throws Exception
	 * */
	TokenModel createToken(SysUser sysUser, Long roleId) throws Exception;

	/**
	 * 登出
	 */
	void platformSignOut(String token);

	/**
	 * @auther huangqin
	 * @description 判断是否已经登录
	 * @param userId
	 * @return boolean
	 * @data 2017-12-19
	 */
	boolean checkLogIn(Long userId);

	/**
	 * @auther huangqin
	 * @description 删除当前用户所存的所有token缓存
	 * @param userId
	 * @data 2017-12-19
	 */
	void removeUserToken(Long userId);
}
