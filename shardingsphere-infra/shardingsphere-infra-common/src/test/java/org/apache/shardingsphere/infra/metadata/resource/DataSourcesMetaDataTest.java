/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.metadata.resource;

import org.apache.shardingsphere.infra.database.type.DatabaseTypeRegistry;
import org.apache.shardingsphere.test.mock.MockedDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class DataSourcesMetaDataTest {
    
    @Test
    public void assertGetAllInstanceDataSourceNamesForShardingRuleByDifferentDataSource() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2, 1);
        dataSourceMap.put("ds_0", createDataSource("jdbc:mysql://127.0.0.1:3306/db_0"));
        dataSourceMap.put("ds_1", createDataSource("jdbc:mysql://127.0.0.1:3307/db_1"));
        DataSourcesMetaData dataSourcesMetaData = new DataSourcesMetaData(DatabaseTypeRegistry.getActualDatabaseType("MySQL"), dataSourceMap);
        Collection<String> allInstanceDataSourceNames = dataSourcesMetaData.getAllInstanceDataSourceNames();
        assertThat(allInstanceDataSourceNames.size(), is(2));
        assertTrue(allInstanceDataSourceNames.contains("ds_0"));
        assertTrue(allInstanceDataSourceNames.contains("ds_1"));
    }
    
    @Test
    public void assertGetAllInstanceDataSourceNamesForShardingRuleBySameDataSource() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2, 1);
        dataSourceMap.put("ds_0", createDataSource("jdbc:mysql://127.0.0.1:3306/db_0"));
        dataSourceMap.put("ds_1", createDataSource("jdbc:mysql://127.0.0.1:3306/db_1"));
        DataSourcesMetaData dataSourcesMetaData = new DataSourcesMetaData(DatabaseTypeRegistry.getActualDatabaseType("MySQL"), dataSourceMap);
        Collection<String> allInstanceDataSourceNames = dataSourcesMetaData.getAllInstanceDataSourceNames();
        assertThat(allInstanceDataSourceNames.size(), is(1));
        assertTrue(allInstanceDataSourceNames.contains("ds_0") || allInstanceDataSourceNames.contains("ds_1"));
    }
    
    @Test
    public void assertGetActualCatalogForShardingRule() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2, 1);
        dataSourceMap.put("ds_0", createDataSource("jdbc:mysql://127.0.0.1:3306/db_0"));
        dataSourceMap.put("ds_1", createDataSource("jdbc:mysql://127.0.0.1:3306/db_1"));
        DataSourcesMetaData dataSourcesMetaData = new DataSourcesMetaData(DatabaseTypeRegistry.getActualDatabaseType("MySQL"), dataSourceMap);
        assertThat(dataSourcesMetaData.getDataSourceMetaData("ds_0").getCatalog(), is("db_0"));
    }
    
    @Test
    public void assertGetActualSchemaNameForShardingRuleForMysql() {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2, 1);
        dataSourceMap.put("ds_0", createDataSource("jdbc:mysql://127.0.0.1:3306/db_0"));
        dataSourceMap.put("ds_1", createDataSource("jdbc:mysql://127.0.0.1:3306/db_1"));
        DataSourcesMetaData dataSourcesMetaData = new DataSourcesMetaData(DatabaseTypeRegistry.getActualDatabaseType("MySQL"), dataSourceMap);
        assertNull(dataSourcesMetaData.getDataSourceMetaData("ds_0").getSchema());
    }
    
    private MockedDataSource createDataSource(final String url) {
        MockedDataSource result = new MockedDataSource();
        result.setUrl(url);
        result.setUsername("test");
        return result;
    }
}
