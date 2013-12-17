package org.jenkinsci.plugins.viewsummary;

import hudson.Extension;
import hudson.model.Descriptor;

import hudson.model.View;
import hudson.plugins.view.dashboard.DashboardPortlet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.DataBoundConstructor;

public class ViewSummaryPortlet extends DashboardPortlet implements ViewSummary, ViewSummaryConfig {

    private final static Logger LOG = Logger.getLogger(ViewSummaryPortlet.class.getName());
    private String includeRegexp;
    private ArrayList<Checkbox> selectedViews;
    private boolean includeTests;
    private boolean includeTotal;
    private String errorColor;
    private String warningColor;
    private String tableBorderColor;
    private String tableHeaderColor;
    private String tableFooterColor;

    @DataBoundConstructor
    public ViewSummaryPortlet(String name, String includeRegexp, ArrayList<Checkbox> selectedViews, boolean includeTests, boolean includeTotal,
                              String errorColor, String warningColor, String tableBorderColor, String tableHeaderColor, String tableFooterColor) {
        super(name);
        this.includeRegexp = includeRegexp;
        this.selectedViews = selectedViews == null ? getSelectedViews() : selectedViews;
        this.includeTests = includeTests;
        this.includeTotal = includeTotal;
        this.errorColor = errorColor;
        this.warningColor = warningColor;
        this.tableBorderColor = tableBorderColor;
        this.tableHeaderColor = tableHeaderColor;
        this.tableFooterColor = tableFooterColor;
    }

    // Configuration parameters
    public String getIncludeRegexp() {
        return includeRegexp;
    }

    public boolean isIncludeTests() {
        return includeTests;
    }

    public boolean isIncludeTotal() {
        return includeTotal;
    }

    public ArrayList<Checkbox> getSelectedViews() {
        selectedViews = ViewSummaryUtil.getSelectedViews(selectedViews, getAllTabs());
        return selectedViews;
    }

    public String getErrorColor() {
        return errorColor;
    }

    public String getWarningColor() {
        return warningColor;
    }

    public String getTableBorderColor() {
        return tableBorderColor;
    }

    public String getTableHeaderColor() {
        return tableHeaderColor;
    }

    public String getTableFooterColor() {
        return tableFooterColor;
    }

    // Data retrieval
    public ViewResultSummary getViewResultSummary() {
        return ViewSummaryUtil.getViewResultSummary(getDashboard().getOwner(), includeRegexp, selectedViews);
    }

    public Collection<View> getAllTabs() {
        return ViewSummaryUtil.getAllTabs(getDashboard().getOwner());
    }

    // The rest
    @Extension
    public static class DescriptorImpl extends Descriptor<DashboardPortlet> {
/*
        public final static String defaultErrorColor = "#FF0000";
        public final static String defaultWarningColor = "#FFFF00";
        public final static String defaultTableBorderColor = "#BBBBBB";
        public final static String defaultTableHeaderColor = "#F0F0F0";
        public final static String defaultTableFooterColor = "#F0F0F0";

        public static String getDefaultErrorColor() {
            return defaultErrorColor;
        }

        public static String getDefaultWarningColor() {
            return defaultWarningColor;
        }

        public static String getDefaultTableBorderColor() {
            return defaultTableBorderColor;
        }

        public static String getDefaultTableHeaderColor() {
            return defaultTableHeaderColor;
        }

        public static String getDefaultTableFooterColor() {
            return defaultTableFooterColor;
        }
        */

        public ArrayList<Checkbox> getDefaultViews() {

            return ViewSummaryUtil.getSelectedViews(null, ViewSummaryUtil.getAllTabs(Jenkins.getInstance().getPrimaryView().getOwner()));
        }

        @Override
        public String getDisplayName() {
            return "View Summary";
        }
    }
}
