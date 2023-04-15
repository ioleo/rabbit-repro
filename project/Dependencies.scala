import sbt.*

object Dependencies {

  object v {
    val scala                   = "3.2.0"
    val scalafixOrganizeImports = "0.6.0"

    // framework base
    val zio            = "2.0.+"
    val zioConfig      = "3.0.+"
    val zioInteropCats = "23.0.+"
    val zioJson        = "0.5.0"
    val zioMock        = "1.0.0-RC9"

    // logging
    val zioLogging = "2.1.+"
    val janino     = "3.1.+"
    val coralogix  = "2.0.+"

    // key value store
    val zioSchema = "0.4.10"

    // messaging
    val lepus = "0.3.+"

    // misc
    val zioUlid = "1.1.+"
  }

  lazy val rootDependencies =
    zio ++ zioConfig ++ zioJson ++ logging ++ zioUlid ++ zioSchema ++ rabbitMq

  lazy val zio = Seq(
    "dev.zio" %% "zio"              % v.zio,
    "dev.zio" %% "zio-interop-cats" % v.zioInteropCats,
    "dev.zio" %% "zio-test"         % v.zio     % "test,it",
    "dev.zio" %% "zio-test-sbt"     % v.zio     % "test,it",
    "dev.zio" %% "zio-mock"         % v.zioMock % "test,it"
  )

  lazy val zioConfig = Seq(
    "dev.zio" %% "zio-config"          % v.zioConfig,
    "dev.zio" %% "zio-config-magnolia" % v.zioConfig,
    "dev.zio" %% "zio-config-typesafe" % v.zioConfig
  )

  lazy val zioJson = Seq(
    "dev.zio" %% "zio-json" % v.zioJson
  )

  lazy val logging = Seq(
    "dev.zio"            %% "zio-logging"       % v.zioLogging,
    "dev.zio"            %% "zio-logging-slf4j" % v.zioLogging,
    "org.codehaus.janino" % "janino"            % v.janino,
    "com.coralogix.sdk"   % "coralogix-sdk"     % v.coralogix,
    "com.coralogix.sdk"   % "logback-appender"  % v.coralogix
  )

  lazy val zioUlid = Seq(
    "com.bilal-fazlani" %% "zio-ulid" % v.zioUlid
  )

  lazy val zioSchema = Seq(
    "dev.zio" %% "zio-schema"            % v.zioSchema,
    "dev.zio" %% "zio-schema-derivation" % v.zioSchema,
    "dev.zio" %% "zio-schema-protobuf"   % v.zioSchema
  )

  lazy val rabbitMq                                                      = Seq(
    "dev.hnaderi" %% "lepus-client" % v.lepus
  )
  lazy val excludedDependencies: Seq[sbt.librarymanagement.InclExclRule] = Seq(
    "org.scala-lang.modules" % "scala-collection-compat_2.13"
  )
}
