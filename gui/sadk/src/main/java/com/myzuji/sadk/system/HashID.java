package com.myzuji.sadk.system;

public class HashID {
    final String name;
    final int hashId;
    static final HashID SM3 = new HashID("SM3", 12289);
    static final HashID MD2 = new HashID("MD2", 8193);
    static final HashID MD5 = new HashID("MD5", 8194);
    static final HashID SHA1 = new HashID("SHA1", 4097);
    static final HashID SHA224 = new HashID("SHA224", 4098);
    static final HashID SHA256 = new HashID("SHA256", 4099);
    static final HashID SHA384 = new HashID("SHA384", 4100);
    static final HashID SHA512 = new HashID("SHA512", 4101);

    private HashID(String name, int hashId) {
        this.name = name;
        this.hashId = hashId;
    }

    public int hashCode() {
        int prime = 1;
        int result = 1;
        result = 31 * result + this.hashId;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            HashID other = (HashID) obj;
            return this.hashId == other.hashId;
        }
    }
}
