/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:7.3.0'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("gradlebuild.root-build")

    id("gradlebuild.teamcity-import-test-data")  // CI: Import Test tasks' JUnit XML if they're UP-TO-DATE or FROM-CACHE
    id("gradlebuild.lifecycle")                  // CI: Add lifecycle tasks to for the CI pipeline (currently needs to be applied early as it might modify global properties)
    id("gradlebuild.generate-subprojects-info")  // CI: Generate subprojects information for the CI testing pipeline fan out
    id("gradlebuild.cleanup")                    // CI: Advanced cleanup after the build (like stopping daemons started by tests)

    id("gradlebuild.update-versions")            // Local development: Convenience tasks to update versions in this build: 'released-versions.json', 'agp-versions.properties', ...
    id("gradlebuild.wrapper")                    // Local development: Convenience tasks to update the wrapper (like 'nightlyWrapper')
}

description = "Adaptable, fast automation for all"
