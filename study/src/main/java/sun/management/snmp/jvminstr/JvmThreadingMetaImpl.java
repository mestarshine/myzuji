/*
 * Copyright (c) 2003, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package sun.management.snmp.jvminstr;

// java imports
//

// jmx imports
//
import javax.management.MBeanServer;

// jdmk imports
//
import com.sun.jmx.snmp.agent.SnmpMib;
        import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;

        import sun.management.snmp.jvmmib.JvmThreadingMeta;
import sun.management.snmp.jvmmib.JvmThreadInstanceTableMeta;

/**
 * The class is used for representing SNMP metadata for the "JvmThreading"
 * group.
 */
public class JvmThreadingMetaImpl extends JvmThreadingMeta {

    static final long serialVersionUID = -2104788458393251457L;

    /**
     * Constructor for the metadata associated to "JvmThreading".
     */
    public JvmThreadingMetaImpl(SnmpMib myMib,
                                SnmpStandardObjectServer objserv) {
        super(myMib, objserv);
    }

    /**
     * Factory method for "JvmThreadInstanceTable" table metadata class.
     *
     * You can redefine this method if you need to replace the default
     * generated metadata class with your own customized class.
     *
     * @param tableName Name of the table object ("JvmThreadInstanceTable")
     * @param groupName Name of the group to which this table belong
     *        ("JvmThreading")
     * @param mib The SnmpMib object in which this table is registered
     * @param server MBeanServer for this table entries (may be null)
     *
     * @return An instance of the metadata class generated for the
     *         "JvmThreadInstanceTable" table (JvmThreadInstanceTableMeta)
     *
     **/
    protected JvmThreadInstanceTableMeta
        createJvmThreadInstanceTableMetaNode(String tableName,
                                             String groupName,
                                             SnmpMib mib,
                                             MBeanServer server)  {
        return new JvmThreadInstanceTableMetaImpl(mib, objectserver);
    }
}
