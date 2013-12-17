package org.jenkinsci.plugins.viewsummary;

import hudson.model.View;
import java.util.Collection;

/**
 *
 * @author christer.moren@leanon.se
 */
public interface ViewSummary {
    public ViewResultSummary getViewResultSummary();
    public Collection<View> getAllTabs();
}
