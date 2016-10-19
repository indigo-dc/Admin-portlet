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
package it.infn.ct.indigo.futuregateway.server;

import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.expando.kernel.exception.DuplicateColumnNameException;
import com.liferay.expando.kernel.exception.DuplicateTableNameException;
import com.liferay.expando.kernel.model.ExpandoColumnConstants;
import com.liferay.expando.kernel.model.ExpandoTable;
import com.liferay.expando.kernel.service.ExpandoColumnLocalService;
import com.liferay.expando.kernel.service.ExpandoTableLocalService;
import com.liferay.expando.kernel.service.ExpandoValueLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.service.CompanyLocalService;

/**
 * 
 * @author Marco Fargetta
 */
@Component(immediate = true, service = FGServerManeger.class)
public class FGServerManeger {

	public void setFGUrl(long companyId, String url) throws PortalException {
		_expandoValueLocalService.addValue(
                companyId, FGServerManeger.class.getName(), "FG", "fgUrl",
                0, url);
	}

	public String getFGUrl(long companyId) throws PortalException {
		return _expandoValueLocalService.getData(companyId,
				FGServerManeger.class.getName(), "FG", "fgUrl", 0, "");
	}

	@Activate
	protected void activate() {
		List<Company> companys = _companyLocalService.getCompanies();

		for (Company company : companys) {
			try {
				setupExpando(company.getCompanyId());
			} catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to setup FutureGateway service for company " + company.getCompanyId() + ": "
							+ e.getMessage());
				}
			}
		}
	}
	
	@Reference(unbind = "-")
	protected void setCompanyLocalService(CompanyLocalService companyLocalService) {

		_companyLocalService = companyLocalService;
	}

	@Reference(unbind = "-")
	protected void setExpandoColumnLocalService(ExpandoColumnLocalService expandoColumnLocalService) {

		_expandoColumnLocalService = expandoColumnLocalService;
	}

	@Reference(unbind = "-")
	protected void setExpandoTableLocalService(ExpandoTableLocalService expandoTableLocalService) {

		_expandoTableLocalService = expandoTableLocalService;
	}

	@Reference(unbind = "-")
    protected void setExpandoValueLocalService(
            ExpandoValueLocalService expandoValueLocalService) {

            _expandoValueLocalService = expandoValueLocalService;
    }

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	protected void setupExpando(long companyId) throws Exception {
		ExpandoTable table = null;

		try {
			table = _expandoTableLocalService.addTable(companyId, FGServerManeger.class.getName(), "FG");
		} catch (DuplicateTableNameException dtne) {
			table = _expandoTableLocalService.getTable(companyId, FGServerManeger.class.getName(), "FG");
		}

		try {
			_expandoColumnLocalService.addColumn(table.getTableId(), "fgUrl", ExpandoColumnConstants.STRING);
		} catch (DuplicateColumnNameException dcne) {
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(FGServerManeger.class);

	private CompanyLocalService _companyLocalService;
	private ExpandoColumnLocalService _expandoColumnLocalService;
	private ExpandoTableLocalService _expandoTableLocalService;
    private ExpandoValueLocalService _expandoValueLocalService;
}
