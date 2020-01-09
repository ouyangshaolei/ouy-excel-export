package com.gwghk.crm.common;



/**
 * @ClassName: Pager
 * @Description: API调用返回实体信息，集合
 * @date 2017年5月16日
 */
public class Pager extends Page<Object> {

    public Pager(com.github.pagehelper.Page<Object> p) {
        this.setPageNo(p.getPageNum());
        this.setPageSize(p.getPageSize());
        this.setTotal(p.getTotal());
        this.setTotalPage(p.getPages());
        this.setData(p.getResult());
    }
}
