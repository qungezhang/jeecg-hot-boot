package org.jeecg.modules.demo.qungetest.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.qungetest.entity.QungeTestUser;
import org.jeecg.modules.demo.qungetest.service.IQungeTestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Title: Controller
 * @Description: QUNGE测试
 * @author： jeecg-boot
 * @date：   2019-03-06
 * @version： V1.0
 */
@RestController
@RequestMapping("/qungetest/qungeTestUser")
@Slf4j
public class QungeTestUserController {
	@Autowired
	private IQungeTestUserService qungeTestUserService;
	
	/**
	  * 分页列表查询
	 * @param qungeTestUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<QungeTestUser>> queryPageList(QungeTestUser qungeTestUser,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<QungeTestUser>> result = new Result<IPage<QungeTestUser>>();
		QueryWrapper<QungeTestUser> queryWrapper = new QueryWrapper<QungeTestUser>(qungeTestUser);
		Page<QungeTestUser> page = new Page<QungeTestUser>(pageNo,pageSize);
		//排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
		if(oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if("asc".equals(order)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			}else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}
		IPage<QungeTestUser> pageList = qungeTestUserService.page(page, queryWrapper);
		//log.debug("查询当前页："+pageList.getCurrent());
		//log.debug("查询当前页数量："+pageList.getSize());
		//log.debug("查询结果数量："+pageList.getRecords().size());
		//log.debug("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param qungeTestUser
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<QungeTestUser> add(@RequestBody QungeTestUser qungeTestUser) {
		Result<QungeTestUser> result = new Result<QungeTestUser>();
		try {
			qungeTestUserService.save(qungeTestUser);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param qungeTestUser
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<QungeTestUser> eidt(@RequestBody QungeTestUser qungeTestUser) {
		Result<QungeTestUser> result = new Result<QungeTestUser>();
		QungeTestUser qungeTestUserEntity = qungeTestUserService.getById(qungeTestUser.getId());
		if(qungeTestUserEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = qungeTestUserService.updateById(qungeTestUser);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<QungeTestUser> delete(@RequestParam(name="id",required=true) String id) {
		Result<QungeTestUser> result = new Result<QungeTestUser>();
		QungeTestUser qungeTestUser = qungeTestUserService.getById(id);
		if(qungeTestUser==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = qungeTestUserService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<QungeTestUser> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<QungeTestUser> result = new Result<QungeTestUser>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.qungeTestUserService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<QungeTestUser> queryById(@RequestParam(name="id",required=true) String id) {
		Result<QungeTestUser> result = new Result<QungeTestUser>();
		QungeTestUser qungeTestUser = qungeTestUserService.getById(id);
		if(qungeTestUser==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(qungeTestUser);
			result.setSuccess(true);
		}
		return result;
	}

}
