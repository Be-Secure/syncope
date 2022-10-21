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
package org.apache.syncope.core.persistence.jpa;

import org.apache.syncope.core.persistence.api.attrvalue.validation.PlainAttrValidationManager;
import org.apache.syncope.core.persistence.api.dao.AnyObjectDAO;
import org.apache.syncope.core.persistence.api.dao.AnySearchDAO;
import org.apache.syncope.core.persistence.api.dao.AuditConfDAO;
import org.apache.syncope.core.persistence.api.dao.DynRealmDAO;
import org.apache.syncope.core.persistence.api.dao.ExternalResourceDAO;
import org.apache.syncope.core.persistence.api.dao.GroupDAO;
import org.apache.syncope.core.persistence.api.dao.JPAJSONAnyDAO;
import org.apache.syncope.core.persistence.api.dao.PlainAttrDAO;
import org.apache.syncope.core.persistence.api.dao.PlainSchemaDAO;
import org.apache.syncope.core.persistence.api.dao.RealmDAO;
import org.apache.syncope.core.persistence.api.dao.UserDAO;
import org.apache.syncope.core.persistence.api.entity.AnyUtilsFactory;
import org.apache.syncope.core.persistence.api.entity.EntityFactory;
import org.apache.syncope.core.persistence.jpa.dao.OJPAJSONAnyDAO;
import org.apache.syncope.core.persistence.jpa.dao.OJPAJSONAnySearchDAO;
import org.apache.syncope.core.persistence.jpa.dao.OJPAJSONAuditConfDAO;
import org.apache.syncope.core.persistence.jpa.dao.OJPAJSONPlainSchemaDAO;
import org.apache.syncope.core.persistence.jpa.entity.OJPAJSONEntityFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@ConditionalOnExpression("#{'${provisioning.quartz.sql}' matches '.*oracle.*'}")
public class OJPAJSONPersistenceContext extends JPAJSONPersistenceContext {

    @ConditionalOnMissingBean(name = "oJPAJSONEntityFactory")
    @Bean
    public EntityFactory entityFactory() {
        return new OJPAJSONEntityFactory();
    }

    @ConditionalOnMissingBean(name = "oJPAJSONAnyDAO")
    @Bean
    public JPAJSONAnyDAO anyDAO(final @Lazy PlainSchemaDAO plainSchemaDAO) {
        return new OJPAJSONAnyDAO(plainSchemaDAO);
    }

    @ConditionalOnMissingBean(name = "oJPAJSONAnySearchDAO")
    @Bean
    public AnySearchDAO anySearchDAO(
            final @Lazy RealmDAO realmDAO,
            final @Lazy DynRealmDAO dynRealmDAO,
            final @Lazy UserDAO userDAO,
            final @Lazy GroupDAO groupDAO,
            final @Lazy AnyObjectDAO anyObjectDAO,
            final @Lazy PlainSchemaDAO schemaDAO,
            final @Lazy EntityFactory entityFactory,
            final AnyUtilsFactory anyUtilsFactory,
            final PlainAttrValidationManager validator) {

        return new OJPAJSONAnySearchDAO(
                realmDAO,
                dynRealmDAO,
                userDAO,
                groupDAO,
                anyObjectDAO,
                schemaDAO,
                entityFactory,
                anyUtilsFactory,
                validator);
    }

    @ConditionalOnMissingBean(name = "oJPAJSONAuditConfDAO")
    @Bean
    public AuditConfDAO auditConfDAO() {
        return new OJPAJSONAuditConfDAO();
    }

    @ConditionalOnMissingBean(name = "oJPAJSONPlainSchemaDAO")
    @Bean
    public PlainSchemaDAO plainSchemaDAO(
            final AnyUtilsFactory anyUtilsFactory,
            final @Lazy PlainAttrDAO plainAttrDAO,
            final @Lazy ExternalResourceDAO resourceDAO) {

        return new OJPAJSONPlainSchemaDAO(anyUtilsFactory, plainAttrDAO, resourceDAO);
    }
}
