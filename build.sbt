import Dependencies.*
import Settings.*

addCommandAlias("fmt", "; scalafmt; scalafmtSbt; Test / scalafmt")
addCommandAlias("fmtCheck", "; scalafmtCheck; scalafmtSbtCheck; Test / scalafmtCheck")
addCommandAlias("updateLock", ";unlock;reload;lock;reload")

lazy val root = (project in file(".")).minimalSettings.withZioTest.withIntegrationTest.settings(
  name                      := "repro",
  ThisBuild / version       := "0.1.0-SNAPSHOT",
  Compile / run / mainClass := Some("com.example.repro.Application"),
  assembly / mainClass      := (Compile / run / mainClass).value,
  libraryDependencies       := rootDependencies,
  excludeDependencies ++= excludedDependencies
)
