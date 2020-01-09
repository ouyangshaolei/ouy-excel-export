package com.gwghk.crm.config;

import com.gwghk.crm.common.ApiErrorCode;
import com.gwghk.crm.exception.SystemException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName: ResultInterceptor
 * @Description: 数据结果拦截器(拦截insert / update)
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class ResultInterceptor implements Interceptor {
    private static final List<String> EXCLUDING = new ArrayList<String>() {{
        add("LabelMapper.insertOnlyOne");
        add("LabelMapper.updateOnlyOne");
        add("LeadMapper.insertOnlyOne");
        add("LeadFilterMapper.insertOnlyOne");
        add("LabelRelationMapper.updateLabelRelation");
        add("LabelRelationMapper.deleteRelation");
        add("FocusMapper.insertOnlyOne");
        add("FocusMapper.deleteByF_T_C");
        add("RelationMapper.insertOnlyOne");
        add("LabelMapper.deleteByI_T");
        add("LeadFilterMapper.deleteByPrimaryKey");
        add("RelationMapper.deleteLeadRelation");
        add("RealAccountMapper.insertOnlyOne");
        add("RealAccountMapper.updateByID");
        add("LeadMapper.insertOnlyOneByCsAndMsg");
        add("RealAccountGroupMapper.insertOnlyOne");
        add("RealAccountGroupMapper.updateName");
        add("FollowMapper.updateNotifyStatus");
        add("RuleCreateInfoMapper.deleteByPkey");
        add("RuleRevokeInfoMapper.deleteByPkey");
        add("RuleAbolishInfoMapper.deleteByPkey");
        add("CustomServiceMapper.updateByCsGroupId");
        add("CfgInfoMapper.insertOnlyOne");
        add("ClaimInfoMapper.deleteByTheme");
        add("FollowSassignInfoMapper.deleteByTheme");
        add("CustomServiceMapper.eraseExtenOfDuplicate");
		add("FollowMapper.updateDegreeByLeadOrReal");
		add("FollowNotifyMapper.deleteNotify");
        add("CustomServiceMapper.deleteByPrimaryKey");//删除用户时，csid不存在于customerService表中，避免报错
		add("TagInfoMapper.insertOnlyOne");
		add("RealAccountGts2TagMapper.insertOnlyOne");
        add("LeadMapper.updateByIdDuplicateMsg");
        add("LeadMapper.updateByIdProject");
        add("LeadMapper.updateByIdCreateTime");
        add("LeadMapper.updateByPhoneRla");
        add("LeadMapper.updateByPhoneActivateRla");
        add("RealAccountMapper.updateActivateByAccount");
        add("RealAccountMapper.updateByAccount4Gts");
    }};

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //提取影响行数
        Object result = invocation.proceed();
        String methodId = ((MappedStatement) invocation.getArgs()[0]).getId();
        for (String str : EXCLUDING) {
            if (methodId.endsWith(str)) {//这些方法如果返回值为0，是正常情况，防止重复提交
                return result;
            }
        }

        if (Integer.parseInt(result.toString()) <= 0) {
            throw new SystemException(ApiErrorCode.DB_DATA_DONE_ERROR);
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
