package org.jenkinsci.plugins.viewsummary;

import hudson.maven.MavenModule;
import hudson.maven.MavenModuleSet;
import hudson.maven.reporters.SurefireAggregatedReport;
import hudson.model.AbstractProject;
import hudson.model.HealthReport;
import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TopLevelItem;
import hudson.model.View;
import hudson.model.ViewGroup;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.TestResultProjectAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author christer.moren@leanon.se
 */
public class ViewSummaryUtil {

    private final static Result WORST_RESULT = Result.ABORTED;
    private final static Logger LOG = Logger.getLogger(ViewSummaryUtil.class.getName());

    public static Set<String> getActiveViews(Collection<Checkbox> selectedViews) {
        Set<String> result = new HashSet<String>();
        if (selectedViews != null) {
            for (Checkbox c : selectedViews) {
                if (c.isSelected()) {
                    result.add(c.getName());
                }
            }
        }
        return result;
    }

    public static ViewResultSummary getViewResultSummary(ViewGroup owner, String includeRegexp, ArrayList<Checkbox> selectedViews) {
        Collection<View> views = getTabs(owner, includeRegexp, selectedViews);
        ViewResultSummary result = new ViewResultSummary();
        for (View v : views) {
            ViewResult vr = new ViewResult();
            vr.setView(v);
            calculateViewResult(vr);
            result.addViewResult(vr);
        }
        return result;
    }

    private static Collection<View> getTabs(ViewGroup owner, String includeRegexp, Collection<Checkbox> selectedViews) {
        Set<String> activeSelectedViews = new HashSet<String>();
        if (selectedViews != null) {
            for (Checkbox c : selectedViews) {
                if (c.isSelected()) {
                    activeSelectedViews.add(c.getName());
                }
            }
        }

        if (includeRegexp == null || "".equals(includeRegexp.trim())) {
            includeRegexp = null;
        }

        Collection<View> result = new ArrayList<View>();
        for (View v : owner.getViews()) {
            if (activeSelectedViews.contains(v.getDisplayName())) {
                result.add(v);
            } else if (includeRegexp != null && v.getDisplayName().matches(includeRegexp)) {
                result.add(v);
            }
        }
        return result;
    }

    public static ArrayList<Checkbox> getSelectedViews(ArrayList<Checkbox> selectedViews, Collection<View> allTabs) {
        ArrayList<Checkbox> result = new ArrayList<Checkbox>();

        for (View v : allTabs) {
            boolean found = false;
            if (selectedViews != null) {
                for (Checkbox c : selectedViews) {
                    if (c.getName().equals(v.getViewName())) {
                        found = true;
                        result.add(c);
                        break;
                    }
                }
            }
            if (!found) {
                result.add(new Checkbox(v.getViewName(), false));
            }
        }
        return result;
    }

    public static Collection<View> getAllTabs(ViewGroup owner) {
        return owner.getViews();
    }

    private static void calculateViewResult(ViewResult vr) {
        Result worstResult = Result.SUCCESS;
        HealthReport worstHealthReport = null;
        // Job info
        int noJobs = 0, noFails = 0, noUnstable = 0, noSuccess = 0, noAborts = 0, notBuilt = 0, noDisabled = 0;
        // Test info
        int noTests = 0, noTestsFailed = 0, noTestsSuccess = 0, noTestsSkipped = 0;
        for (TopLevelItem tli : vr.getView().getItems()) {
            if (tli instanceof Job && !(tli instanceof AbstractProject && ((AbstractProject) tli).isDisabled())) {
                // Job statistics
                Job job = (Job) tli;
                noJobs++;
                final Run r = job.getLastBuild();
                if (r != null && r.getResult().isWorseThan(worstResult)) {
                    worstResult = r.getResult();
                }
                if (r == null) {
                    notBuilt++;
                } else if (r.getResult() == Result.FAILURE) {
                    noFails++;
                } else if (r.getResult() == Result.UNSTABLE) {
                    noUnstable++;
                } else if (r.getResult() == Result.SUCCESS) {
                    noSuccess++;
                } else if (r.getResult() == Result.ABORTED) {
                    noAborts++;
                }

                if (job.getBuildHealth() != null) {
                    if (worstHealthReport == null) {
                        worstHealthReport = job.getBuildHealth();
                    } else {
                        worstHealthReport = HealthReport.min(worstHealthReport, job.getBuildHealth());
                    }
                }

                // Tests
                TestResultProjectAction testResults = job.getAction(TestResultProjectAction.class);
                job.getBuildHealth().getScore();
                if (testResults != null) {
                    AbstractTestResultAction tra = testResults.getLastTestResultAction();
                    noTestsFailed += tra.getFailCount();
                    noTestsSkipped += tra.getSkipCount();
                    noTests += tra.getTotalCount();
                } else {
                    SurefireAggregatedReport surefireTestResults = job.getAction(SurefireAggregatedReport.class);
                    if (surefireTestResults != null) {
                        noTestsFailed += surefireTestResults.getFailCount();
                        noTestsSkipped += surefireTestResults.getSkipCount();
                        noTests += surefireTestResults.getTotalCount();
                    }
                }
            }
            if (tli instanceof Job && (tli instanceof AbstractProject && ((AbstractProject) tli).isDisabled())) {
                noJobs++;
                noDisabled++;
            }
        }

        // Jobs
        vr.setWorstResult(worstResult);

        vr.setWorstHealthReport(worstHealthReport);

        vr.setNoJobs(noJobs);

        vr.setNoFails(noFails);

        vr.setNoUnstable(noUnstable);

        vr.setNoSuccess(noSuccess);

        vr.setNoAborts(noAborts);

        vr.setNotBuilt(notBuilt);
        // Tests
        noTestsSuccess = noTests - noTestsFailed - noTestsSkipped;

        vr.setNoTests(noTests);

        vr.setNoTestsSuccess(noTestsSuccess);

        vr.setNoTestsFailed(noTestsFailed);

        vr.setNoTestsSkipped(noTestsSkipped);
    }
}
