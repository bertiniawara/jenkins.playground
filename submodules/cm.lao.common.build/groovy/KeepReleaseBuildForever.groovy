def isReleaseBuildFile = manager.build.workspace.child("true.isReleaseBuild")

if (isReleaseBuildFile.exists()) {
	manager.build.keepLog();
	def releaseVersion = manager.envVars["RELEASE_BUILD"];
	manager.build.setDescription(isReleaseBuildFile.readToString());
} else {
	def parameters = manager.build.getActions(hudson.model.ParametersAction).get(0);
	def branchName = parameters.getParameter("branch").getValue();
	manager.build.setDescription("branch: '" + branchName.toString() + "'");
}

if (manager.build.result.isWorseThan(hudson.model.Result.SUCCESS)) {
	manager.build.keepLog();
}

previousBuild = manager.build.previousBuild;
while (previousBuild != null) {
	if (previousBuild.result == null) {
		continue;
	}
	if (!previousBuild.result.isWorseThan(manager.build.result)) {
		break;
	}
	
	previousBuild.keepLog(false);
	previousBuild = previousBuild.previousBuild;
}
