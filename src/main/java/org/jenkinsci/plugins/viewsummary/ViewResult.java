package org.jenkinsci.plugins.viewsummary;

import hudson.model.HealthReport;
import hudson.model.Result;
import hudson.model.View;

/**
 *
 * @author christer.moren@leanon.se
 */
public class ViewResult {
    private View view;
    private Result worstResult;
    private HealthReport worstHealthReport;
    protected int noJobs;
    protected int noFails;
    protected int noUnstable;
    protected int noSuccess;
    protected int noAborts;
    protected int notBuilt;
    protected int noDisabled;

    protected int noTests;
    protected int noTestsFailed;
    protected int noTestsSuccess;
    protected int noTestsSkipped;

    public Result getWorstResult() {
        return worstResult;
    }

    public void setWorstResult(Result worstResult) {
        this.worstResult = worstResult;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getNoDisabled() {
        return noDisabled;
    }

    public void setNoDisabled(int noDisabled) {
        this.noDisabled = noDisabled;
    }

    public int getNoJobs() {
        return noJobs;
    }

    public void setNoJobs(int noJobs) {
        this.noJobs = noJobs;
    }

    public int getNoFails() {
        return noFails;
    }

    public void setNoFails(int noFails) {
        this.noFails = noFails;
    }

    public int getNoUnstable() {
        return noUnstable;
    }

    public void setNoUnstable(int noUnstable) {
        this.noUnstable = noUnstable;
    }

    public int getNoSuccess() {
        return noSuccess;
    }

    public void setNoSuccess(int noSuccess) {
        this.noSuccess = noSuccess;
    }

    public int getNoAborts() {
        return noAborts;
    }

    public void setNoAborts(int noAborts) {
        this.noAborts = noAborts;
    }

    public int getNotBuilt() {
        return notBuilt;
    }

    public void setNotBuilt(int notBuilt) {
        this.notBuilt = notBuilt;
    }

    public int getNoTests() {
        return noTests;
    }

    public void setNoTests(int noTests) {
        this.noTests = noTests;
    }

    public int getNoTestsFailed() {
        return noTestsFailed;
    }

    public void setNoTestsFailed(int noTestsFailed) {
        this.noTestsFailed = noTestsFailed;
    }

    public int getNoTestsSuccess() {
        return noTestsSuccess;
    }

    public void setNoTestsSuccess(int noTestsSuccess) {
        this.noTestsSuccess = noTestsSuccess;
    }


    public int getNoTestsSkipped() {
        return noTestsSkipped;
    }

    public void setNoTestsSkipped(int noTestsSkipped) {
        this.noTestsSkipped = noTestsSkipped;
    }

    public HealthReport getWorstHealthReport() {
        return worstHealthReport;
    }

    public void setWorstHealthReport(HealthReport worstHealthReport) {
        this.worstHealthReport = worstHealthReport;
    }
}
