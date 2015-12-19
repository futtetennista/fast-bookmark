[![Build Status](https://travis-ci.org/stefanodacchille/fast-bookmark.svg)](https://travis-ci.org/stefanodacchille/fast-bookmark)
[![License](https://img.shields.io/badge/license-BSD-lightgrey.svg?style=flat)](https://github.com/stefanodaccchille/fast-bookmark/blob/master/LICENSE)

# fast-bookmark

Simply copy a link to add it to your favourite read later service

# Project goals:
- Getting some experience writing kotlin for prod and test code
- Trying the [MVVM pattern](https://www.objc.io/issues/13-architecture/mvvm/) out
- Trying out how having a Android instead of a POJO prenter impacts testing
- Trying out Dagger 2
- Integrating Travis CI or some other cloud service for testing

# Open questions:
- how do I mock a kotlin class with mockito? is it even possible? Something like
`AppPreferences::class` returns a `kotlin.reflect.KClass` reference and not a `java.lang.Class`
one, needed by mockito. Using `AppPreferences::class.java` returns the java class but the mocking
 does not work.

# Gotchas
- when mocking a method defined in a kotlin class, the parameters must be optionals in order for
the mocking to work, otherwise the tests will fail with an `java.lang.IllegalStateException: any
(SomeClass::class.java) must not be null`
- when mocking a kotlin class it is necessary to create an interface and mock it instead of the
implementation, otherwise the mocks won' work. Is there a better way to do this?

# Pains
- Make the app build on Travis CI:
  - the `lintVital` task always failed with PermGen out of memory. The solution was to add this config line in the Travis yml file: `GRADLE_OPTS="$GRADLE_OPTS -XX:MaxPermSize=256m"`
