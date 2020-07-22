# EventBus

Java library providing a high-speed event bus implementation. 

## Usage

First, create an `EventBus` instance. There are multiple implementations available,
but at the moment `ASMEventBus` is the fastest.

When you have an instance, you can use `register` and `registerLambda`
to register listeners, `unregister` to remove the listeners, and
`post` to post an event.

For more help, refer to the Javadoc.

## Adding dependency
Gradle: 
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.deamsy:eventbus:VERSION'
}
```

Maven: 
```xml
<repositories>
  <repository>
    <id>jitpack</id>
    <name>Jitpack</name>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>com.github.deamsy</groupId>
    <artifactId>eventbus</artifactId>
    <version>VERSION</version>
  </dependency>
</dependencies>
```