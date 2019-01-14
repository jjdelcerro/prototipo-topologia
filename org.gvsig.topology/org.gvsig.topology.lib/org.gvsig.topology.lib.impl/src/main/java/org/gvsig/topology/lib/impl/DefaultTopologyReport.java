/**
 * gvSIG. Desktop Geographic Information System.
 *
 * Copyright (C) 2007-2013 gvSIG Association.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * For any additional information, do not hesitate to contact us
 * at info AT gvsig.com, or visit our website www.gvsig.com.
 */
package org.gvsig.topology.lib.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.gvsig.expressionevaluator.Code;
import org.gvsig.expressionevaluator.ExpressionEvaluatorLocator;
import org.gvsig.expressionevaluator.ExpressionEvaluatorManager;
import org.gvsig.expressionevaluator.MutableSymbolTable;
import org.gvsig.expressionevaluator.spi.AbstractSymbolTable;
import org.gvsig.fmap.dal.feature.FeatureReference;
import org.gvsig.fmap.geom.Geometry;
import org.gvsig.tools.task.SimpleTaskStatus;
import org.gvsig.topology.lib.api.TopologyDataSet;
import org.gvsig.topology.lib.api.TopologyPlan;
import org.gvsig.topology.lib.api.TopologyReport;
import static org.gvsig.topology.lib.api.TopologyReport.IS_ERROR;
import static org.gvsig.topology.lib.api.TopologyReport.RULE_ID;
import org.gvsig.topology.lib.api.TopologyReportLine;
import org.gvsig.topology.lib.api.TopologyReportLineSet;
import org.gvsig.topology.lib.api.TopologyRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jjdelcerro
 */
@SuppressWarnings("UseSpecificCatch")
public class DefaultTopologyReport 
        extends AbstractTopologyReportLineSet
        implements TopologyReport 
    {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTopologyReport.class);

    private class DefaultTopologyReportLineSet extends AbstractTopologyReportLineSet {

        public DefaultTopologyReportLineSet() {
            super();
        }

        public void polulate(List<TopologyReportLine> allLines, String filter) {
            this.lines.clear();
            if (filter == null) {
                this.lines.addAll(allLines);
                this.completed = true;
                return;
            }
            SimpleTaskStatus theTaskStatus = plan.getTaskStatus();
            try {
                theTaskStatus.restart();
                theTaskStatus.message("Preparing filter");
                theTaskStatus.setAutoremove(true);
                theTaskStatus.setIndeterminate();

                ExpressionEvaluatorManager manager = ExpressionEvaluatorLocator.getManager();

                TopologyReportLineSymbolTable lineSymbolTable = new TopologyReportLineSymbolTable();
                MutableSymbolTable symbolTable = manager.createSymbolTable();
                symbolTable.addSymbolTable(lineSymbolTable);

                Code code = manager.compile(filter);
                code = manager.optimize(symbolTable, code);

                theTaskStatus.setRangeOfValues(0, allLines.size());
                theTaskStatus.setCurValue(0);
                for (TopologyReportLine line : allLines) {
                    lineSymbolTable.setLine(line);
                    Object value = manager.evaluate(symbolTable, code);
                    if (value instanceof Boolean && ((Boolean) value)) {
                        this.lines.add(line);
                        this.changeListenerHelper.fireEvent();
                    }
                    theTaskStatus.incrementCurrentValue();
                }

            } catch (Exception ex) {
                LOGGER.warn("Problems filtering.", ex);
                theTaskStatus.abort();
            } finally {
                if (theTaskStatus.isRunning()) {
                    theTaskStatus.terminate();
                }
                this.completed = true;
            }
        }


    }

    // TODO: Habria que meter las lineas del report en disco
    private final TopologyPlan plan;

    public DefaultTopologyReport(TopologyPlan plan) {
        this.plan = plan;
    }

    @Override
    public TopologyReportLine addLine(TopologyRule rule, TopologyDataSet dataSet1,
            TopologyDataSet dataSet2, Geometry geometry,
            FeatureReference feature1, FeatureReference feature2,
            boolean exception, String description
    ) {
        TopologyReportLine line = new DefaultTopologyReportLine(
                this, rule, dataSet1, dataSet2, geometry, feature1, feature2,
                exception, description
        );
        this.lines.add(line);
        this.changeListenerHelper.fireEvent();
        return line;
    }

    @Override
    public void removeAllLines() {
        this.lines.clear();
        this.fireChangeEvent();
    }

    @Override
    public TopologyReportLineSet getLineSet(final String filter) {
        final DefaultTopologyReportLineSet set = new DefaultTopologyReportLineSet();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                set.polulate(lines, filter);
            }
        });
        th.start();
        return set;
    }

    @Override
    public List<TopologyReportLine> getLines(String filter) {
        if (filter == null) {
            return Collections.unmodifiableList(this.lines);
        }
        List<TopologyReportLine> ll = new ArrayList<>();
        ExpressionEvaluatorManager manager = ExpressionEvaluatorLocator.getManager();

        TopologyReportLineSymbolTable lineSymbolTable = new TopologyReportLineSymbolTable();
        MutableSymbolTable symbolTable = manager.createSymbolTable();
        symbolTable.addSymbolTable(lineSymbolTable);

        Code code = manager.compile(filter);
        code = manager.optimize(symbolTable, code);

        for (TopologyReportLine line : this.lines) {
            lineSymbolTable.setLine(line);
            Object value = manager.evaluate(symbolTable, code);
            if (value instanceof Boolean && ((Boolean) value)) {
                ll.add(line);
            }
        }
        return Collections.unmodifiableList(ll);
    }
}
