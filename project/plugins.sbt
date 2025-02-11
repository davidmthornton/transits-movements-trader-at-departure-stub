resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"

resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(Resolver.ivyStylePatterns)

resolvers += Resolver.typesafeRepo("releases")

addSbtPlugin("uk.gov.hmrc"               % "sbt-auto-build"       % "3.4.0")
addSbtPlugin("uk.gov.hmrc"               % "sbt-distributables"   % "2.1.0")
addSbtPlugin("com.typesafe.play"         % "sbt-plugin"           % "2.8.8")
addSbtPlugin("org.scoverage"             % "sbt-scoverage"        % "1.8.2")
addSbtPlugin("com.lucidchart"            % "sbt-scalafmt"         % "1.16")
addSbtPlugin("ch.epfl.scala"             % "sbt-scalafix"         % "0.9.29")
addSbtPlugin("com.timushev.sbt"          % "sbt-updates"          % "0.6.0")
addSbtPlugin("net.virtual-void"          % "sbt-dependency-graph" % "0.10.0-RC1")
addSbtPlugin("io.github.davidgregory084" % "sbt-tpolecat"         % "0.1.20")
