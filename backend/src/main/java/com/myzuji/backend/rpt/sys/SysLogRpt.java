package com.myzuji.backend.rpt.sys;

import com.myzuji.backend.domain.system.SysLog;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public interface SysLogRpt {

    void save(SysLog sysLog);
}
