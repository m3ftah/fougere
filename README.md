# Fougere

Fougere is an Andoid library that provides a user-centric privacy control for data-intensive mobile apps.


# Build the template app

```bash
./gradlew installDebug
```


# Using Fougere library Gradle Plugin
For faster integration with your app, you can use our Gradle project.

In your root project folder you should checkout this project:
```bash
git checkout git@github.com:m3ftah/fougere.git
```

In `YourProject/settings.gradle` you should add:
```gradle
include ':fougere'
project(':fougere').projectDir = new File('../fougere')
```

And in `YourProject/settings.gradle` you should add:
```gradle
compile project(":fougere")
```

# Using Fougere
If you are using `Fougere`, please cite the following research paper:
>Lakhdar  Meftah,  Romain  Rouvoy,  and  Isabelle  Chrisment."FOUGERE:  User-Centric Location Privacy in Mobile Crowdsourcing Apps."In IFIP International Conference on Distributed Applications and Interoperable Systems, pp. 116-132. Springer,Cham, 2019. ([pdf](https://hal.inria.fr/hal-02121311)). **Best Paper Award.**
