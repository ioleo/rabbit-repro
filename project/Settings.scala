import sbt.*
import sbt.Keys.*
import Dependencies.v

object Settings {

  private lazy val compilerOptions = Seq(
    "-deprecation",     // emit warning and location for usages of deprecated APIs
    "-feature",         // emit warning and location for usages of features that should be imported explicitly
    "-unchecked",       // enable additional warnings where generated code depends on assumptions
    "-Xfatal-warnings", // fail the compilation if there are any warnings
    /**
     * Scala 3
     */
    "-explain",         // explain errors in more detail
    "-explain-types",   // explain type errors in more detail
    "-indent",          // allow significant indentation
    "-new-syntax",      // require `then` and `do` in control expressions
    "-print-lines",     // show source code line numbers
    "-Ykind-projector", // allow `*` as wildcard to be compatible with kind projector
    "-Xmigration"       // warn about constructs whose behavior may have changed since version
  )

  implicit final class ProjectOps(val project: Project) {

    def minimalSettings: Project =
      project.withScalafix.withBuildInfo.settings(
        organization     := "com.cognism",
        organizationName := "Cognism",
        scalaVersion     := v.scala,
        scalacOptions ++= compilerOptions,
        resolvers ++= Resolver.sonatypeOssRepos("snapshots")
      )

    def withScalafix: Project = {
      import scalafix.sbt.ScalafixPlugin.autoImport.*

      project.settings(
        semanticdbEnabled                                          := true,
        ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % v.scalafixOrganizeImports
      )
    }

    def noScalafix: Project =
      project.disablePlugins(scalafix.sbt.ScalafixPlugin).settings(semanticdbEnabled := false)

    def withBuildInfo: Project = {
      import sbtbuildinfo.BuildInfoPlugin
      import sbtbuildinfo.BuildInfoPlugin.autoImport.*

      project
        .enablePlugins(BuildInfoPlugin)
        .settings(
          buildInfoKeys    := Seq[BuildInfoKey](name, organization, version, scalaVersion, sbtVersion),
          buildInfoPackage := s"${organization.value}.${name.value}"
        )
    }

    def withZioTest: Project =
      project.settings(
        testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework"), // Use zio-test runner
        Test / fork := true // Ensure canceling `run` releases socket, no matter what
      )

    def withIntegrationTest: Project =
      project.configs(IntegrationTest).settings(Defaults.itSettings).settings(IntegrationTest / fork := true)

  }
}
