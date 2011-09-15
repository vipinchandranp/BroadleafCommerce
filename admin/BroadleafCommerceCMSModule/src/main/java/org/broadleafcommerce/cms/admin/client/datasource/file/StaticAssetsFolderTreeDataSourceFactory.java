/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.cms.admin.client.datasource.file;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import org.broadleafcommerce.cms.admin.client.datasource.CeilingEntities;
import org.broadleafcommerce.cms.admin.client.datasource.EntityImplementations;
import org.broadleafcommerce.openadmin.client.datasource.DataSourceFactory;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.CustomCriteriaTreeGridDataSource;
import org.broadleafcommerce.openadmin.client.datasource.dynamic.module.DataSourceModule;
import org.broadleafcommerce.openadmin.client.dto.*;
import org.broadleafcommerce.openadmin.client.service.AppServices;

/**
 * 
 * @author jfischer
 *
 */
public class StaticAssetsFolderTreeDataSourceFactory implements DataSourceFactory {

	public static final String parentFolderForeignKey = "parentFolder";
	public static CustomCriteriaTreeGridDataSource dataSource = null;

	public void createDataSource(String name, OperationTypes operationTypes, Object[] additionalItems, AsyncCallback<DataSource> cb) {
		if (dataSource == null) {
			operationTypes = new OperationTypes(OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY, OperationType.ENTITY);
			PersistencePerspective persistencePerspective = new PersistencePerspective(operationTypes, new String[] {}, new ForeignKey[]{});
			persistencePerspective.addPersistencePerspectiveItem(PersistencePerspectiveItemType.FOREIGNKEY, new ForeignKey(parentFolderForeignKey, EntityImplementations.STATICASSETFOLDERIMPL, null));
            DataSourceModule[] modules = new DataSourceModule[]{
				new StaticAssetsFolderClientEntityModule(CeilingEntities.STATICFOLDERASSETS, persistencePerspective, AppServices.DYNAMIC_ENTITY)
			};
            persistencePerspective.setPopulateToOneFields(true);
            persistencePerspective.setExcludeFields(new String[]{
                "site",
                "sandbox"
            });
			dataSource = new CustomCriteriaTreeGridDataSource(name, persistencePerspective, AppServices.DYNAMIC_ENTITY, modules, null, null, true, true, true, true, true);
			dataSource.setCustomCriteria(new String[]{"assetFolderUi"});
            dataSource.buildFields(null, false, cb);
		} else {
			if (cb != null) {
				cb.onSuccess(dataSource);
			}
		}
	}

}
