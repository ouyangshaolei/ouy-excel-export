package com.gwghk.crm.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gwghk.crm.check.vo.export.ExcelExportInVO;
import com.gwghk.crm.check.vo.export.F00000;
import com.gwghk.crm.common.Pager;
import com.gwghk.crm.common.Util;
import com.gwghk.crm.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @apiDefine excel 报表导出/查询
 */
@RestController
@RequestMapping("/excel")
public class ExcelExportController {
    private ExcelExportService excelExportService;
    private HttpServletResponse response;

    @Autowired
    public ExcelExportController(ExcelExportService excelExportService, HttpServletResponse response) {
        this.excelExportService = excelExportService;
        this.response = response;
    }


    /**
     * @api {get} /excel/export 每日跟进率所有N名单
     * @apiGroup excel
     * @apiName F00001
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00001)
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDayStart 日期开始时间(yyyy-mm-dd)
     * @apiParam {String} paramJson.reportDayEnd 日期结束时间(yyyy-mm-dd)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.followOne 首次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.followTwo 二次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.followThree 三次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidTwo 首次有效二次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidTotal 首次有效跟进率
     * @apiSuccess (成功响应) {Json} data.data.firstFollowRate 首次跟进率
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidRate 首次有效二次跟进率
     * @apiSuccess (成功响应) {Json} data.data.secondFollowRate 二次跟进率
     * @apiSuccess (成功响应) {Json} data.data.thirdlyFollowRate 三次跟进率
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */

    /**
     * @api {get} /excel/export 每日跟进率其他名单
     * @apiGroup excel
     * @apiName F00002
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00002)
     * @apiParam {String} paramJson.custType 名单名称(不包含直接N名单)
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDayStart 日期开始时间(yyyy-mm-dd)
     * @apiParam {String} paramJson.reportDayEnd 日期结束时间(yyyy-mm-dd)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.firstFollowRate 首次跟进率
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidRate 首次有效二次跟进率
     * @apiSuccess (成功响应) {Json} data.data.secondFollowRate 二次跟进率
     * @apiSuccess (成功响应) {Json} data.data.thirdlyFollowRate 三次跟进率
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */
    /**
     * @api {get} /excel/export 直接N名单跟进
     * @apiGroup excel
     * @apiName F00002a
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00002a)
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDayStart 日期开始时间(yyyy-mm-dd)
     * @apiParam {String} paramJson.reportDayEnd 日期结束时间(yyyy-mm-dd)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.followOne 首次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.followTwo 二次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.followThree 三次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidTwo 首次有效二次跟进次数
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidTotal 首次有效跟进率
     * @apiSuccess (成功响应) {Json} data.data.firstFollowRate 首次跟进率
     * @apiSuccess (成功响应) {Json} data.data.firstFollowValidRate 首次有效二次跟进率
     * @apiSuccess (成功响应) {Json} data.data.secondFollowRate 二次跟进率
     * @apiSuccess (成功响应) {Json} data.data.thirdlyFollowRate 三次跟进率
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */
    /**
     * @api {get} /excel/export 名单分配日报表
     * @apiGroup excel
     * @apiName F00003
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00003)
     * @apiParam {String} paramJson.custType 名单名称
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDayStart 日期开始时间(yyyy-mm-dd)
     * @apiParam {String} paramJson.reportDayEnd 日期结束时间(yyyy-mm-dd)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.custCount 分配名单数
     * @apiSuccess (成功响应) {Json} data.data.custCountFact 实际名单数
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */

    /**
     * @api {get} /excel/export 总名单分配
     * @apiGroup excel
     * @apiName F00004
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00004)
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDayStart 日期开始时间(yyyy-mm-dd)
     * @apiParam {String} paramJson.reportDayEnd 日期结束时间(yyyy-mm-dd)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.custCount 分配名单数
     * @apiSuccess (成功响应) {Json} data.data.custCountFact 实际名单数
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */
    /**
     * @api {get} /excel/export 名单分配报表(月)
     * @apiGroup excel
     * @apiName F00005
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00005)
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDay 日期(yyyy-mm)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.custCountFact 实际名单数
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */
    /**
     * @api {get} /excel/export 业绩统计报表_个人
     * @apiGroup excel
     * @apiName F00006
     * @apiDescription 数据查询接口(post): url中/export换成/data 即可
     * @apiParam {Json} paramJson 参数
     * @apiParam {String} paramJson.tmplateCode 导出模板(tmplateCode=F00006)
     * @apiParam {String} [paramJson.empName] 员工(多个员工用 | 分割)
     * @apiParam {String} paramJson.reportDay 日期(yyyy-mm)
     * @apiParam {String} [paramJson.filesName] 导出文件名(可自定义 /export导出时使用)
     * @apiParam {String} [paramJson.sheetName] sheet名称(可自定义 /export导出时使用)
     * @apiParam {String} track 跟踪号
     * @apiParam {String} token token
     * @apiSuccess (成功响应) {String} code 0
     * @apiSuccess (成功响应) {String} msg ok!
     * @apiSuccess (成功响应) {Json} data
     * @apiSuccess (成功响应) {Json} data.data
     * @apiSuccess (成功响应) {Json} data.data.reportDay 日期
     * @apiSuccess (成功响应) {Json} data.data.empName 员工
     * @apiSuccess (成功响应) {Json} data.data.custType 名单名称
     * @apiSuccess (成功响应) {Json} data.data.reachRate 达标率
     * @apiSuccess (成功响应) {Json} data.data.activateTotal 总激活数
     * @apiSuccess (成功响应) {Json} data.data.activateLevelTotal 合计
     * @apiSuccess (成功响应) {Json} data.data.accountLevelTranslate 等级
     * @apiSuccess (成功响应) {Json} data.data.accountLevelTranslateTotal 等级对应的数量
     * @apiSuccess (成功响应) {Json} data.data.targetNum 个人激活目标
     * @apiSuccessExample {Json} 成功响应示例:
     * {
     * "code": "0" ,
     * "msg": "ok!"
     * "data": "{}"
     * "ext": track
     * }
     * @apiErrorExample {Json} 失败响应示例:
     * {
     * "code": "1001" ,
     * "msg": "错误，请检查！"
     * "ext": track
     * }
     */
    @GetMapping("/export")
    public void report(ExcelExportInVO vo) {
        excelExportService.export(vo.getVo(), response);
    }

    @PostMapping("/data")
    public Object data(ExcelExportInVO vo) {
        F00000 vo1 = vo.getVo();
        Page<Object> p = PageHelper.startPage(vo1.getPageNo(), vo1.getPageSize());
        excelExportService.retunData(vo1);
        return new Pager(p);
    }
}
