---
name: Pattern Catalog
description: A catalog of design patterns that can be used when implementing a new feature.
---
The patterens to consider in this catalog are the following you can use

# Creational Patterns
1. Abstract Factory Pattern
2. Builder Pattern

# Structural Patterns
1. Facade pattern
2. Decorator
3. Adapter

# Behavioral patterns
1. Command Pattern
2. Observer Pattern
3. State Pattern
4. Strategy Pattern

# Abstract Factory Pattern
Abstract Factory is a creational design pattern that lets you produce families of related objects without specifying their concrete classes.
The approch to implmenting the abstract factory is to deifne a Factoty interface with methods for creating each type of object in the family. 
Then, create concrete factory classes that implement the factory interface and create the specific objects. 
**Example:**
```interface GUIFactory is
    method createButton():Button
    method createCheckbox():Checkbox

class WinFactory implements GUIFactory is
    method createButton():Button is
        return new WinButton()
    method createCheckbox():Checkbox is
        return new WinCheckbox()

// Each concrete factory has a corresponding product variant.
class MacFactory implements GUIFactory is
    method createButton():Button is
        return new MacButton()
    method createCheckbox():Checkbox is
        return new MacCheckbox()
```

# Builder Pattern
Builder is a creational design pattern that lets you construct complex objects step by step. 
The patteren allows to produce different types and representations of an object using the same construction code.
This patteren includes the interface Builder that defines the methods to construc the different parts of the product. 
Director class is responsible for managing the construction process and using a builder to create the product.
**Example:**
```
interface Builder is
    method reset()
    method setSeats(...)
    method setEngine(...)
    method setTripComputer(...)
    method setGPS(...)

class CarBuilder implements Builder is
    private field car:Car


    constructor CarBuilder() is
        this.reset()

    method reset() is
        this.car = new Car()

    method setSeats(...) is
        // Set the number of seats in the car.

    method setEngine(...) is
        // Install a given engine.

    method setTripComputer(...) is
        // Install a trip computer.

    method setGPS(...) is
        // Install a global positioning system.

```
# Facade Pattern
Facade is a structural design pattern that provides a simplified interface to a library, a framework, or any other complex set of classes.
The Facade wil hide the complexities of the system and provide an interface to the client from which the client can access the system.
Use this patteren to hide complex behavor and give a nice interface to the client.
**Example:**
```
class VideoConverter is
    method convert(filename, format):File is
        file = new VideoFile(filename)
        sourceCodec = (new CodecFactory).extract(file)
        if (format == "mp4")
            destinationCodec = new MPEG4CompressionCodec()
        else
            destinationCodec = new OggCompressionCodec()
        buffer = BitrateReader.read(filename, sourceCodec)
        result = BitrateReader.convert(buffer, destinationCodec)
        result = (new AudioMixer()).fix(result)
        return new File(result)


class Application is
    method main() is
        convertor = new VideoConverter()
        mp4 = convertor.convert("funny-cats-video.ogg", "mp4")
        mp4.save()
```
# Decorator Pattern
Decorator is a structural design pattern that lets you attach new behaviors to objects by placing these objects inside special wrapper objects that contain the behaviors.


