package org.jenkinsci.plugins.viewsummary;

import hudson.DescriptorExtensionList;
import hudson.Extension;
import hudson.Util;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.model.Hudson;
import hudson.model.ListView;
import hudson.model.Saveable;
import hudson.model.View;
import hudson.model.ViewDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 *
 * @author christer.moren@leanon.se
 */
public class ViewSummaryDashboard extends ListView implements ViewSummary, ViewSummaryConfig {

    private final static Logger LOG = Logger.getLogger(ViewSummaryDashboard.class.getName());
    private String includeRegexp;
    private ArrayList<Checkbox> selectedViews;
    private boolean includeTests = true;
    private boolean includeTotal = true;
    private String errorColor;
    private String warningColor;
    private String tableBorderColor;
    private String tableHeaderColor;
    private String tableFooterColor;
    /*
    private String errorColor = ViewSummaryPortlet.DescriptorImpl.defaultErrorColor;
    private String warningColor = ViewSummaryPortlet.DescriptorImpl.defaultWarningColor;
    private String tableBorderColor = ViewSummaryPortlet.DescriptorImpl.defaultTableBorderColor;
    private String tableHeaderColor = ViewSummaryPortlet.DescriptorImpl.defaultTableHeaderColor;
    private String tableFooterColor = ViewSummaryPortlet.DescriptorImpl.defaultTableFooterColor;
    */

    @DataBoundConstructor
    public ViewSummaryDashboard(String name) {
        super(name);
    }

    // Configuration parameters

    public String getIncludeRegexp() {
        return includeRegexp;
    }

    public ArrayList<Checkbox> getSelectedViews() {
        selectedViews = ViewSummaryUtil.getSelectedViews(selectedViews, getAllTabs());
        return selectedViews;
    }

    public boolean isIncludeTests() {
        return includeTests;
    }

    public boolean isIncludeTotal() {
        return includeTotal;
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
        return ViewSummaryUtil.getViewResultSummary(getOwner(), includeRegexp, selectedViews);
    }

    public Collection<View> getAllTabs() {
        return ViewSummaryUtil.getAllTabs(getOwner());
    }

    // The rest

    @Override
    protected synchronized void submit(StaplerRequest req) throws IOException, ServletException, FormException {
        super.submit(req);

        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "View summary dashboard: " + ex.getMessage());
        }

        includeRegexp = Util.nullify(req.getParameter("includeRegexp"));
        String sIncludeTests = Util.nullify(req.getParameter("includeTests"));
        includeTests = "on".equals(sIncludeTests);
        String sIncludeTotal = Util.nullify(req.getParameter("includeTotal"));
        includeTotal = "on".equals(sIncludeTotal);

        errorColor = Util.nullify(req.getParameter("errorColor"));
        warningColor = Util.nullify(req.getParameter("warningColor"));
        tableBorderColor = Util.nullify(req.getParameter("tableBorderColor"));
        tableHeaderColor = Util.nullify(req.getParameter("tableHeaderColor"));
        tableFooterColor = Util.nullify(req.getParameter("tableFooterColor"));

        JSONObject json = req.getSubmittedForm();

        JSONArray jsonSelectedViews = json.getJSONArray("selectedViews");
        selectedViews = new ArrayList<Checkbox>();
        for (int i = 0; i < jsonSelectedViews.size(); i++) {
            JSONObject jo = jsonSelectedViews.getJSONObject(i);
            String cName = (String) jo.get("name");
            Boolean cSelected = (Boolean) jo.get("selected");
            Checkbox c = new Checkbox(cName, cSelected);
            selectedViews.add(c);
        }
    }

    public static DescriptorExtensionList<Checkbox, Descriptor<Checkbox>> descriptorList() {
      return Hudson.getInstance().getDescriptorList(Checkbox.class);
    }

    @Extension
    public static class DescriptorImpl extends ViewDescriptor {

        @Override
        public String getDisplayName() {
            return "View Summary Dashboard";
        }
    }
}
