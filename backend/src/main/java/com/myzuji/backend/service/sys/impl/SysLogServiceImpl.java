package com.myzuji.backend.service.sys.impl;

import com.myzuji.backend.domain.system.SysLog;
import com.myzuji.backend.rpt.sys.SysLogRpt;
import com.myzuji.backend.service.sys.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogRpt sysLogRpt;

    @Override
    public void save(SysLog sysLog) {
        sysLogRpt.save(sysLog);
    }
}
