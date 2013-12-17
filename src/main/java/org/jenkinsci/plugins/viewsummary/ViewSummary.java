package org.jenkinsci.plugins.viewsummary;

import hudson.model.View;
import java.util.Collection;

public interface ViewSummary {
    public ViewResultSummary getViewResultSummary();
    public Collection<View> getAllTabs();
}
