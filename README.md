# Loomo Remote: Robot app

This project is a showcase of [SegwayRobotics Loomo](https://loomo.com) API.

It consists of this project and the [corresponding app to be run on a Smartphone or Emulator](https://github.com/iteratec/LoomoRemotePhone).

It allows users to remotely control the robot, play sounds, use text-to-speech and show a video feed from Loomos front camera.


## Key Features

* Sample code for most of Loomo's SDK functions
* Uses Loomos Connectivity API to connect to the Robot and transmit commands/data
* Head Control (Turn head to the sides and move it up/down)
* Manual Base Control (Forward/backward and turning acceleration) via Joysticks
* Camera Stream
* Text-to-Speech and file playing showcase
* Movement Showcase of Loomo's VLS mode
* Voice recognition and command parsing
* Emoji functionality showcase

## Getting Started

### Prerequisites

* A Segway Loomo Developer Kit - Robot for practical testing
* An Android phone or emulator
* Both Loomo and the phone need to be connected to the same Wifi network

### Installing

1. Clone the git repo into your designated folder.
2. Open the project with Android Studio
3. Connect Loomo to via USB-C
4. Deploy the app on Loomo

### Usage

Before the remote phone app for Loomo can be used, Loomo has to be connected to the same Wifi
as your Android device.
Make sure the corresponding app for the smartphone (https://github.com/iteratec/LoomoRemotePhone) is deployed on the smartphone and running.

Loomo should show his IP on the screen on launch and you simply need to connect to the shown IP via the Phone app.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE.md) file for details

## Related Repositories

[Robot endpoint](https://github.com/iteratec/LoomoRemoteRobot)

[VirtualJoystick view for Android](https://github.com/controlwear/virtual-joystick-android)

[Other sample apps from SegwayRobotics](https://github.com/SegwayRoboticsSamples)

## Disclaimer

This project is a prototype developed by iteratec's students and interns.
It is work in progress in many areas and does not reflect the usual code quality standards
applied by iteratec.

## Acknowledgments

* Jacob Wong - technical engineer for SegwayRobotics
