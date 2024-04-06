package com.myzuji.sadk.asn1.parser;

import com.myzuji.sadk.algorithm.common.PKIException;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ASN1BigFileParser {
    private File f;
    private RandomAccessFile fs = null;

    public ASN1BigFileParser(File f) {
        this.f = f;
    }

    public ASN1Node parser() throws Exception {
        try {
            this.fs = new RandomAccessFile(this.f, "r");
            ASN1Node parentNode = this.parseASN1TLV(0L);
            this.Node_Parser(parentNode, 0);
            if (this.fs != null) {
                this.fs.close();
            }

            return parentNode;
        } catch (Exception var2) {
            throw new PKIException("the file's format is wrong,please check it if it is der,can not parse the data after Base64!");
        }
    }

    private ASN1Node parseASN1TLV(long startPos) throws Exception {
        try {
            ASN1Node node = new ASN1Node();
            node.f = this.f;
            this.fs.seek(startPos);
            int tag = this.fs.read();
            if (tag == 0) {
                return null;
            } else if (tag == -1) {
                throw new Exception("the file's format is wrong!");
            } else {
                this.getASN1ValueLength(this.f, startPos + 1L, node);
                node.tag = tag;
                return node;
            }
        } catch (Exception var5) {
            Exception e = var5;
            throw e;
        }
    }

    private void Node_Parser(ASN1Node parentNode, int recursiveCount) throws Exception {
        if (parentNode != null) {
            if (recursiveCount >= 50) {
                throw new Exception("the file is not good asn.1 format,Please check it!");
            } else {
                ++recursiveCount;
                int tagValue = parentNode.tag;
                if (tagValue >= 0 && tagValue <= 31 || tagValue >= 128 && tagValue <= 143) {
                    if (parentNode.isInfiniteLength) {
                        this.getInfiniteLength(parentNode);
                    }

                } else {
                    long valueStartPos = parentNode.valueStartPos;

                    for (long childNodeLength = 0L; childNodeLength < parentNode.valueLength && valueStartPos < this.f.length(); valueStartPos = parentNode.valueStartPos + childNodeLength) {
                        ASN1Node childNode = this.parseASN1TLV(valueStartPos);
                        if (childNode == null) {
                            break;
                        }

                        this.Node_Parser(childNode, recursiveCount);
                        if (childNode.isInfiniteLength) {
                            this.getInfiniteLength(childNode);
                        }

                        childNode.parentNode = parentNode;
                        parentNode.childNodes.add(childNode);
                        childNodeLength += childNode.totalLength;
                    }

                    if (parentNode.isInfiniteLength) {
                        this.getInfiniteLength(parentNode);
                    }

                }
            }
        }
    }

    private void getInfiniteLength(ASN1Node node) throws Exception {
        if (node.isInfiniteLength) {
            try {
                int previousByte;
                if (node.childNodes.size() == 0) {
                    previousByte = -1;
                    int currentByte = 0;
                    long valueLength = 0L;
                    long valueStartPos = node.valueStartPos;
                    this.fs.seek(valueStartPos);

                    while ((currentByte = this.fs.read()) != -1) {
                        ++valueLength;
                        if (previousByte == 0 && currentByte == 0) {
                            node.isInfiniteLength = false;
                            node.valueLength = valueLength;
                            node.totalLength = 2L + valueLength;
                            break;
                        }

                        previousByte = currentByte;
                    }
                } else {
                    node.valueLength = 0L;

                    for (previousByte = 0; previousByte < node.childNodes.size(); ++previousByte) {
                        ASN1Node child = (ASN1Node) node.childNodes.get(previousByte);
                        this.getInfiniteLength(child);
                        node.valueLength += child.totalLength;
                    }

                    node.isInfiniteLength = false;
                    node.totalLength = 2L + node.valueLength + 2L;
                }

            } catch (Exception var8) {
                Exception e = var8;
                throw e;
            }
        }
    }

    private void getASN1ValueLength(File f, long lengthStartPos, ASN1Node node) throws Exception {
        try {
            this.fs.seek(lengthStartPos);
            long length = (long) this.fs.read();
            if (length < 0L) {
                throw new EOFException("EOF found when length expected");
            } else if (length == 128L) {
                node.isInfiniteLength = true;
                node.berNode = true;
                node.valueLengthSize = 1;
                node.valueLength = Long.MAX_VALUE;
                node.valueStartPos = 1L + lengthStartPos;
                node.totalLength = Long.MAX_VALUE;
            } else {
                long size = 1L;
                if (length > 127L) {
                    size = length & 127L;
                    length = 0L;

                    for (int i = 0; (long) i < size; ++i) {
                        int next = this.fs.read();
                        if (next < 0) {
                            throw new EOFException("EOF found reading length");
                        }

                        length = (length << 8) + (long) next;
                    }

                    if (length < 0L) {
                        throw new IOException("corrupted stream - negative length found");
                    }

                    node.isInfiniteLength = false;
                    node.berNode = false;
                    node.valueLengthSize = (int) size;
                    node.valueLength = length;
                    node.valueStartPos = lengthStartPos + size + 1L;
                    node.totalLength = 2L + size + length;
                } else {
                    node.isInfiniteLength = false;
                    node.berNode = false;
                    node.valueLengthSize = (int) size;
                    node.valueLength = length;
                    node.valueStartPos = lengthStartPos + size;
                    node.totalLength = 1L + size + length;
                }

            }
        } catch (Exception var11) {
            Exception e = var11;
            throw e;
        }
    }
}
