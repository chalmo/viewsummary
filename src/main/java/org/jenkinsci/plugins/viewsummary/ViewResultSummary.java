package org.jenkinsci.plugins.viewsummary;

import java.util.ArrayList;
import java.util.List;

public class ViewResultSummary extends ViewResult {

    private List<ViewResult> viewResults = new ArrayList<ViewResult>();

    public void addViewResult(ViewResult viewResult) {
        viewResults.add(viewResult);

        noJobs += viewResult.getNoJobs();
        noFails += viewResult.getNoFails();
        noUnstable += viewResult.getNoUnstable();
        noSuccess += viewResult.getNoSuccess();
        noAborts += viewResult.getNoAborts();
        notBuilt += viewResult.getNotBuilt();
        noDisabled += viewResult.getNoDisabled();

        noTests += viewResult.getNoTests();
        noTestsFailed += viewResult.getNoTestsFailed();
        noTestsSuccess += viewResult.getNoTestsSuccess();
        noTestsSkipped += viewResult.getNoTestsSkipped();
    }

    public List<ViewResult> getViewResults() {
        return viewResults;
    }
}
