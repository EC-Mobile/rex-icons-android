# ReX icons library for Android and Compose

This library contains [ReX icons](https://rex.rakuten.design/en/design/the-basics/icons/) for easier integration of ReX icons.

Some icons have two styles - filled and outlined.

There are two arifacts:

1. `design.rakuten.rex.icons:icons`

   This artifact contains XML (vector drawable) versions of the icons and can be used with Android View system.

   Filled and outlined versions are distinguished by the suffix (e.g. `copy_filled` and `copy_outlined`).
   
3. `design.rakuten.rex.icons:icons-compose`

   This artifact contains `ImageVector` versions of the icons and can be used with Compose Multiplatform. Currently supports Android/iOS/desktop.

   Filled and outlined versions are distinguished by the namespace (e.g. `RexIcons.Filled.Copy` and `RexIcons.Outlined.Copy`).

The artifacts are published using [GitHub Packages](https://docs.github.com/en/packages).

## Getting started

To access and use the artifacts, you must add the GitHub Packages repository to your project's Gradle config.

```Kotlin
repositories {
    maven("https://maven.pkg.github.com/EC-Mobile/rex-icons-android") {
        credentials {
            username = "github_username"
            password = "github_access_token"
        }
    }
}

dependencies {
    // XML version
    implementation("design.rakuten.rex.icons:icons:${latest_version}")
    // Compose version
    implementation("design.rakuten.rex.icons:icons-compose:${latest_version}")
}
```

You will need an access token with `read:packages` scope to be able to access a GitHub package.

See [Authenticating to GitHub Packages](https://docs.github.com/en/packages/learn-github-packages/introduction-to-github-packages#authenticating-to-github-packages) for more details.

## Usage

XML version:
```XML
<ImageView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/cross_use" />
```

Compose version:
```Kotlin
Icon(
    imageVector = RexIcons.Default.CrossUse,
    contentDescription = contentDescription
)
```

## Additional info for maintainers

- The Compose icon generator is taken from [AndroidX](https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/material/material/icons/).
- Run `./gradlew assemble` to build the library, and `./gradlew publishAllPublicationsToTempRepository` to publish the artifacts to build/repo directory for local testing.
- To release a new version, update the `VERSION_NAME` property in [gradle.properties](gradle.properties), create a new tag (e.g. `v2.0`), and the artifacts will be published automatically.

## License

The icons in [svg](svg) directory and the artifacts are distributed under [proprietary license](LICENSE-ICONS).

Other code is licensed under [the Apache License, Version 2.0](LICENSE-CODE).
