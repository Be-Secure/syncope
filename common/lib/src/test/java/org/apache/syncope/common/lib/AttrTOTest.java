/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.common.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.syncope.common.lib.to.AttrTO;
import org.junit.jupiter.api.Test;

public class AttrTOTest {

    @Test
    public void compareTo_equals() {
        AttrTO first = new AttrTO.Builder().schema("schema").value("value").build();
        AttrTO second = new AttrTO.Builder().schema("schema").value("value").build();
        assertEquals(0, first.compareTo(second));
    }

    @Test
    public void compareTo_different() {
        AttrTO first = new AttrTO.Builder().schema("schema1").value("value1").build();
        AttrTO second = new AttrTO.Builder().schema("schema2").value("value2").build();
        assertEquals(-1, first.compareTo(second));
        assertEquals(1, second.compareTo(first));
    }

    @Test
    public void compareTo_differentSchema_sameValue() {
        AttrTO first = new AttrTO.Builder().schema("schema1").value("value").build();
        AttrTO second = new AttrTO.Builder().schema("schema2").value("value").build();
        assertEquals(-1, first.compareTo(second));
    }

    @Test
    public void compareTo_sameSchema_differentValue() {
        AttrTO first = new AttrTO.Builder().schema("schema").value("value1").build();
        AttrTO second = new AttrTO.Builder().schema("schema").value("value2").build();
        assertEquals(-1, first.compareTo(second));
    }
}
