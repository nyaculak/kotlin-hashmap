### Kotlin Hashmap - https://github.com/nyaculak/kotlin-hashmap
A basic implementation of a hashmap in Kotlin
The hashmap tries to fill the array associated with the keys hashcode
If there is a collision, the map searches linearly for the next open slot
If there is no open slot in the underlying array, insertion fails

## Building
Run gradle
./gradlew

## Running Tests
Run gradle task test
./gradlew test
