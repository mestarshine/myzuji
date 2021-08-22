package com.myzuji.backend.common.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultWrapper {

    private String code;

    private String msg;
}
