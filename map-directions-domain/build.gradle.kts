plugins {
    `java-library`
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}