/**
 * *********************************************************************
 * Copyright (c) 2016: Istituto Nazionale di Fisica Nucleare (INFN) -
 * INDIGO-DataCloud
 *
 * See http://www.infn.it and and http://www.consorzio-cometa.it for details on
 * the copyright holders.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **********************************************************************
 */

package it.infn.ct.indigo.futuregateway.constants;

/**
 * Keys used in the module.
 *
 */
public final class FutureGatewayAdminPortletKeys {

    /**
     * Blocks public instance of this class.
     */
    private FutureGatewayAdminPortletKeys() {
    }

    /**
     * Identifier for the portlet.
     */
    public static final String FUTURE_GATEWAY_ADMIN = "FutureGatewayAdmin";

    /**
     * Content type for FG server messages.
     */
    public static final String FUTURE_GATEWAY_CONTENT_TYPE =
            "application/vnd.indigo-datacloud.apiserver+json";

    /**
     * Weight assigned to the portlet in the configuration panel.
     */
    public static final int FUTURE_GATEWAY_WIGHT = 101;

    /**
     * Name of the collection containing the applications.
     */
    public static final String FUTURE_GATEWAY_APPLICATION_COLLECTION =
            "applications";

    /**
     * Name of the collection containing the applications.
     */
    public static final String FUTURE_GATEWAY_INFRASTRUCTURE_COLLECTION =
            "infrastructures";

    /**
     * Name of the attribute providing the id.
     */
    public static final String FUTURE_GATEWAY_ATTRIBUTE_ID =
            "id";

    /**
     * Name of the attribute providing the name.
     */
    public static final String FUTURE_GATEWAY_ATTRIBUTE_NAME =
            "name";

    /**
     * Name of the attribute providing if it is enabled.
     */
    public static final String FUTURE_GATEWAY_ATTRIBUTE_ENABLED =
            "enabled";
}
