package com.myzuji.sadk.algorithm.common;

public class PKIException extends PKIExceptionKit {

    private static final long serialVersionUID = 7888097347110728103L;

    public PKIException(String _errDesc, Exception _history) {
        super(_errDesc, _history);
    }

    public PKIException(String _errCode, String _errDesc, Exception _history) {
        super(_errCode, _errDesc, _history);
    }

    public PKIException(String _errCode, String _errDesc) {
        super(_errCode, _errDesc);
    }

    public PKIException(String _errDesc) {
        super(_errDesc);
    }
}
