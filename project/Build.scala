import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "shadence"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "mysql" % "mysql-connector-java" % "5.1.18",
      "org.imgscalr" % "imgscalr-lib" % "4.2",
      "javax.mail" % "mail" % "1.4.1",
      "commons-codec" % "commons-codec" % "1.7"
    )
	
    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
