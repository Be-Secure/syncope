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
package org.apache.syncope.services.proxy;

import java.util.Arrays;
import java.util.List;
import org.apache.syncope.client.mod.StatusMod;
import org.apache.syncope.client.mod.UserMod;
import org.apache.syncope.client.search.NodeCond;
import org.apache.syncope.client.to.UserTO;
import org.apache.syncope.client.to.WorkflowFormTO;
import org.apache.syncope.services.UserService;
import org.springframework.web.client.RestTemplate;

public class UserServiceProxy extends SpringServiceProxy implements UserService {

    public UserServiceProxy(final String baseUrl, final RestTemplate restTemplate) {
        super(baseUrl, restTemplate);
    }

    @Override
    public Boolean verifyPassword(final String username, final String password) {
        return getRestTemplate().getForObject(
                baseUrl + "user/verifyPassword/{username}.json?password={password}", Boolean.class,
                username, password);
    }

    @Override
    public int count() {
        return getRestTemplate().getForObject(baseUrl + "user/count.json", Integer.class);
    }

    @Override
    public List<UserTO> list() {
        return Arrays.asList(getRestTemplate().getForObject(baseUrl + "user/list.json", UserTO[].class));
    }

    @Override
    public List<UserTO> list(final int page, final int size) {
        return Arrays.asList(getRestTemplate().getForObject(baseUrl + "user/list/{page}/{size}.json",
                UserTO[].class, page, size));
    }

    @Override
    public UserTO read(final Long userId) {
        return getRestTemplate().getForObject(baseUrl + "user/read/{userId}.json", UserTO.class, userId);
    }

    @Override
    public UserTO read(final String username) {
        return getRestTemplate().getForObject(baseUrl + "user/readByUsername/{username}.json", UserTO.class,
                username);
    }

    @Override
    public UserTO create(final UserTO userTO) {
        return getRestTemplate().postForObject(baseUrl + "user/create", userTO, UserTO.class);
    }

    @Override
    public UserTO update(final Long userId, final UserMod userMod) {
        return getRestTemplate().postForObject(baseUrl + "user/update", userMod, UserTO.class);
    }

    @Override
    public UserTO delete(final Long userId) {
        return getRestTemplate().getForObject(baseUrl + "user/delete/{userId}", UserTO.class, userId);
    }

    @Override
    public UserTO executeWorkflow(final String taskId, final UserTO userTO) {
        return null;
    }

    @Override
    public List<WorkflowFormTO> getForms() {
        return Arrays.asList(getRestTemplate().getForObject(baseUrl + "user/workflow/form/list",
                WorkflowFormTO[].class));
    }

    @Override
    public WorkflowFormTO getFormForUser(final Long userId) {
        return getRestTemplate().getForObject(baseUrl + "user/workflow/form/{userId}", WorkflowFormTO.class,
                userId);
    }

    @Override
    public WorkflowFormTO claimForm(final String taskId) {
        return getRestTemplate().getForObject(baseUrl + "user/workflow/form/claim/{taskId}",
                WorkflowFormTO.class, taskId);
    }

    @Override
    public UserTO submitForm(final WorkflowFormTO form) {
        return getRestTemplate().postForObject(baseUrl + "user/workflow/form/submit", form, UserTO.class);
    }

    @Override
    public UserTO activate(final long userId, final String token) {
        return getRestTemplate().getForObject(baseUrl + "user/activate/{userId}?token=" + token, UserTO.class,
                userId);
    }

    @Override
    public UserTO activateByUsername(final String username, final String token) {
        return getRestTemplate().getForObject(baseUrl + "user/activateByUsername/{username}.json?token=" + token,
                UserTO.class, username);
    }

    @Override
    public UserTO suspend(final long userId) {
        return getRestTemplate().getForObject(baseUrl + "user/suspend/{userId}", UserTO.class, userId);
    }

    @Override
    public UserTO reactivate(final long userId) {
        return getRestTemplate().getForObject(baseUrl + "user/reactivate/{userId}", UserTO.class, userId);
    }

    @Override
    public UserTO reactivate(long userId, String query) {
        return getRestTemplate().getForObject(baseUrl + "user/reactivate/" + userId + query, UserTO.class);
    }

    @Override
    public UserTO suspendByUsername(final String username) {
        return getRestTemplate().getForObject(baseUrl + "user/suspendByUsername/{username}.json", UserTO.class,
                username);
    }

    @Override
    public UserTO reactivateByUsername(final String username) {
        return getRestTemplate().getForObject(baseUrl + "user/reactivateByUsername/{username}.json",
                UserTO.class, username);
    }

    @Override
    public UserTO suspend(final long userId, final String query) {
        return getRestTemplate().getForObject(baseUrl + "user/suspend/" + userId + query, UserTO.class);
    }

    @Override
    public UserTO readSelf() {
        return getRestTemplate().getForObject(baseUrl + "user/read/self", UserTO.class);
    }

    @Override
    public List<UserTO> search(final NodeCond searchCondition) {
        return Arrays.asList(getRestTemplate().postForObject(baseUrl + "user/search", searchCondition,
                UserTO[].class));
    }

    @Override
    public List<UserTO> search(final NodeCond searchCondition, final int page, final int size) {
        return Arrays.asList(getRestTemplate().postForObject(baseUrl + "user/search/{page}/{size}",
                searchCondition, UserTO[].class, page, size));
    }

    @Override
    public int searchCount(final NodeCond searchCondition) {
        return getRestTemplate()
                .postForObject(baseUrl + "user/search/count.json", searchCondition, Integer.class);
    }

    @Override
    public UserTO setStatus(final Long userId, final StatusMod statusUpdate) {
        return null; // Not used in old REST API
    }
}
