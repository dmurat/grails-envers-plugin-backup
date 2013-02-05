/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.lucasward.grails.plugin

import net.lucasward.grails.plugin.criteria.DoNothingCriteria
import net.lucasward.grails.plugin.criteria.EnversCriteria

import org.hibernate.SessionFactory
import org.hibernate.envers.AuditReader
import org.hibernate.envers.AuditReaderFactory
import org.hibernate.envers.query.AuditQuery

/**
 * @author Lucas Ward
 */
class RevisionsOfEntityQueryMethod {

    SessionFactory sessionFactory
    Class clazz
    EnversCriteria criteria
    PropertyNameAuditOrder auditOrder = new PropertyNameAuditOrder()
    PaginationHandler paginationHandler = new PaginationHandler()
    String dataSourceName
    DatasourceAwareAuditEventListener datasourceAwareAuditEventListener

    RevisionsOfEntityQueryMethod(
        String dataSourceName, DatasourceAwareAuditEventListener datasourceAwareAuditEventListener, SessionFactory sessionFactory,
        Class clazz, EnversCriteria criteria)
    {
        this.dataSourceName = dataSourceName
        this.datasourceAwareAuditEventListener
        this.sessionFactory = sessionFactory
        this.clazz = clazz
        this.criteria = criteria
    }

    RevisionsOfEntityQueryMethod(
        String dataSourceName, DatasourceAwareAuditEventListener datasourceAwareAuditEventListener, SessionFactory sessionFactory, Class clazz)
    {
        this(dataSourceName, datasourceAwareAuditEventListener, sessionFactory, clazz, new DoNothingCriteria())
    }

//    def query(String propertyName, argument, Map parameters) {
//        AuditReader auditReader = datasourceAwareAuditEventListener.createAuditReader(sessionFactory.currentSession, dataSourceName)
//
////        def auditQueryCreator = AuditReaderFactory.get(sessionFactory.currentSession).createQuery()
//        def auditQueryCreator = auditReader.createQuery()
//        AuditQuery query = auditQueryCreator.forRevisionsOfEntity(clazz, false, true)
//        criteria.addCriteria(query, clazz, propertyName, argument)
//        auditOrder.addOrder(query, parameters)
//        paginationHandler.addPagination(query, parameters)
//
//        return query.resultList.collect { EnversPluginSupport.collapseRevision(it) }
//    }

    def query(
        String dataSourceName, DatasourceAwareAuditEventListener datasourceAwareAuditEventListener, String propertyName, argument, Map parameters)
    {
      AuditReader auditReader = datasourceAwareAuditEventListener.createAuditReader(sessionFactory.currentSession, dataSourceName)

      def auditQueryCreator = auditReader.createQuery()
      AuditQuery query = auditQueryCreator.forRevisionsOfEntity(clazz, false, true)
      criteria.addCriteria(query, clazz, propertyName, argument)
      auditOrder.addOrder(query, parameters)
      paginationHandler.addPagination(query, parameters)

      return query.resultList.collect { EnversPluginSupport.collapseRevision(it) }
    }
}
