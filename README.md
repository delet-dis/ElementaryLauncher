# Elementary launcher

<img src="https://user-images.githubusercontent.com/47276603/119395950-6ceb3100-bcfe-11eb-81fd-e3c3946f19b4.png" height = "220" align="right" hspace="50">


[![Android CI](https://github.com/delet-dis/ElementaryLauncher/actions/workflows/android.yml/badge.svg)](https://github.com/delet-dis/ElementaryLauncher/actions/workflows/android.yml)

[![codebeat badge](https://codebeat.co/badges/2f55c8e1-58ae-4e35-a454-27602b58e910)](https://codebeat.co/projects/github-com-delet-dis-elementarylauncher-main)
[![CodeFactor](https://www.codefactor.io/repository/github/delet-dis/elementarylauncher/badge)](https://www.codefactor.io/repository/github/delet-dis/elementarylauncher)

Elementary launcher is an [Android](https://en.wikipedia.org/wiki/Android_(operating_system)) app that helps people with vision problems interact with their home screen.

The goal of project is to make easy to access, use and customize launcher that replaces home screen.

Please check [CONTRIBUTING](CONTRIBUTING.md) page if you want to help.

## Project characteristics and tech-stack

<img src="https://user-images.githubusercontent.com/47276603/119392731-5511ae00-bcfa-11eb-942b-8314b2cc7928.jpg" width="336" align="right" hspace="20">

This project takes advantage of best practices, many popular libraries and tools in the Android ecosystem. 

* Tech-stack
    * [100% Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - perform background operations
    * [Flow](https://developer.android.com/kotlin/flow) - receive live updates from a database
    * [Jetpack](https://developer.android.com/jetpack)
        * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) - in-app navigation
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - perform an action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way
        * [Room](https://developer.android.com/jetpack/androidx/releases/room) - store shortcuts data
        * [Android KTX](https://developer.android.com/kotlin/ktx) -  set of Kotlin extensions
        * [Palette](https://developer.android.com/training/material/palette-colors) -  colors selecting
        * [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) -  implementation of most screens of the application
        * [ViewBinding](https://developer.android.com/topic/libraries/view-binding) -  getting links to interface elements
* Modern Architecture
    * Clean Architecture
    * MVVM
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture) ([ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata), [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation))
    * [Android KTX](https://developer.android.com/kotlin/ktx) - Jetpack Kotlin extensions
* CI 
  * [GitHub Actions](https://github.com/features/actions)
  * Automatic code analyzing by 3rd party online tools
* UI
    * [Material design](https://material.io/design)

### More project screenshots
<img src="https://user-images.githubusercontent.com/47276603/119395044-2fd26f00-bcfd-11eb-86a5-1b051b98ca3d.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395047-306b0580-bcfd-11eb-881c-b3181a286f61.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395048-31039c00-bcfd-11eb-9688-afe7b3c71d86.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395051-31039c00-bcfd-11eb-9b41-fec92c8b39e6.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395053-319c3280-bcfd-11eb-823e-46d802a6410a.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395054-319c3280-bcfd-11eb-8c12-cdc442c89d7e.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395055-3234c900-bcfd-11eb-8dc9-2c9afaa1fd4b.jpg" width="200" hspace="5"  vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395057-32cd5f80-bcfd-11eb-9b8c-f1e04f25b9cc.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395059-32cd5f80-bcfd-11eb-9f94-2aee2f779a50.jpg" width="200" hspace="5" vspace ="10">
<img src="https://user-images.githubusercontent.com/47276603/119395062-3365f600-bcfd-11eb-9ece-84d8a95be347.jpg" width="200" hspace="5" align="left" vspace ="10">
<br/>
<img src="https://user-images.githubusercontent.com/47276603/119395063-33fe8c80-bcfd-11eb-8016-ab6206b28834.jpg" width="200" hspace="5" vspace ="10">

## License
```
MIT License

Copyright (c) 2021 Igor Efimov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
