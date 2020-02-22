package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysLog;
import com.myzuji.backend.rpt.base.BaseRptImpl;
import org.springframework.stereotype.Repository;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Repository
public class SysLogRptImpl extends BaseRptImpl<SysLog> implements SysLogRpt {

    @Override
    public void save(SysLog sysLog) {
        saveOrUpdate(sysLog);
    }
}
