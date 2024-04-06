package com.myzuji.sadk.org.bouncycastle.cms;

import java.util.*;

public class SignerInformationStore {
    private List all = new ArrayList();
    private Map table = new HashMap();

    public SignerInformationStore(Collection signerInfos) {
        SignerInformation signer;
        ArrayList list;
        for (Iterator it = signerInfos.iterator(); it.hasNext(); list.add(signer)) {
            signer = (SignerInformation) it.next();
            SignerId sid = signer.getSID();
            list = (ArrayList) this.table.get(sid);
            if (list == null) {
                list = new ArrayList(1);
                this.table.put(sid, list);
            }
        }

        this.all = new ArrayList(signerInfos);
    }

    public SignerInformation get(SignerId selector) {
        Collection list = this.getSigners(selector);
        return list.size() == 0 ? null : (SignerInformation) list.iterator().next();
    }

    public int size() {
        return this.all.size();
    }

    public Collection getSigners() {
        return new ArrayList(this.all);
    }

    public Collection getSigners(SignerId selector) {
        ArrayList results;
        if (selector.getIssuer() != null && selector.getSubjectKeyIdentifier() != null) {
            results = new ArrayList();
            Collection match1 = this.getSigners(new SignerId(selector.getIssuer(), selector.getSerialNumber()));
            if (match1 != null) {
                results.addAll(match1);
            }

            Collection match2 = this.getSigners(new SignerId(selector.getSubjectKeyIdentifier()));
            if (match2 != null) {
                results.addAll(match2);
            }

            return results;
        } else {
            results = (ArrayList) this.table.get(selector);
            return results == null ? new ArrayList() : new ArrayList(results);
        }
    }
}
