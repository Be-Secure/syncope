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
package org.apache.syncope.client.console.panels;

import static org.apache.syncope.client.console.panels.AbstractModalPanel.LOG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.syncope.client.console.commons.Constants;
import org.apache.syncope.client.console.pages.AbstractBasePage;
import org.apache.syncope.client.console.rest.ResourceRestClient;
import org.apache.syncope.client.console.wicket.markup.html.bootstrap.dialog.BaseModal;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxCheckBoxPanel;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxDropDownChoicePanel;
import org.apache.syncope.client.console.wicket.markup.html.form.AjaxTextFieldPanel;
import org.apache.syncope.common.lib.to.AbstractSchemaTO;
import org.apache.syncope.common.lib.to.ProvisionTO;
import org.apache.syncope.common.lib.to.ResourceTO;
import org.apache.syncope.common.lib.to.VirSchemaTO;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

public class VirSchemaDetails extends AbstractSchemaDetailsPanel {

    private static final long serialVersionUID = 5979623248182851337L;

    private final ResourceRestClient resourceRestClient = new ResourceRestClient();

    private Map<Long, String> anys = new HashMap<>();

    private final AjaxDropDownChoicePanel<Long> provision;

    public VirSchemaDetails(final String id,
            final PageReference pageReference,
            final BaseModal<AbstractSchemaTO> modal) {
        super(id, pageReference, modal);

        final AjaxCheckBoxPanel readonly = new AjaxCheckBoxPanel("readonly", getString("readonly"),
                new PropertyModel<Boolean>(schemaTO, "readonly"));
        schemaForm.add(readonly);

        final AjaxDropDownChoicePanel<String> resource = new AjaxDropDownChoicePanel<>(
                "resource", getString("resource"), new PropertyModel<String>(schemaTO, "resource"));
        resource.setChoices(
                CollectionUtils.collect(resourceRestClient.getAll(), new Transformer<ResourceTO, String>() {

                    @Override
                    public String transform(final ResourceTO input) {
                        return input.getKey();
                    }
                }, new ArrayList<String>()));

        resource.setOutputMarkupId(true);
        resource.addRequiredLabel();
        schemaForm.add(resource);

        provision = new AjaxDropDownChoicePanel<>(
                "provision", getString("provision"), new PropertyModel<Long>(schemaTO, "provision"));

        provision.setChoices(new ArrayList<>(anys.keySet()));
        provision.setChoiceRenderer(new AnyTypeRenderer());
        provision.setOutputMarkupId(true);
        provision.setOutputMarkupPlaceholderTag(true);
        provision.addRequiredLabel();
        provision.setVisible(false);

        schemaForm.add(provision);

        final AjaxTextFieldPanel extAttrName = new AjaxTextFieldPanel(
                "extAttrName", getString("extAttrName"), new PropertyModel<String>(schemaTO, "extAttrName"));
        extAttrName.addRequiredLabel();
        schemaForm.add(extAttrName);

        add(schemaForm);

        resource.getField().add(new AjaxFormComponentUpdatingBehavior(Constants.ON_CHANGE) {

            private static final long serialVersionUID = -1107858522700306810L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                anys.clear();
                if (resource != null & resource.getModelObject() != null) {
                    for (ProvisionTO provisionTO : resourceRestClient.read(resource.getModelObject()).getProvisions()) {
                        anys.put(provisionTO.getKey(), provisionTO.getAnyType());
                    }
                }
                provision.setChoices(new ArrayList<>(anys.keySet()));
                provision.setModelObject(0L);
                provision.setVisible(true);
                target.add(provision);
            }
        });
    }

    @Override
    public void getOnSubmit(final AjaxRequestTarget target,
            final BaseModal<?> modal, final Form<?> form, final PageReference pageReference, final boolean createFlag) {

        try {
            final VirSchemaTO updatedVirSchemaTO = VirSchemaTO.class.cast(form.getModelObject());

            if (createFlag) {
                schemaRestClient.createVirSchema(updatedVirSchemaTO);
            } else {
                schemaRestClient.updateVirSchema(updatedVirSchemaTO);
            }

            if (pageReference.getPage() instanceof AbstractBasePage) {
                ((AbstractBasePage) pageReference.getPage()).setModalResult(true);
            }
            modal.close(target);

        } catch (Exception e) {
            LOG.error("While creating or updating VirSchema", e);
            error(getString(Constants.ERROR) + ": " + e.getMessage());
            modal.getFeedbackPanel().refresh(target);
        }
    }

    private class AnyTypeRenderer extends ChoiceRenderer<Long> {

        private static final long serialVersionUID = 2840364232128308553L;

        AnyTypeRenderer() {
            super();
        }

        @Override
        public Object getDisplayValue(final Long object) {
            return anys.get(object);
        }

        @Override
        public String getIdValue(final Long object, final int index) {
            return String.valueOf(object != null ? object : 0L);
        }
    }
}
