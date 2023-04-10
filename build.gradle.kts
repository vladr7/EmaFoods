plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.google.hilt).apply(false)
}
allprojects {

}
apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")
