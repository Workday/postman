Contributing
============

If you would like to contribute, you can do so by forking the repository and sending a pull request.

Please follow the existing style. You may import the code style into IntilliJ or Android Studio by
copying the [code style file](./workday-oss-code-style.xml) into the following directory, depending
on your operating system.

- Windows: `<your home directory>\.<product name><version number>\config\codestyles`
- Linux: `~/.<product name><version number>/config/codestyles`
- OS X: `~/Library/Preferences/<product name><version number>/codestyles`

Restart IntelliJ or Android Studio. Then go to Settings > Editor > Code Style and select
"workday-oss-code-style".

From the root directory, please run `./gradlew assemble check` before submitting your pull request
to make sure that you didn't break anything.