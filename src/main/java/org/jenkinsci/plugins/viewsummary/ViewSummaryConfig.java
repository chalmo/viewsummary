package org.jenkinsci.plugins.viewsummary;

import java.util.ArrayList;

public interface ViewSummaryConfig {
    public String getIncludeRegexp();
    public ArrayList<Checkbox> getSelectedViews();
    public boolean isIncludeTests();
    public boolean isIncludeTotal();
    public String getErrorColor();
    public String getWarningColor();
    public String getTableBorderColor();
    public String getTableHeaderColor();
    public String getTableFooterColor();
}
