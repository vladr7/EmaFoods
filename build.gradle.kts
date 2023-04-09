plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)
}
allprojects {

}
apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")
