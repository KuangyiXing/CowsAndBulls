name := "CowsAndBulls"

version := "0.1"

scalaVersion := "2.12.7"

val specs2Version = "4.3.4"

libraryDependencies ++= Seq(
"org.specs2"                      %% "specs2-core"                        % specs2Version    % "test",
"org.specs2"                      %% "specs2-matcher-extra"               % specs2Version    % "test",
"org.specs2"                      %% "specs2-scalacheck"                  % specs2Version    % "test",
"org.typelevel"                   %% "cats-effect"                        % "1.2.0"
)
