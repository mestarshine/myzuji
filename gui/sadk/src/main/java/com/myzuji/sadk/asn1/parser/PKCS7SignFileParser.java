package com.myzuji.sadk.asn1.parser;

import java.io.File;
import java.util.ArrayList;

public class PKCS7SignFileParser {
    private ASN1Node digestAlgorithm_node;
    private ASN1Node sourceData_node;
    private ASN1Node certificate_node;
    private ASN1Node singerinfo_node;
    private File f;

    public ASN1Node getDigestAlgorithm_node() {
        return this.digestAlgorithm_node;
    }

    public ASN1Node getSourceData_node() {
        return this.sourceData_node;
    }

    public ASN1Node getCertificate_node() {
        return this.certificate_node;
    }

    public ASN1Node getSingerinfo_node() {
        return this.singerinfo_node;
    }

    public PKCS7SignFileParser(File f) {
        this.f = f;
    }

    public void parser() throws Exception {
        ASN1BigFileParser parser = new ASN1BigFileParser(this.f);
        ASN1Node root = parser.parser();
        ASN1Node context = (ASN1Node) root.childNodes.get(1);
        ASN1Node sequnence_1 = (ASN1Node) context.childNodes.get(0);
        ArrayList child_sequnence_1 = sequnence_1.childNodes;
        if (child_sequnence_1.size() == 5) {
            this.digestAlgorithm_node = (ASN1Node) child_sequnence_1.get(1);
            this.sourceData_node = (ASN1Node) child_sequnence_1.get(2);
            this.certificate_node = (ASN1Node) child_sequnence_1.get(3);
            this.singerinfo_node = (ASN1Node) child_sequnence_1.get(4);
        } else if (child_sequnence_1.size() == 6) {
            this.digestAlgorithm_node = (ASN1Node) child_sequnence_1.get(1);
            this.sourceData_node = (ASN1Node) child_sequnence_1.get(2);
            this.certificate_node = (ASN1Node) child_sequnence_1.get(3);
            this.singerinfo_node = (ASN1Node) child_sequnence_1.get(5);
        }

    }
}
