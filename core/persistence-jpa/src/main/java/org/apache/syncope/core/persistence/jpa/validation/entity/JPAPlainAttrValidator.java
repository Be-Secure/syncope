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
package org.apache.syncope.core.persistence.jpa.validation.entity;

import jakarta.validation.ConstraintValidatorContext;
import org.apache.syncope.common.lib.types.EntityViolationType;
import org.apache.syncope.core.persistence.api.entity.PlainAttr;

public class JPAPlainAttrValidator extends AbstractValidator<PlainAttrCheck, PlainAttr<?>> {

    @Override
    public boolean isValid(final PlainAttr<?> attr, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        boolean isValid;
        if (attr == null) {
            isValid = true;
        } else {
            if (attr.getSchema().isUniqueConstraint()) {
                isValid = attr.getValues().isEmpty() && attr.getUniqueValue() != null;
            } else {
                isValid = !attr.getValues().isEmpty() && attr.getUniqueValue() == null;

                if (!attr.getSchema().isMultivalue()) {
                    isValid &= attr.getValues().size() == 1;
                }
            }

            if (!isValid) {
                LOG.error("Invalid values for attribute schema={}, values={}",
                        attr.getSchema().getKey(), attr.getValuesAsStrings());

                context.buildConstraintViolationWithTemplate(
                        getTemplate(EntityViolationType.InvalidValueList,
                                "Invalid values " + attr.getValuesAsStrings())).
                        addPropertyNode(attr.getSchema().getKey()).addConstraintViolation();
            }
        }

        return isValid;
    }
}
